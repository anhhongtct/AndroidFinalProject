package com.example.finalproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import java.net.URLEncoder;

/**
 * @author: Tran Thi Anh Hong
 * @version 01
 * @date 12/2019
 * This is the class handling async task to display the search list, move to another activity for fragment
 *
 */

public class ListActivityChargeStation extends AppCompatActivity {
    private static final String ACTIVITY_NAME = "LIST ACTIVITY";
    private static final String [] COLUMNS = {MyDatabaseOpenHelperChargeStation.COL_ID, MyDatabaseOpenHelperChargeStation.COL_LAT, MyDatabaseOpenHelperChargeStation.COL_LONG, MyDatabaseOpenHelperChargeStation.COL_NAME, MyDatabaseOpenHelperChargeStation.COL_PHONE};

    String lon;
    String lat;

    //Array to traverse in the Adapter
    ArrayList<ChargingStation> chargingList = new ArrayList<>();
    BaseAdapter myAdapter;
    ListView theList;
    ProgressBar bar;

    MyDatabaseOpenHelperChargeStation opener;

    SQLiteDatabase db;

    public static final String TITLE = "title";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String TEL = "tel";
    public boolean isFind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_menu_charge);
        Toolbar tBar = findViewById(R.id.my_toolbar);

        if (tBar != null) {
            setSupportActionBar(tBar);//To display toolbar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0); // or other...
        }

        //Get intent from previous activity
        Intent previousMess = getIntent();
        //Get the List view
        theList = findViewById(R.id.theList);

        if(previousMess.getStringExtra("activity").equals("Find")){//go from click Find Button on previous activity
            isFind = true;
            lon = URLEncoder.encode(previousMess.getStringExtra("lon"));
            lat = URLEncoder.encode(previousMess.getStringExtra("lat"));

            //progress Bar
            bar = findViewById(R.id.weatherBar);
            bar.setVisibility(View.VISIBLE);
            //connect URL to load information
            MyNetworkQuery theQuery = new MyNetworkQuery();
            theQuery.execute();
        } else { //click Favorite list
            isFind = false;
            theList.setAdapter( myAdapter = new MyListAdapter());
            chargingList.addAll(readDatabase());
            myAdapter.notifyDataSetChanged();
        }

        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

        theList.setOnItemClickListener( (list, item, position, id) -> { //listen to click at a row

            String title = chargingList.get(position).getTitle();
            String latitude = chargingList.get(position).getLatitude();
            String longitude  = chargingList.get(position).getLongitude();
            String telephone = " ";
            if(chargingList.get(position).getTelephone()!=null) {
                telephone = chargingList.get(position).getTelephone().equals("null") ? "No contact information" : chargingList.get(position).getTelephone();
            }
            Bundle dataToPass = new Bundle();
            dataToPass.putString(TITLE, title);
            dataToPass.putString(LAT, latitude);
            dataToPass.putString(LON, longitude);
            dataToPass.putString(TEL, telephone);
            dataToPass.putBoolean("Boolean",isFind);
            dataToPass.putInt("index", position);
            if(isTablet)
            {
                DetailFragment dFragment = new DetailFragment(); //add a DetailFragment
                dFragment.setArguments(dataToPass); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Replace the fragment in FrameLayout
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent viewIntent = new Intent(ListActivityChargeStation.this, EmptyChargeStationActivity.class);
                viewIntent.putExtras(dataToPass);
                startActivityForResult(viewIntent, 50);
            }
        });

    }

    public Cursor runQuery(String[] columns) {
        //query all the results from the database:
        opener = new MyDatabaseOpenHelperChargeStation(this);
        db = opener.getWritableDatabase();
        Cursor results = db.query(false, MyDatabaseOpenHelperChargeStation.TABLE_NAME, columns, null, null, null, null, null, null);
        return results;
    }

    /**
     * helper function to read the database
     * @return the ArrayList of Charging Station after read database
     */
    private ArrayList<ChargingStation> readDatabase() {
        ArrayList <ChargingStation> list = new ArrayList<>();
        Cursor results = runQuery(COLUMNS);

        int latIndex = results.getColumnIndex(MyDatabaseOpenHelperChargeStation.COL_LAT);
        int lonIndex = results.getColumnIndex(MyDatabaseOpenHelperChargeStation.COL_LONG);
        int nameIndex = results.getColumnIndex(MyDatabaseOpenHelperChargeStation.COL_NAME);
        int phoneIndex = results.getColumnIndex(MyDatabaseOpenHelperChargeStation.COL_PHONE);
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelperChargeStation.COL_ID);

        //iterate over the results, populate the Arraylist of charge Station
        results.moveToFirst();
        while(results.moveToNext())
        {
            String lat = results.getString(latIndex);
            String lon = results.getString(lonIndex);
            String name = results.getString(nameIndex);
            String phone = results.getString(phoneIndex);
            long id = results.getLong(idColIndex);
            //add the new Contact to the array list:
            list.add(new ChargingStation(id, name, lat, lon, phone));
        }

        return list;
    }

    public boolean writeDatabase (String lat, String lon, String name, String phoneNo) {
        ArrayList <ChargingStation> favList = readDatabase();
        ChargingStation check = new ChargingStation(name, lat, lon, phoneNo);
        if (favList.size() == 0) {
            writeRow(lat, lon, name, phoneNo);
            return true;
        } else {
            for (ChargingStation a : favList) {
                if (a.compareTo(check) == 0) return false;
            }
            writeRow(lat, lon, name, phoneNo);

            return true;
        }

    }

    /**
     *
     * privte helper method to write a row to the database table
     * @param lat
     * @param lon
     * @param name
     * @param phoneNo
     */

    private void writeRow(String lat, String lon, String name, String phoneNo) {
        ContentValues newValues = new ContentValues();
        newValues.put(MyDatabaseOpenHelperChargeStation.COL_LAT, lat);
        newValues.put(MyDatabaseOpenHelperChargeStation.COL_LONG, lon);
        newValues.put(MyDatabaseOpenHelperChargeStation.COL_NAME, name);
        newValues.put(MyDatabaseOpenHelperChargeStation.COL_PHONE, phoneNo);
        long id = db.insert(MyDatabaseOpenHelperChargeStation.TABLE_NAME, "NullColumnName", newValues);
    };

    /**
     * The base clase for Adapter to populate data of the List View
     */
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

            String t = getItem(position).getTitle();

            if(chargingList.size()!=0) sendText.setText(getItem(position).getTitle());
            return thisRow;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }


    /**
     * Class handle async task
     */
    private class MyNetworkQuery extends AsyncTask<String, Integer, String> {
        String [] titleBack = new String [50];
        @Override                       //Type 1
        protected String doInBackground(String... strings) {
            String ret = null;
//            String queryURL = "https://api.openchargemap.io/v3/poi/?countrycode=CA&latitude=45.347571&longitude=-75.756140&maxresults=10";
            String queryURL = "https://api.openchargemap.io/v3/poi/?countrycode=CA"+"&latitude="+ lat +"&longitude="+lon+"&maxresults=10";
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

    public void deleteRowId(int index)
    {
        MyDatabaseOpenHelperChargeStation db = new MyDatabaseOpenHelperChargeStation(this);
        db.deleteRow(String.valueOf(chargingList.get(index).getId()));
        chargingList.remove(index);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 50)
        {
            if(resultCode == 51) //if you hit the delete button instead of back button
            {
                if(!writeDatabase(data.getStringExtra("lat"), data.getStringExtra("lon"), data.getStringExtra("title"),
                    data.getStringExtra("tel")))
                    Toast.makeText(this, "Already in database", Toast.LENGTH_SHORT).show();
            } else if (resultCode == 52) {
                deleteRowId(data.getIntExtra("index",-1));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_charge_1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MainMenuChargeStation.class);
        startActivity(intent);
        return true;
    }



}
