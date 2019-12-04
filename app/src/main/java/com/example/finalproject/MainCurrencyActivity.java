package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.example.finalproject.MyCurrencyActivity.ITEM_AMOUNT;
import static com.example.finalproject.MyCurrencyActivity.ITEM_NAME;

public class MainCurrencyActivity extends AppCompatActivity {
    public static final int REQUEST_ADD_CURRENCY = 1;
    public static final int REQUEST_HISTORY_CURRENCY = 2;

    public ToCurrencyAdapter adapter;
    private ListView currencyContainer;

    private String fromCurrencyName;
    private double fromCurrencyAmount;

    MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(this);
    public SQLiteDatabase db;

    /*
     * to save all current currency list
     */
    ArrayList<CurrencyItem> currencyList = new ArrayList<CurrencyItem>();
    private String appLatestCurrency ="LatestCurrency";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_currency);
//        getSupportActionBar().setTitle(R.string.app_title);

        currencyContainer = (ListView) findViewById(R.id.currencylist);
        adapter = new ToCurrencyAdapter(MainCurrencyActivity.this);

        //get a database:
        db = dbOpener.getWritableDatabase();

        //query all the results from the database:
        String [] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_NAME, MyDatabaseOpenHelper.COL_ISFROM};
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        /**
         * The database version number
         * The number of columns in the cursor.
         * The number of results in the cursor
         */
        Log.i("Cursor Information", "Database Version:" + MyDatabaseOpenHelper.VERSION_NUM + ", Number of Columns:" + results.getColumnCount()
                + ", Number of Results:" + results.getCount());

        /**
         * ind the column indices:
         */
        int fromColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ISFROM);
        int nameColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_NAME);
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);

        /**
         * iterate over the results, return true if there is a next item:
         */
        while(results.moveToNext())
        {
            Boolean isFrom = (results.getInt(fromColumnIndex) == 1);
            String name = results.getString(nameColIndex);
            long id = results.getLong(idColIndex);

            if(isFrom == false) {
                //add the new Message to the array list:
                adapter.add(new ToCurrencyItem(name, 0, false, id));
            }
            else {
                TextView from = (TextView) findViewById(R.id.fromCurrency);
                from.setText(name);
            }
        }

        currencyContainer.setAdapter(adapter);  // to populate ListView with data call setAdapter()

        TextView from = (TextView) findViewById(R.id.fromCurrency);
        fromCurrencyName = from.getText().toString();

        Button convertButton = (Button) findViewById(R.id.convert);
        convertButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(adapter.getCount() > 0) {
                    ConvertCurrency();
                }
                else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainCurrencyActivity.this);
                    // set title
                    alertDialogBuilder.setTitle("Add new currency");
                    // set dialog message
                    alertDialogBuilder
                            .setMessage("No foreign currency to convert." + "\n" + "Do you want to add other currency?")
                            .setCancelable(false)
                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked,
                                    LoadCurrencyList();
                                }
                            })
                            .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }

            }

        });

        Button addButton = (Button) findViewById(R.id.addcurrency);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                LoadCurrencyList();
            }
        });

        /**
         * ListView on item selected listener.
         */
        currencyContainer.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                long viewId = view.getId();

                ToCurrencyItem msg = adapter.getItem(position);
                if (viewId == R.id.currency_history) {
                    //click history button in the listview item
                    LoadHistoryCurrency(msg.getName());
                } else if (viewId == R.id.currency_remove) {
                    //click history button in the listview item
                    RemoveCurrency(msg.getName());
                } else {
                    //click listview item
                    MoveFromCurrency(msg.getName());
                }
            }
        });
    }

    /**
     * move a selected currency to "From Currency"
     * @param name
     */
    private void MoveFromCurrency(String name) {
        //1. update state of current "From Currency", true->false
        long fromId = updateCurrency(fromCurrencyName, false);

        //2. add it to arraylist of adapter
        adapter.add(new ToCurrencyItem(fromCurrencyName, 0, false, fromId));

        //3. update state of selected currency, false->true
        updateCurrency(name, true);

        //4. removeit from arraylist of adapter
        adapter.remove(name);

        //5. set new "From Currency"
        TextView from = (TextView) findViewById(R.id.fromCurrency);
        from.setText(name);
        fromCurrencyName = name;

        //6. Reset current amount
        EditText amountText=(EditText)findViewById(R.id.fromAmount);
        amountText.setText("");

        //refresh listview
        adapter.notifyDataSetChanged();
    }

    /**
     * remove a selected currency from "To Currency"
     * @param name
     */
    private void RemoveCurrency(String name) {
        deleteCurrency(name);
        Toast.makeText(this, "Removed currency [" + name + "] from favorite.", Toast.LENGTH_LONG).show();
    }

    /**
     *
     */
    private void ConvertCurrency() {
        /*
         * Get the latest foreign exchange reference rates.
         * to set the progressBar’s visibility to View
         */
        ProgressBar loadingImage = (ProgressBar) findViewById(R.id.progressBar);
        loadingImage.setVisibility(ProgressBar.VISIBLE);

        EditText fromAmount = (EditText) findViewById(R.id.fromAmount);
        if(fromAmount.getText().toString().trim().length() == 0)
            fromCurrencyAmount = 1; //by default
        else
            fromCurrencyAmount = Double.parseDouble(fromAmount.getText().toString());

        String symbols = "";

        //make symbol list, ex) "KRW,USD,CAD"
        for(int i = 0; i < adapter.getCount() ; i++) {
            ToCurrencyItem msg = adapter.getItem(i);
            if(i == 0) {
                symbols = msg.getName();
            }
            else {
                symbols += "," + msg.getName();
            }
        }

        new MainCurrencyActivity.LatestCurrencyQuery(symbols).execute(appLatestCurrency);
    }

    /**
     *
     */
    private class LatestCurrencyQuery extends AsyncTask<String, Integer, String> {
        ArrayList<HistoryItem> historyList = new ArrayList<HistoryItem>();

        private String symbols;

        public LatestCurrencyQuery(String symbols) {
            this.symbols = symbols;
        }

        @Override                       //Type 1
        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            publishProgress(25);

            /**
             * API https://api.exchangeratesapi.io/latest?base=KRW&symbols=USD,CAD,HKD
             * get the latest currency
             */
            String urlString =
                    "https://api.exchangeratesapi.io/latest?base=" + fromCurrencyName + "&symbols=" + symbols;

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

                /**
                 * json is UTF-8 by default
                 */
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
                    double currency = jObject.getDouble(key);

                    //update to amount in the ArrayList
                    UpdateCurrencyAmount(key, currency * fromCurrencyAmount);
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

    /**
     *
     * @param key
     * @param amount
     */
    private void UpdateCurrencyAmount(String key, double amount) {
        //update amount in the ArrayList
        adapter.getItem(key).setAmount(amount);
    }

    /**
     *
     * @param name
     */
    private void LoadHistoryCurrency(String name) {
        Intent messageIntent = new Intent(MainCurrencyActivity.this,
                HistoryCurrencyActivity.class);
        messageIntent.putExtra("symbols", name);
        messageIntent.putExtra("base", fromCurrencyName);
        startActivityForResult(messageIntent, REQUEST_HISTORY_CURRENCY);
    }

    /**
     *
     */
    private void LoadCurrencyList() {
        //nothing enter for amount
        EditText fromAmount = (EditText) findViewById(R.id.fromAmount);
        if(fromAmount.getText().toString().trim().length() == 0)
            fromCurrencyAmount = 1;
        else
            fromCurrencyAmount = Double.parseDouble(fromAmount.getText().toString());

        // Start MyCurrencyActivity.class
        Intent addIntent = new Intent(MainCurrencyActivity.this,
                MyCurrencyActivity.class);
        Bundle params = new Bundle();
        //pass the current to Currencies
        //params.putStringArrayList("toCurrency", );
        params.putString("fromCurrency", fromCurrencyName);
        params.putDouble("fromAmount", fromCurrencyAmount);
        addIntent.putExtras(params);
        startActivityForResult(addIntent, REQUEST_ADD_CURRENCY);
    }

    /**
     *
     * @param isFrom
     * @param name
     * @return
     */
    private long InsertCurrency(boolean isFrom, String name) {
        //add to the database and get the new ID
        ContentValues newRowValues = new ContentValues();
        //put string name in the COL_NAME column:
        newRowValues.put(MyDatabaseOpenHelper.COL_NAME, name);
        //put string email in the COL_ISFROM column:
        newRowValues.put(MyDatabaseOpenHelper.COL_ISFROM, isFrom);
        //insert in the database:
        long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

        return newId;
    }

    public long deleteCurrency(long currencyId) {
        //delete message from database
        String id = Long.toString(currencyId);
        long newId = db.delete(MyDatabaseOpenHelper.TABLE_NAME, MyDatabaseOpenHelper.COL_ID + "=?", new String[]{id});

        //delete message from ArrayList
        adapter.remove(currencyId);

        //refresh listview
        adapter.notifyDataSetChanged();

        return newId;
    }

    public long deleteCurrency(String name) {
        //delete message with name from database
        long newId = db.delete(MyDatabaseOpenHelper.TABLE_NAME, MyDatabaseOpenHelper.COL_NAME + "=?", new String[]{name});

        //delete currency with name from ArrayList
        adapter.remove(name);

        //refresh listview
        adapter.notifyDataSetChanged();

        return newId;
    }

    public long updateCurrency(String name, boolean state) {
        //update state with name from database
        ContentValues updateRowValues = new ContentValues();
        //put string email in the COL_ISFROM column:
        updateRowValues.put(MyDatabaseOpenHelper.COL_ISFROM, state);
        long newId = db.update(MyDatabaseOpenHelper.TABLE_NAME, updateRowValues,MyDatabaseOpenHelper.COL_NAME + "=?", new String[]{name});

        return newId;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_CURRENCY && resultCode != RESULT_CANCELED) {
            String newCurrency = data.getStringExtra(ITEM_NAME);
            double newAmount = data.getDoubleExtra(ITEM_AMOUNT, 0);

            long id = InsertCurrency(false, newCurrency);
            adapter.add(new ToCurrencyItem(newCurrency, newAmount, false, id));

            adapter.notifyDataSetChanged();

            Toast.makeText(this, "Added new currency [" + newCurrency + "] for favorite.", Toast.LENGTH_LONG).show();
        }
    }
}
