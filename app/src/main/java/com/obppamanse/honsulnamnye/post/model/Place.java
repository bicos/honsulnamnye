package com.obppamanse.honsulnamnye.post.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class Place implements Parcelable {

    private String name;
    private double lat;
    private double lon;

    public Place() {
    }

    public Place(String name, double lat, double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }


    protected Place(Parcel in) {
        name = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
