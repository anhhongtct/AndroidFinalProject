package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public class NewsMain extends AppCompatActivity {

    String API_KEY = "e5c509c08480483da6e0fca9e25ada92";
    String NEWS_SEARCH = "search";
    private EditText searchNews;
    private ListView listView;
    ArrayList<Article> savedList = new ArrayList<>();

    static final String KEY_URL = "url";
    static final String KEY_URLTOIMAGE = "urlToImage";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_main);

        searchNews = (EditText)findViewById(R.id.search_et);
        listView = (ListView) findViewById(R.id.news_list);


        // Edit text to search news

        searchNews.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                //Enter key Action
                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    Intent searchIntent = new Intent(NewsMain.this, NewsList.class);
                    searchIntent.putExtra("key", searchNews.getText().toString());
                    Toast.makeText(NewsMain.this, "Searching...",Toast.LENGTH_LONG).show();
                    startActivity(searchIntent);
                    return true;
                }
                return false;
            }
        });
    }

}



