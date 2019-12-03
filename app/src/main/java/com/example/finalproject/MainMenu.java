package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity {
    private static final String ACTIVITY_NAME = "Find Charging Station";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        //Button to go to other activities
        Button findButton = findViewById(R.id.find_button);
        Button addButton = findViewById(R.id.add_button);

        findButton.setOnClickListener(clk -> {
            Intent findIntent = new Intent(this, FindActivity.class);
            Toast.makeText(this, "You chose "+ ACTIVITY_NAME,Toast.LENGTH_LONG).show();
            startActivity(findIntent); }

        );





    }
}
