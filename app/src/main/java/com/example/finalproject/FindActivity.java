package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FindActivity extends AppCompatActivity {
    private static final String ACTIVITY_NAME = "Find";
    EditText latText;
    EditText lonText;
    EditText countryText;
    Button findButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_menu);
        latText = findViewById(R.id.lat_text);
        lonText = findViewById(R.id.long_text);
        countryText = findViewById(R.id.country_text);
        findButton = findViewById(R.id.find_button);
        if (findButton != null) {
            Toast.makeText(this, "You chose "+ ACTIVITY_NAME,Toast.LENGTH_LONG).show();
            findButton.setOnClickListener(clk -> {
                        Intent findIntent = new Intent(this, ListActivity.class);
                        findIntent.putExtra("lat", latText.getText().toString());
                        findIntent.putExtra("lon", lonText.getText().toString());
                        findIntent.putExtra("cou", countryText.getText().toString());
                        startActivity(findIntent);
            }
            );


        }
    }
}
