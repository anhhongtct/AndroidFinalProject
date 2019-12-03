package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipePhoneDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_phone_display);

        final Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample
        int id = dataToPass.getInt(Recipes.ITEM_ID);
        String message = dataToPass.getString(Recipes.ITEM_SELECTED);

        TextView title = (TextView)findViewById(R.id.recipetitle);
        title.setText(dataToPass.getString(Recipes.ITEM_SELECTED));

        TextView url = (TextView)findViewById(R.id.recipelink);
        url.setText(dataToPass.getString(Recipes.ITEM_URL));

    }
}