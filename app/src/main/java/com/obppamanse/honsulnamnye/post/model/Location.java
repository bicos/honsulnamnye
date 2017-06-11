package com.obppamanse.honsulnamnye.post.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class Location implements Parcelable{

    private String name;
    private long lat;
    private long lon;

    public Location() {
    }

    protected Location(Parcel in) {
        name = in.readString();
        lat = in.readLong();
        lon = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(lat);
        dest.writeLong(lon);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

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
