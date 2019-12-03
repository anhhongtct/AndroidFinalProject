package com.example.finalproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;

public class HistoryAdapter  extends BaseAdapter {
    ArrayList<HistoryItem> historyList = new ArrayList<HistoryItem>();
    public Activity context;

    public HistoryAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (historyList != null) {
            return historyList.size();
        } else {
            return 0;
        }
    }

    public HistoryItem getItem(int position) {
        if (historyList != null) {
            return historyList.get(position);
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
        HistoryAdapter.ViewHolder holder = null;
        HistoryItem rowItem = (HistoryItem)getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        convertView = mInflater.inflate(R.layout.history_item, null);
        holder = new HistoryAdapter.ViewHolder();
        holder.historyDate = (TextView) convertView.findViewById(R.id.history_date);
        holder.historyAmount = (TextView) convertView.findViewById(R.id.history_currency);

        convertView.setTag(holder);

        holder.historyDate.setText(rowItem.getDate());
        holder.historyAmount.setText(Double.toString(rowItem.getAmount()));

        return convertView;
    }

    public void add(HistoryItem msg) {
        historyList.add(msg);
    }

    public ArrayList<HistoryItem> getAll() {
        return historyList;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView historyDate;
        TextView historyAmount;
    }

}

class SortbyDate implements Comparator<HistoryItem>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(HistoryItem a, HistoryItem b)
    {
        return a.getDate().compareTo(b.getDate());
    }
}
