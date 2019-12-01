package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import static com.example.finalproject.MainCurrencyActivity.APP_TITLE;

public class HistoryCurrencyActivity extends AppCompatActivity {
    public HistoryAdapter adapter;
    private ListView historyContainer;

    private String appCurrency = "HistoryCurrency";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_currency);
        getSupportActionBar().setTitle(APP_TITLE);

        historyContainer = (ListView) findViewById(R.id.historylist);
        adapter = new HistoryAdapter(this);

        String symbols = getIntent().getStringExtra("symbols");
        String base = getIntent().getStringExtra("base");

        TextView titleHistory = (TextView) findViewById(R.id.title_history);
        titleHistory.setText(base + " to " + symbols);

        //get current date
        Calendar cal = Calendar.getInstance();
        Date c = cal.getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = df.format(c);

        //get -30 days date
        cal.add(Calendar.DATE, -30);
        String startDate = df.format(cal.getTime());

        TextView dateHistory = (TextView) findViewById(R.id.date_history);
        dateHistory.setText(startDate + " ~ " + currentDate);

        //looking for history of currency for the latest 30 days
        LoadHistoryCurrency(startDate, currentDate, symbols, base);

    }

    private void LoadHistoryCurrency(String startDate, String currentDate, String symbols, String base) {
        // to set the progressBar’s visibility to View
        ProgressBar loadingImage = (ProgressBar) findViewById(R.id.progressBar);
        loadingImage.setVisibility(ProgressBar.VISIBLE);

        new HistoryCurrencyActivity.HistoryCurrencyQuery(startDate, currentDate, symbols, base).execute(appCurrency);

    }

    private class HistoryCurrencyQuery extends AsyncTask<String, Integer, String> {
        ArrayList<HistoryItem> historyList = new ArrayList<HistoryItem>();

        private String startDate;
        private String currentDate;
        private String symbols;
        private String base;

        public HistoryCurrencyQuery(String startDate, String currentDate, String symbols, String base) {
            this.startDate = startDate;
            this.currentDate = currentDate;
            this.symbols = symbols;
            this.base = base;
        }

        @Override                       //Type 1
        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            publishProgress(25);

            //API: https://api.exchangeratesapi.io/history?start_at=2019-01-01&end_at=2019-02-01&symbols=KRW&base=USD
            //get the latest currency
            String urlString =
                    "https://api.exchangeratesapi.io/history?start_at=" + startDate + "&end_at=" + currentDate + "&symbols=" + symbols + "&base=" + base;

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
                JSONObject jObject = new JSONObject(result).getJSONObject("rates");
                // Get keys from json
                Iterator<String> panelKeys = jObject.keys();

                while(panelKeys.hasNext()) {
                    String key= panelKeys.next();

                    JSONObject jDateObject = jObject.getJSONObject(key);
                    double currency = jDateObject.getDouble(symbols);

                    adapter.add(new HistoryItem(key, currency));
                }
                stream.close();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
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

            //sort by date
            Collections.sort(adapter.getAll(), new SortbyDate());
            historyContainer.setAdapter(adapter);  // to populate ListView with data call setAdapter()
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
