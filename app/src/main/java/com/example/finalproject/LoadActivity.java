package com.example.finalproject;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LoadActivity extends AppCompatActivity {

    ArrayList<ChargingStation> chargingList = new ArrayList<>();
    BaseAdapter myAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_menu);

        //List View
        ListView theList =findViewById(R.id.theList);
    }

    public class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {return chargingList.size();
        }

        @Override
        public ChargingStation getItem(int position) {
            return chargingList.get(position);
        }

        @Override
        public View getView(int position, View messView, ViewGroup parent) {
            View thisRow = messView;
                thisRow = getLayoutInflater().inflate(R.layout.list, null);
                TextView sendText = thisRow.findViewById(R.id.viewText);
                sendText.setText(String.valueOf(getItem(position).getLatitude()));
            return thisRow;
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }
    }

}
