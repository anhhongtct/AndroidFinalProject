package com.example.finalproject;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Recipes extends AppCompatActivity {

    ArrayList<Recipe> list;
    BaseAdapter myadapter;
    ListView viewList;
    Button search;
    EditText searchBar;
    SharedPreferences perf;
    final Context context = this;
    Cursor results;
    SQLiteDatabase db;

    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_URL = "URL";
    public static final String ITEM_TITLE = "TITLE";
    public static final int EMPTY_ACTIVITY = 345;
    public static final String ITEM_ID = "ID";
    public static final Image ITEM_IMAGE = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_default_layout);
        list = new ArrayList<>();
        viewList = (ListView)findViewById(R.id.theList);

        Toolbar myToolbar = (Toolbar)findViewById(R.id.toolbarRecipe);
        setSupportActionBar(myToolbar);

        MyDataBaseOpenHelperRecipe dbOpen = new MyDataBaseOpenHelperRecipe(this);
        db = dbOpen.getWritableDatabase();
        String [] columns = {MyDataBaseOpenHelperRecipe.COL_ID,MyDataBaseOpenHelperRecipe.COL_TITLE,MyDataBaseOpenHelperRecipe.COL_URL};
        results = db.query(false,MyDataBaseOpenHelperRecipe.TABLE_NAME, columns , null, null, null, null, null,null);

        int titleIndex= results.getColumnIndex(MyDataBaseOpenHelperRecipe.COL_TITLE);
        int idColIndex = results.getColumnIndex(MyDataBaseOpenHelperRecipe.COL_ID);
        int urlIndex = results.getColumnIndex(MyDataBaseOpenHelperRecipe.COL_URL);
        int picture = results.getColumnIndex(MyDataBaseOpenHelperRecipe.COL_IMAGE);
        viewList.setAdapter(myadapter = new MyListAdapter());

        while (results.moveToNext())
        {
            String title = results.getString(titleIndex);
            String url = results.getString(urlIndex);
            long id = results.getLong(idColIndex);
            Bitmap image = null;
            FileInputStream fis = null;
            try {
                //fis = openFileInput(results.getString(picture));
                fis = openFileInput("Picture"+id+".png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            image = BitmapFactory.decodeStream(fis);

            list.add(new Recipe(image,title,url));

            Toast toast = Toast.makeText(getApplicationContext(),"Added Recipe Back",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
            toast.show();
        }
        myadapter.notifyDataSetChanged();

        final boolean isTablet = findViewById(R.id.framesRecipe) != null;
        viewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> viewList, View view, int position, long id) {

                Bundle dataToPass = new Bundle();
                dataToPass.putString(ITEM_SELECTED, String.valueOf(list.get(position)));
                dataToPass.putInt(ITEM_POSITION, position);
                dataToPass.putLong(ITEM_ID,id);
                dataToPass.putString(ITEM_URL,list.get(position).getRecipeURL());
                dataToPass.putString(ITEM_TITLE,list.get(position).getTitle());
                //dataToPass.putParcelable(String.valueOf(ITEM_IMAGE),list.get(position).getImage());

                if (isTablet)
                {
                    RecipeTabletDisplay dfrag = new RecipeTabletDisplay();
                    dfrag.setArguments( dataToPass);
                    dfrag.setTablet(true);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.framesRecipe, dfrag) //Add the fragment in FrameLayout
                            //.addToBackStack("AnyName") //make the back button undo the transaction
                            .commit(); //actually load the fragment.
                }
                else
                {
                    Intent nextActivity = new Intent(Recipes.this, RecipePhoneDisplay.class);
                    nextActivity.putExtras(dataToPass); //send data to next activity
                    startActivityForResult(nextActivity, EMPTY_ACTIVITY);
                }

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.recipemenu,menu);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search_recipe);
        final SearchView sView = (SearchView)searchItem.getActionView();
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override public boolean onQueryTextSubmit(String query) {
                list.clear();
                new RecipeQuery().execute();
                myadapter.notifyDataSetChanged();
                return true;  }
            @Override public boolean onQueryTextChange(String newText) {return false;}
        });

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.CarCharger:
                Intent moveToCar = new Intent (Recipes.this,MainMenuChargeStation.class);
                startActivity(moveToCar);
                break;

            case R.id.Currency:
                Intent moveToCurrency = new Intent (Recipes.this,MainCurrencyActivity.class);
                startActivity(moveToCurrency);
                break;

            case R.id.otherOTopic:
                Intent moveToitem3 = new Intent (Recipes.this,null);
                startActivity(moveToitem3);
                break;

            case R.id.ott:
                Intent moveToOtt = new Intent (Recipes.this,MainActivity.class);
                startActivity(moveToOtt);
                break;

            case R.id.aboutRecipe:
                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle("About this segment");
                Button close = (Button) dialog.findViewById(R.id.buttonRecipeInfo);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
        }
        return true;
    }

    private class RecipeQuery extends AsyncTask<String, Integer, String> {

        String title;
        String url;
        String pictureurl;
        Bitmap image;
        JSONObject json;
        String ret = null;
        @Override
        protected String doInBackground(String... strings) {
            String baseURL = "http://torunski.ca/FinalProjectChickenBreast.json";

            //look up the title,url and create a picture from a url all using the Json version
            //don't give up!
            try {
                URL jsonURL = new URL(baseURL);
                HttpURLConnection connection = (HttpURLConnection)jsonURL.openConnection();
                InputStream inStreamy = connection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inStreamy, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                json = new JSONObject(result);
                JSONArray searcher = json.getJSONArray("recipes");
                for (int i = 0; i <searcher.length(); i++)
                {
                    JSONObject c = searcher.getJSONObject(i);
                    title = c.getString("title");
                    url = c.getString("source_url");
                    pictureurl = "https://www.101cookingfortwo.com/wp-content/uploads/2018/01/Oven-Baked-Chicken-Legs-1c.jpg"; //c.getString("image_url");

                    URL urlx = new URL(pictureurl);
                    HttpURLConnection connectionr = (HttpURLConnection) urlx.openConnection();
                    connectionr.connect();
                    InputStream input  = connectionr.getInputStream();
                    Bitmap imager = null;//BitmapFactory.decodeStream(input);
                    ContentValues newRowValues = new ContentValues();
                    newRowValues.put(MyDataBaseOpenHelperRecipe.COL_TITLE,title);
                    newRowValues.put(MyDataBaseOpenHelperRecipe.COL_URL,url);
                    //image goes here
                    long newId = db.insert(MyDataBaseOpenHelperRecipe.TABLE_NAME,null,newRowValues);
                    newRowValues.put(MyDataBaseOpenHelperRecipe.COL_IMAGE,"Picture"+newId+".png");

                    int responseCode = connectionr.getResponseCode();
                    if (responseCode == 200) {
                        imager = BitmapFactory.decodeStream(input);
                    }
                    publishProgress(100);

                    FileOutputStream outputStream = openFileOutput("Picture"+newId+".png", Context.MODE_PRIVATE);
                    imager.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();

                    FileInputStream fis = null;
                    try {
                        fis = openFileInput("Picture"+newId+".png");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    image = BitmapFactory.decodeStream(fis);

                    list.add(new Recipe(image,title,url));
                }





            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return ret;
        }

        public boolean fileExistance(String fname) {
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onPostExecute(String s) {
            myadapter.notifyDataSetChanged();
        }

    }

    private class MyListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int p) {
            return p;
        }

        @Override
        public View getView(int p, View recycled, ViewGroup parent) {

            //Ask the professor questions about this one: like how to transfer from one section to the other.
            //you can do it
            TextView title;
            ImageView image;
            View thisRow;

            thisRow = getLayoutInflater().inflate(R.layout.display_recipe,null);
            title = (TextView)thisRow.findViewById(R.id.title_recipe);
            title.setText(list.get(p).getTitle());
            image = (ImageView)thisRow.findViewById(R.id.recipePreview);
            image.setImageBitmap(list.get(p).getImage());

            //Snackbar.make(thisRow,"Recipes Loaded.",Snackbar.LENGTH_SHORT).show();

            return thisRow;
        }
    }


}
