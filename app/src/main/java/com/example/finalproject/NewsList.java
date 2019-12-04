package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class NewsList extends AppCompatActivity {

    private ProgressBar progressBar;
    NewsAdapter newsAdapter;
    ListView dataList;
    String key;

    ArrayList<Article> articles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent prev = getIntent();
        key = prev.getStringExtra("key");
        setContentView(R.layout.activity_news_list);
        dataList = findViewById(R.id.news_list);
        progressBar = (ProgressBar) findViewById(R.id.progBar);

        MyNetworkQuery net = new MyNetworkQuery();
        net.execute();

        boolean isTablet = findViewById(R.id.fragmentLocation) != null;

        dataList.setOnItemClickListener( (list, item, position, id) -> {

            Bundle dataToPass = new Bundle();

            String a = articles.get(position).getTitle();
            dataToPass.putString("selectedURL", articles.get(position).getUrlToImage());

            if(isTablet)
            {
                NewsFragment dFragment = new NewsFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(NewsList.this, EmptyActivityNews.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, 20); //make the transition
            }
        });
    }

    /**
     * Class handle async task
     */
    private class MyNetworkQuery extends AsyncTask<String, Integer, String> {
        @Override                       //Type 1
        protected String doInBackground(String... strings) {
            String ret = null;

            String queryURL ="https://newsapi.org/v2/everything?q=" + key+ "&apiKey=e5c509c08480483da6e0fca9e25ada92";

            try {
                // Connect to the server:
                URL url = new URL(queryURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                //Set up the JSON object parser:
                // json is UTF-8 by default
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                JSONObject jObject = new JSONObject(result);
                JSONArray aJ = jObject.getJSONArray("articles");

                for (int i=0, j = 0; i < aJ.length() && j<100; i++, j+=10) {
                    String url1 = aJ.getJSONObject(i).getString("url");
                    String  urlImage = aJ.getJSONObject(i).getString("urlToImage");
                    String title = aJ.getJSONObject(i).getString("title");
                    String des = aJ.getJSONObject(i).getString("description");
                    Article temp = new Article(url1, title, des);
                    articles.add(temp);
                    publishProgress(j);
                    try{Thread.sleep(200);} catch(Exception e) { }
                }
            } catch (JSONException ex) {
                ret = "JSON exception";
            } catch (MalformedURLException mfe) {
                ret = "Malformed URL exception";
            } catch (IOException ioe) {
                ret = "IO Exception. Is the Wifi connected?";
            }

            //What is returned here will be passed as a parameter to onPostExecute:
            return ret;
        }

        @Override                   //Type 3
        protected void onPostExecute(String sentFromDoInBackground) {
            super.onPostExecute(sentFromDoInBackground);
            newsAdapter = new NewsAdapter();
            dataList.setAdapter(newsAdapter);
            newsAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override                       //Type 2
        protected void onProgressUpdate(Integer... value) {
            super.onProgressUpdate(value);
            progressBar.setProgress(value[0]);
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    class NewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return articles.size();
        }

        @Override
        public Article getItem(int i) {
            return articles.get(i);
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View thisRow = getLayoutInflater().inflate(R.layout.row_article, null);

            TextView title = thisRow.findViewById(R.id.article_title);
            ImageView img = thisRow.findViewById(R.id.article_image);
            TextView des = thisRow.findViewById(R.id.article_description);

            if(articles.size()>0) {
                title.setText(getItem(i).getTitle());
                des.setText(getItem(i).getDescription());
                String u_Img = getItem(i).getUrlToImage();
                Picasso.with(NewsList.this).load(u_Img).fit().into(img);

            }
            return thisRow;
        }
        @Override
        public long getItemId(int i) {
            return i;
        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 20)
        {
            if(resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                String url = data.getStringExtra("selectedURL");
            }
        }
    }
}
