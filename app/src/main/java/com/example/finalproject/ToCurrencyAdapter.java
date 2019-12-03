package com.example.finalproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ToCurrencyAdapter extends BaseAdapter {
    private ArrayList<ToCurrencyItem> currencyList = new ArrayList<ToCurrencyItem>();
    private Activity context;
    private String selectedName;

    ToCurrencyAdapter(Activity context) {
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

    public ToCurrencyItem getItem(int position) {
        if (currencyList != null) {
            return currencyList.get(position);
        } else {
            return null;
        }
    }

    public ToCurrencyItem getItem(String name) {
        for (ToCurrencyItem m : currencyList) {
            if(m.getName().equals(name)) {
                return m;
            }
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ToCurrencyAdapter.ViewHolder holder = null;
        final ToCurrencyItem rowItem = (ToCurrencyItem)getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        convertView = mInflater.inflate(R.layout.tocurrency_item, null);
        holder = new ToCurrencyAdapter.ViewHolder();
        holder.currencyName = (TextView) convertView.findViewById(R.id.currency_name);
        holder.currencyNow = (TextView) convertView.findViewById(R.id.currency_amount);
        holder.currencyHistory = (ImageButton) convertView.findViewById(R.id.currency_history);
        holder.currencyRemove = (ImageButton) convertView.findViewById(R.id.currency_remove);

        convertView.setTag(holder);


        DecimalFormat df = new DecimalFormat("###.####");
        selectedName = rowItem.getName();
        holder.currencyName.setText(selectedName);
        holder.currencyNow.setText(df.format(rowItem.getAmount()));
        holder.currencyHistory.setImageResource(R.drawable.history);
        holder.currencyRemove.setImageResource(R.drawable.remove);

        holder.currencyHistory.setFocusable(false);
        holder.currencyHistory.setFocusableInTouchMode(false);

        holder.currencyRemove.setFocusable(false);
        holder.currencyRemove.setFocusableInTouchMode(false);

        //history button click event on the listview item
        holder.currencyHistory.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((ListView) parent).performItemClick(v, position, 0);
            }

        });

        //remove button click event on the listview item
        holder.currencyRemove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((ListView) parent).performItemClick(v, position, 0);
            }

        });

        return convertView;
    }

    public void add(ToCurrencyItem msg) {
        currencyList.add(msg);
    }

    public void remove(long id) {
        for (ToCurrencyItem m : currencyList) {
            if(m.getId() == id) {
                currencyList.remove(m);
                break;
            }
        }
    }

    public void remove(String name) {
        for (ToCurrencyItem m : currencyList) {
            if(m.getName().equals(name)) {
                currencyList.remove(m);
                break;
            }
        }
    }

    public ArrayList<ToCurrencyItem> getAll() {
        return currencyList;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView currencyName;
        TextView currencyNow;
        ImageButton currencyHistory;
        ImageButton currencyRemove;
    }

}
