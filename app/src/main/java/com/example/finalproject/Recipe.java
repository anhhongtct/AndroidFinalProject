package com.example.finalproject;
import android.graphics.Bitmap;
import android.media.Image;

import androidx.annotation.NonNull;

public class Recipe {

    Bitmap image;
    String title;
    String recipeURL;

    public Recipe (Bitmap image,String title,String recipeURL)
    {
        this.image = image;
        this.title = title;
        this.recipeURL = recipeURL;
    }

    public Bitmap getImage ()
    {
        return image;
    }

    public String getTitle()
    {
        return title;
    }

    public String getRecipeURL ()
    {
        return recipeURL;
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }
}
