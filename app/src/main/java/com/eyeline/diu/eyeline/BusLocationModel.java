package com.eyeline.diu.eyeline;

public class BusLocationModel {
    private String id,latitude ,Longitude;

    public BusLocationModel(String id, String latitude, String longitude) {
        this.id = id;
        this.latitude = latitude;
        Longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }
}
