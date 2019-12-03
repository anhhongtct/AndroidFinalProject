package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.net.URLEncoder;
import java.util.Arrays;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

public class ListActivity extends AppCompatActivity {
    String lon;
    String lat;
    String cou;
    //Array to traverse in the Adapter
    ArrayList<ChargingStation> chargingList = new ArrayList<>();
    BaseAdapter myAdapter;
    ListView theList;
    ProgressBar bar;
    //For fragment purpose
    ArrayAdapter<String> theAdapter;
    ArrayList<String> source = new ArrayList<>( Arrays.asList( "One", "Two", "Three", "Four" ));

    public static final String TITLE = "title";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String TEL = "tel";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_menu);
        Intent previousMess = getIntent();
        lon = URLEncoder.encode(previousMess.getStringExtra("lon"));
        lat = URLEncoder.encode(previousMess.getStringExtra("lat"));
        cou = URLEncoder.encode(previousMess.getStringExtra("cou"));

        //List view
        theList = findViewById(R.id.theList);
        //progress Bar
        bar = findViewById(R.id.weatherBar);
        bar.setVisibility(View.VISIBLE);
        //connect URL
        MyNetworkQuery theQuery = new MyNetworkQuery();
        theQuery.execute();

        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

        theList.setOnItemClickListener( (list, item, position, id) -> {

            String title = chargingList.get(position).getTitle();
            String latitude = chargingList.get(position).getLatitude();
            String longitude  = chargingList.get(position).getLongitude();
            String telephone = chargingList.get(position).getTelephone();

            Bundle dataToPass = new Bundle();
            dataToPass.putString(TITLE, title);
            dataToPass.putString(LAT, latitude);
            dataToPass.putString(LON, longitude);
            dataToPass.putString(TEL, telephone);
//            dataToPass.putInt(ITEM_POSITION, position);
//            dataToPass.putLong(ITEM_ID, id);
            if(isTablet)
            {
                DetailFragment dFragment = new DetailFragment(); //add a DetailFragment
                dFragment.setArguments(dataToPass); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("AnyName") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent viewIntent = new Intent(ListActivity.this, DetailActivity.class);
                viewIntent.putExtra(TITLE, title);
                viewIntent.putExtra(LAT, latitude);
                viewIntent.putExtra(LON, longitude);
                viewIntent.putExtra(TEL, telephone);
                startActivity(viewIntent);
            }
        });

//        theList.setOnItemClickListener((parent, view, position, id) -> {
//            Intent viewIntent = new Intent(ListActivity.this, DetailActivity.class);
//            viewIntent.putExtra(TITLE, chargingList.get(position).getTitle());
//            viewIntent.putExtra(LAT, chargingList.get(position).getLatitude());
//            viewIntent.putExtra(LON, chargingList.get(position).getLongitude());
//            viewIntent.putExtra(TEL, chargingList.get(position).getTelephone());
//            startActivity(viewIntent);
//        });


//         set the adapter for list view
    }

    public class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {return chargingList.size();
        }

        @Override
        public ChargingStation getItem(int position) {
            return chargingList.get(position);
        }

        @Override
        public View getView(int position, View messView, ViewGroup parent) {
            View thisRow;
            thisRow = getLayoutInflater().inflate(R.layout.list, null);
            TextView sendText = thisRow.findViewById(R.id.viewText);

            if(chargingList.size()!=0) sendText.setText(getItem(position).getTitle());
            return thisRow;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    private class MyNetworkQuery extends AsyncTask<String, Integer, String> {
        String [] titleBack = new String [50];
        @Override                       //Type 1
        protected String doInBackground(String... strings) {
            String ret = null;
//            String queryURL = "https://api.openchargemap.io/v3/poi/?countrycode=CA&latitude=45.347571&longitude=-75.756140&maxresults=10";
            String queryURL = "https://api.openchargemap.io/v3/poi/?countrycode="+cou+"&latitude="+ lat +"&longitude="+lon+"&maxresults=10";
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
                //JSONObject jObject = new JSONObject(result);
                JSONArray jArray = new JSONArray(result);
                for (int i=0, j = 0; i < jArray.length() && j<100; i++, j+=10) {
                    JSONObject obj = jArray.getJSONObject(i).getJSONObject("AddressInfo");
                    ChargingStation temp = new ChargingStation(obj);
                    chargingList.add(temp);
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
            myAdapter = new MyListAdapter();
            theList.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
            bar.setVisibility(View.INVISIBLE);
        }

        @Override                       //Type 2
        protected void onProgressUpdate(Integer... value) {
            super.onProgressUpdate(value);
            bar.setProgress(value[0]);
            bar.setVisibility(View.VISIBLE);
        }
    }


}
