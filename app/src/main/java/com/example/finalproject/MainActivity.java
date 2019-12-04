package com.example.finalproject;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * @author: Group
 * @version 01
 * @date 12/2019
 * This class the base for the main menu of the app, contain button to go to different apps
 */


public class MainActivity extends AppCompatActivity {

    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Toolbar tBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button3.setOnClickListener(clk -> startIntent(NewsMain.class));

<<<<<<< HEAD
=======

        //Go to Charge Station main menu
        button1.setOnClickListener(clk -> startIntent(MainMenuChargeStation.class));

>>>>>>> 4f9f1b182b3f4121affa5efe52976b6cfb36ffdd
        tBar = findViewById(R.id.my_toolbar);
        if (tBar != null) {
            setSupportActionBar(tBar);//To display toolbar
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setElevation(0); // or other...
        }

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent recipeIntent = new Intent(MainActivity.this,Recipes.class);
            startActivity(recipeIntent);
            }
        });
        //button listener for news activity
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start ProfileActivity.class
                Intent profileIntent = new Intent(MainActivity.this,
                        MainCurrencyActivity.class);
                startActivity(profileIntent);
            }
        });

      
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_charge_2, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //what to do when the menu item is selected:
            case R.id.icon1:
                break;
            case R.id.icon2:
                break;
            case R.id.icon3:
                startIntent(NewsMain.class);
                break;
            case R.id.icon4:
<<<<<<< HEAD
=======
                startIntent(MainMenuChargeStation.class);
>>>>>>> 4f9f1b182b3f4121affa5efe52976b6cfb36ffdd
                break;
        }
        return true;
    }




    private void startIntent(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);

    }
}
