package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import static com.example.finalproject.MainCurrencyActivity.REQUEST_ADD_CURRENCY;

public class MyCurrencyActivity extends AppCompatActivity {
    public static final String ITEM_NAME ="CURRENCY_NAME";
    public static final String ITEM_AMOUNT ="CURRENCY_AMOUNT";
    public CurrencyAdapter adapter;
    private ListView currencyContainer;

    private String base;
    private double amount;
    private String appCurrency = "AddCurrency";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_currency);
//        getSupportActionBar().setTitle(R.string.app_title);

        currencyContainer = (ListView) findViewById(R.id.currencyListAdd);
        adapter = new CurrencyAdapter(this);


        Bundle b = getIntent().getExtras();
        base = b.getString("fromCurrency");
        amount = b.getDouble("fromAmount");

        // to set the progressBar’s visibility to View
        ProgressBar loadingImage = (ProgressBar) findViewById(R.id.progressBar);
        loadingImage.setVisibility(ProgressBar.VISIBLE);

        new LatestCurrencyQuery().execute(appCurrency);

        // ListView on item selected listener.
        currencyContainer.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                CurrencyItem msg = adapter.getItem(position);

                Intent resultIntent = getIntent();
                resultIntent.putExtra(ITEM_NAME, msg.getName());
                resultIntent.putExtra(ITEM_AMOUNT, msg.getAmount());
                setResult(REQUEST_ADD_CURRENCY, resultIntent);
                finish();
            }
        });

    }

    private class LatestCurrencyQuery extends AsyncTask<String, Integer, String> {
        ArrayList<CurrencyItem> currencyList = new ArrayList<CurrencyItem>();

        @Override                       //Type 1
        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            publishProgress(25);

            //API https://api.exchangeratesapi.io/latest?base=KRW
            //get the latest currency
            String urlString =
                    "https://api.exchangeratesapi.io/latest?base=" + base;

            try {
                URL url = new URL(urlString);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();

                InputStream stream = urlConnection.getInputStream();

                // json is UTF-8 by default
                BufferedReader readerJson = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = readerJson.readLine()) != null) {
                    sb.append(line + "\n");
                }

                publishProgress(50);

                String result = sb.toString();
                JSONObject jObject = new JSONObject(result).getJSONObject("rates"); ;
                // Get keys from json
                Iterator<String> panelKeys = jObject.keys();

                while(panelKeys.hasNext()) {
                    String key= panelKeys.next();
                    double currency = jObject.getDouble(key) * amount;

                    adapter.add(new CurrencyItem(key, currency));
                }
                stream.close();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }


            publishProgress(100);
            return "";
        }

        @Override                   //Type 3
        protected void onPostExecute(String sentFromDoInBackground) {
            //update GUI Stuff:
            currencyContainer.setAdapter(adapter);  // to populate ListView with data call setAdapter()

            //sort by name
            Collections.sort(adapter.getAll(), new SortbyName());
            currencyContainer.setAdapter(adapter);  // to populate ListView with data call setAdapter()

            adapter.notifyDataSetChanged();

            ProgressBar loadingImage = (ProgressBar) findViewById(R.id.progressBar);
            loadingImage.setVisibility(ProgressBar.INVISIBLE);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            ProgressBar loadingImage = (ProgressBar) findViewById(R.id.progressBar);
            loadingImage.setProgress(values[0]);
        }
    }

}
