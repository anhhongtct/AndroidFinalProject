package com.example.finalproject;

public class CurrencyItem {
    String  name;
    double  amount;

    public CurrencyItem(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }
}
