package com.example.finalproject;

public class ToCurrencyItem {
    boolean isFrom;
    String  name;
    double  amount;
    long    id;

    public ToCurrencyItem(String name, double amount, boolean isFrom, long id) {
        this.name = name;
        this.amount = amount;
        this.isFrom = isFrom;
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public boolean isFrom() {
        return isFrom;
    }
    public long getId() {
        return id;
    }
}
