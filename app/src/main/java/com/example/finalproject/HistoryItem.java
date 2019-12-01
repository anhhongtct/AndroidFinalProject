package com.example.finalproject;

public class HistoryItem {
    String  date;
    double  amount;

    public HistoryItem(String date, double amount) {
        this.date = date;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }
}
