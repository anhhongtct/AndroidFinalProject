package com.example.finalproject;

public class ChargingStation {

    long id;
    String title;
    String latitude;
    String longitude;
    String telephone;
    public ChargingStation(String title, String latitude, String longitude, String telephone) {
      this.title = title;
      this.latitude = latitude;
      this.longitude = longitude;
      this.telephone = telephone;
    }
    public long getId() {return id; }

    public String getTitle() {
        return this.title;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }
    public String getTelephone() {
        return this.telephone;
    }
}
