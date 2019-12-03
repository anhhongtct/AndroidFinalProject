package com.example.finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


/**
 * @author: Tran Thi Anh Hong
 * @version 01
 * @date 12/2019
 * The home menu class of the Charing Station
 */
public class MainMenuChargeStation extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "Find Charging Station";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_charge_station);

        //Button to go to other activities
        Button findButton = findViewById(R.id.find_button);
        Button addButton = findViewById(R.id.add_button);
        Toolbar tBar = findViewById(R.id.my_toolbar);

        if (tBar != null) {
            setSupportActionBar(tBar);//To display toolbar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0); // or other...
        }

        //Intent to go to Find Menu
        findButton.setOnClickListener(clk -> {
            Intent findIntent = new Intent(this, FindChargeStationActivity.class);
            startActivity(findIntent); }

        );


        //Intent to go to Find Menu
        addButton.setOnClickListener(clk -> {
            Intent likedIntent = new Intent(this, ListActivityChargeStation.class);
            likedIntent.putExtra("activity", "Liked");
            startActivity(likedIntent); }

        );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_charge_2, menu);


	    /* slide 15 material:
	    MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchView sView = (SearchView)searchItem.getActionView();
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }  });

	    */

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //what to do when the menu item is selected:
            case R.id.icon1:
                startIntent(MainCurrencyActivity.class);
                break;
            case R.id.icon2:
                break;
            case R.id.icon3:
                break;
            case R.id.icon4:
                startIntent(MainMenuChargeStation.class);
                break;
            case R.id.search_item:
                alertExample();
                break;
        }
        return true;
    }


    private void startIntent(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    public void alertExample()
    {
        View middle = getLayoutInflater().inflate(R.layout.charge_station_instruct, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("How to use: ")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
               .setView(middle);

        builder.create().show();
    }
}
