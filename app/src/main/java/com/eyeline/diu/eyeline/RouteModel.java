package com.eyeline.diu.eyeline;

public class RouteModel {
    private String Instruction;
    private double lat;
    private double lng;

    public RouteModel(String instruction, double lat, double lng) {
        Instruction = instruction;
        this.lat = lat;
        this.lng = lng;
    }

    public String getInstruction() {
        return Instruction;
    }

    public void setInstruction(String instruction) {
        Instruction = instruction;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "RouteModel{" +
                "Instruction='" + Instruction + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
