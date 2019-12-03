package com.example.finalproject;

import org.json.JSONException;
import org.json.JSONObject;

public class ChargingStation {

    long id;
    String title;
    String latitude;
    String longitude;
    String telephone;

    public ChargingStation(JSONObject object) {
        try {
            this.title = object.getString("Title");
            this.latitude = object.getString("Latitude");
            this.longitude = object.getString("Longitude");
            this.telephone = object.getString("ContactTelephone1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
