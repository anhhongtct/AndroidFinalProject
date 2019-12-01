package com.example.finalproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;

public class CurrencyAdapter  extends BaseAdapter {
    ArrayList<CurrencyItem> currencyList = new ArrayList<CurrencyItem>();
    public Activity context;

    public CurrencyAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (currencyList != null) {
            return currencyList.size();
        } else {
            return 0;
        }
    }

    public CurrencyItem getItem(int position) {
        if (currencyList != null) {
            return currencyList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        CurrencyItem rowItem = (CurrencyItem)getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        convertView = mInflater.inflate(R.layout.currency_item, null);
        holder = new ViewHolder();
        holder.currencyName = (TextView) convertView.findViewById(R.id.currencyName);
        holder.currencyNow = (TextView) convertView.findViewById(R.id.currencyNow);

        convertView.setTag(holder);


        holder.currencyName.setText(rowItem.getName());
        holder.currencyNow.setText(Double.toString(rowItem.getAmount()));

        return convertView;
    }

    public void add(CurrencyItem msg) {
        currencyList.add(msg);
    }

    public ArrayList<CurrencyItem> getAll() {
        return currencyList;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView currencyName;
        TextView currencyNow;
    }
}

class SortbyName implements Comparator<CurrencyItem>
{
    // Used for sorting in ascending order of
    // name
    public int compare(CurrencyItem a, CurrencyItem b)
    {
        return a.getName().compareTo(b.getName());
    }
}

