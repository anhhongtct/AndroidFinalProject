package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.List;


public class DetailFragment extends Fragment {

    private boolean isTablet;
    private Bundle dataFromActivity;

    public void setTablet(boolean tablet) { isTablet = tablet; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
//        id = dataFromActivity.getLong(FragmentExample.ITEM_ID );

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.detail_menu, container, false);

        //show the message
        TextView titleText = result.findViewById(R.id.title_View);
        TextView latText = result.findViewById(R.id.lat_View);
        TextView lonText = result.findViewById(R.id.lon_View);
        TextView telText = result.findViewById(R.id.telephone_View);
        titleText.setText(dataFromActivity.getString(ListActivity.TITLE));
        latText.setText(dataFromActivity.getString(ListActivity.LAT));
        lonText.setText(dataFromActivity.getString(ListActivity.LON));
        telText.setText(dataFromActivity.getString(ListActivity.TEL));
        return result;
    }
}
