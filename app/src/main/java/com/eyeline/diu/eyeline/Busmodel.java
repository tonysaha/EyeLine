package com.eyeline.diu.eyeline;

public class Busmodel {
    private String busId;
    private String busName;
    private String destLat;
    private String desLng;

    public Busmodel(String busId, String busName, String destLat, String desLng) {

        this.busId = busId;
        this.busName = busName;
        this.destLat = destLat;
        this.desLng = desLng;
    }

    public Busmodel() {
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getDestLat() {
        return destLat;
    }

    public void setDestLat(String destLat) {
        this.destLat = destLat;
    }

    public String getDesLng() {
        return desLng;
    }

    public void setDesLng(String desLng) {
        this.desLng = desLng;
    }

    @Override
    public String toString() {
        return "Busmodel{" +
                "busId='" + busId + '\'' +
                ", busName='" + busName + '\'' +
                ", destLat='" + destLat + '\'' +
                ", desLng='" + desLng + '\'' +
                '}';
    }

}
