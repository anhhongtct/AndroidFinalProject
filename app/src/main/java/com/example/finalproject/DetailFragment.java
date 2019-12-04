package com.example.finalproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;


/**
 * @author: Tran Thi Anh Hong
 * @version 01
 * @date 12/2019
 * This is the detail fragment
 */
public class DetailFragment extends Fragment {

    private boolean isTablet;
    private Bundle dataFromActivity;
    Toolbar tBar;
    Button addBtn;
    private LinearLayout mRoot;

    /**
     * set the boolean <code>true</code>  if run on a tablet
     * @param tablet
     */
    public void setTablet(boolean tablet) { isTablet = tablet; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataFromActivity = getArguments();

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.fragment_detail_charge_station, container, false);

        tBar = result.findViewById(R.id.second_toolbar);
        mRoot = result.findViewById(R.id.main);
        if (tBar != null) {

            ((AppCompatActivity)getActivity()).setSupportActionBar(tBar);
        }

        //show the message
        TextView titleText = result.findViewById(R.id.title_View);
        TextView latText = result.findViewById(R.id.lat_View);
        TextView lonText = result.findViewById(R.id.lon_View);
        TextView telText = result.findViewById(R.id.telephone_View);
        addBtn = result.findViewById(R.id.like_btn);
        Button googleButton = result.findViewById(R.id.google_btn);

        String lat = dataFromActivity.getString(ListActivityChargeStation.LAT);
        String lon = dataFromActivity.getString(ListActivityChargeStation.LON);
        String name = dataFromActivity.getString(ListActivityChargeStation.TITLE);
        String tel = dataFromActivity.getString(ListActivityChargeStation.TEL);
        int index = dataFromActivity.getInt("index");
        if(!dataFromActivity.getBoolean("Boolean")) addBtn.setText("Delete");

        titleText.setText(name);
        latText.setText(lat);
        lonText.setText(lon);
        telText.setText(dataFromActivity.getString(ListActivityChargeStation.TEL));

        googleButton.setOnClickListener(clk -> {
            // Create a Uri from an intent string. Use the result to create an Intent.
            String uri = "geo:0,0?q="+lat+","+lon + "(label)";
            Uri gmmIntentUri = Uri.parse(uri);

            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps");

            // Attempt to start an activity that can handle the Intent
            mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

        });

        if(dataFromActivity.getBoolean("Boolean")) {
           addBtn.setOnClickListener(clk -> {
               if (isTablet) { //both the list and details are on the screen:
                   ListActivityChargeStation parent = (ListActivityChargeStation) getActivity();
                  if (!parent.writeDatabase(lat, lon, name, tel))
                      Toast.makeText(parent, "Already in database", Toast.LENGTH_SHORT).show(); //this deletes the item and updates the list
                   //now remove the fragment since you deleted it from the database:
                   // this is the object to be removed, so remove(this):
                   //parent.getSupportFragmentManager().beginTransaction().remove(DetailFragment.this).commit();
               }
               //for Phone:
               else //You are only looking at the details, you need to go back to the previous list page
               {
                   EmptyChargeStationActivity parent = (EmptyChargeStationActivity) getActivity();
                   Intent backToFragmentExample = new Intent();
                   backToFragmentExample.putExtra("title", name);
                   backToFragmentExample.putExtra("lat", lat);
                   backToFragmentExample.putExtra("lon", lon);
                   backToFragmentExample.putExtra("tel", tel);
                   parent.setResult(51, backToFragmentExample);
                   Snackbar.make(mRoot, "You add to Favorite", Snackbar.LENGTH_LONG)
                           .setAction("Go back to main menu?", e -> parent.startIntent()).show();
                   //send data back to FragmentExample in onActivityResult()
                   //parent.finish(); //go back
               }
           });

       } else {

            addBtn.setOnClickListener(clk -> {
               if (isTablet) { //both the list and details are on the screen:
                   ListActivityChargeStation parent = (ListActivityChargeStation) getActivity();
                   parent.deleteRowId(index); //this deletes the item and updates the list
                   //now remove the fragment since you deleted it from the database:
                   // this is the object to be removed, so remove(this):
                   parent.getSupportFragmentManager().beginTransaction().remove(DetailFragment.this).commit();
               }
               //for Phone:
               else //You are only looking at the details, you need to go back to the previous list page
               {
                   EmptyChargeStationActivity parent = (EmptyChargeStationActivity) getActivity();
                   Intent backToFragmentExample = new Intent();
                   backToFragmentExample.putExtra("index", index);
                   parent.setResult(52, backToFragmentExample);
                   Snackbar.make(mRoot, "Deleted from favorite", Snackbar.LENGTH_LONG)
                           .setAction("Go back to main menu?", e -> parent.startIntent()).show();
                   //send data back to FragmentExample in onActivityResult()
                  // parent.finish(); //go back
               }
           });
       }
        return result;
    }

}
