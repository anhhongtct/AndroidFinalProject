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

public class MainActivity extends AppCompatActivity {

    /*
    public void convertToEuro(View view){
        EditText editText = (EditText) findViewById(R.id.edtText);
        int dollars = Integer.parseInt(editText.getText().toString());
        int euro = 2000;
        double result = dollars * euro ;
        Toast.makeText(MainActivity.this, Double.toString(result), Toast.LENGTH_LONG).show();
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start ProfileActivity.class
                Intent profileIntent = new Intent(MainActivity.this,
                        MainCurrencyActivity.class);
                startActivity(profileIntent);
            }
        });
    }
}
