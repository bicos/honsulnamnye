package com.obppamanse.honsulnamnye.post.model;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class Location {

    private String name;
    private long lat;
    private long lon;

    public Location() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLon() {
        return lon;
    }

    public void setLon(long lon) {
        this.lon = lon;
    }
}
