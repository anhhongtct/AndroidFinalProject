package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * @author: Tran Thi Anh Hong
 * @version 01
 * @date 12/2019
 * This class the base class for Find Menu, recieve input from client and send to List Activity to display result
 */

public class FindChargeStationActivity extends AppCompatActivity {
    private static final String ACTIVITY_NAME = "Find";
    EditText latText;
    EditText lonText;
    EditText countryText;
    Button findButton;
    Toolbar tBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_from_user_charge_station);

        tBar = findViewById(R.id.second_toolbar);

        if (tBar != null) {
            setSupportActionBar(tBar);//To display toolbar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0); // or other...
        }

        SharedPreferences mPrefs = getSharedPreferences("SavedInfo", MODE_PRIVATE);
        latText = findViewById(R.id.lat_text);
        lonText = findViewById(R.id.long_text);
        latText.setHint(mPrefs.getString("Latitude", ""));
        lonText.setHint(mPrefs.getString("Longitude", ""));
        findButton = findViewById(R.id.find_button);
        if (findButton != null) {
            findButton.setOnClickListener(clk -> {
                        if(!latText.getText().toString().equals("") && !lonText.getText().toString().equals("")) {
                            try {
                                Intent findIntent = new Intent(this, ListActivityChargeStation.class);
                                findIntent.putExtra("activity", ACTIVITY_NAME);
                                findIntent.putExtra("lat", latText.getText().toString());
                                findIntent.putExtra("lon", lonText.getText().toString());
                                startActivity(findIntent);
                                SharedPreferences.Editor editor = mPrefs.edit();
                                editor.putString("Latitude",latText.getText().toString());
                                editor.putString("Longitude",lonText.getText().toString());
                                editor.commit();
                            } catch (Exception e) {
                            }
                        }
                        else {
                            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                        }
            }
            );


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
        Intent returnIntent  = new Intent(this, MainMenuChargeStation.class);
        startActivity(returnIntent);
        return true;
    }

}
