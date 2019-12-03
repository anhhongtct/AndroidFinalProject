package com.example.finalproject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author: Tran Thi Anh Hong
 * @version 01
 * @date 12/2019
 * This is the base class encapsulate the Charging Sation information
 */
public class ChargingStation implements Comparable<ChargingStation> {

    /**
     *
     */
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
    public ChargingStation(long id, String title, String latitude, String longitude, String telephone) {
        this.id = id;
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

    @Override
    public int compareTo(ChargingStation chargingStation) {
        if (getLatitude().compareTo(chargingStation.getLatitude()) ==0 )
            if (getLongitude().compareTo(chargingStation.getLongitude()) ==0)
                if (getTitle().compareTo(chargingStation.getTitle()) ==0)
                    if(getTelephone().compareTo(chargingStation.getTelephone()) == 0)
                        return 0;

                    return -1;
    }

}

