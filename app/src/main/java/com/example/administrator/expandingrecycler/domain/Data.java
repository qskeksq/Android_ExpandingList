package com.example.administrator.expandingrecycler.domain;


public class Data {
    private GeoInfoPublicToiletWGS GeoInfoPublicToiletWGS;

    public GeoInfoPublicToiletWGS getGeoInfoPublicToiletWGS () {
        return GeoInfoPublicToiletWGS;
    }

    public void setGeoInfoPublicToiletWGS (GeoInfoPublicToiletWGS GeoInfoPublicToiletWGS) {
        this.GeoInfoPublicToiletWGS = GeoInfoPublicToiletWGS;
    }

    @Override
    public String toString() {
        return "ClassPojo [GeoInfoPublicToiletWGS = "+GeoInfoPublicToiletWGS+"]";
    }
}