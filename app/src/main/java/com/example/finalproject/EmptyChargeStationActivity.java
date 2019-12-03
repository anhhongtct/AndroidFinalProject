package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class EmptyChargeStationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_charge_station);
        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample
        String title = dataToPass.getString(ListActivityChargeStation.TITLE);

        //This is copied directly from FragmentExample.java lines 47-54
        DetailFragment dFragment = new DetailFragment();
        dFragment.setArguments( dataToPass ); //pass data to the the fragment
        dFragment.setTablet(false); //tell the Fragment that it's on a phone.
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, dFragment)
                .commit();
    }


    public void startIntent() {
        Intent intent = new Intent(this, MainMenuChargeStation.class);
        startActivity(intent);
    }
}
