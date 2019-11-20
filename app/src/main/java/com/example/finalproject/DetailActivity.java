package com.example.finalproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    private TextView titleText, latText, lonText, telText;
    private Button googleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_menu);

        titleText = findViewById(R.id.title_View);
        latText = findViewById(R.id.lat_View);
        lonText = findViewById(R.id.lon_View);
        telText = findViewById(R.id.telephone_View);
        googleButton = findViewById(R.id.google_btn);
        Intent previousMess = getIntent();
        titleText.setText(previousMess.getStringExtra("title"));
        latText.setText(previousMess.getStringExtra("lat"));
        lonText.setText(previousMess.getStringExtra("lon"));
        telText.setText(previousMess.getStringExtra("tel"));
        googleButton.setOnClickListener(clk -> {
            // Create a Uri from an intent string. Use the result to create an Intent.
            String uri = "geo:0,0?q="+previousMess.getStringExtra("lat")+","+previousMess.getStringExtra("lon") + "(label)";
            Uri gmmIntentUri = Uri.parse(uri);

            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps");

            // Attempt to start an activity that can handle the Intent
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }
        });

    }




}
