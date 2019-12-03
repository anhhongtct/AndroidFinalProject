package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsList extends AppCompatActivity {

    private ProgressBar progressBar;
    NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        progressBar = (ProgressBar) findViewById(R.id.progBar);
        newsAdapter = new NewsAdapter(NewsList.this, R.layout.row_article);

    }


    class NewsAdapter extends ArrayAdapter {

        Activity activity;
        ArrayList<Article> articles = new ArrayList<>();

        public NewsAdapter(Activity activity, int resources){

            super(activity, resources);
        }
        @Override
        public int getCount() {
            return articles.size();
        }

        @Override
        public Article getItem(int i) {
            return articles.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ArticleView articleView = new ArticleView((getApplicationContext()));
            Article article = articles.get(i);
            articleView.setImage(article.getUrlToImage());
            articleView.setTitle(article.getTitle());
            articleView.setDescription(article.getDescription());

            return articleView;
        }
    }
}
