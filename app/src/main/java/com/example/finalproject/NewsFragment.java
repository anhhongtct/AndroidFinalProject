package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class NewsFragment extends Fragment {

    private boolean isTablet;
    private Bundle dataFromActivity;
    private String url;

    public void setTablet(boolean tablet) { isTablet = tablet; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        url = dataFromActivity.getString("selectedURL" );

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.news_detail, container, false);

        //show the message
        WebView webView = (WebView) result.findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        return result;
    }
}
