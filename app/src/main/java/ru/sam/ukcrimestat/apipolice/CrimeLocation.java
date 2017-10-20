package ru.sam.ukcrimestat.apipolice;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sam on 13.10.17.
 */

public class CrimeLocation {
    private double latitude;
    private double longitude;
    private String date;
    private Map<String,String> street;

    public CrimeLocation() {
        street = new HashMap<>();
        street.put("id","");
        street.put("name","");

    }

    @Override
    public String toString() {
        return "CrimeLocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", date='" + date + '\'' +
                ", street=" + street +
                '}';
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, String> getStreet() {
        return street;
    }

    public void setStreet(Map<String, String> street) {
        this.street = street;
    }
}
