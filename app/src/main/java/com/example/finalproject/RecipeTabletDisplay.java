package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipeTabletDisplay extends Fragment {

    private boolean isTablet;
    private Bundle dataFromActivity;
    private long id;

    public void setTablet(boolean tablet) { isTablet = tablet; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(Recipes.ITEM_ID);

        View result =  inflater.inflate(R.layout.activity_recipe_tablet_display, container, false);

        TextView title = (TextView)result.findViewById(R.id.titleTablet);
        title.setText(dataFromActivity.getString(Recipes.ITEM_SELECTED));

        TextView url = (TextView)result.findViewById(R.id.hyperTablet);
        url.setText(dataFromActivity.getString(Recipes.ITEM_URL));

        return result;
    }
}