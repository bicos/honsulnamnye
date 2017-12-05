package com.obppamanse.honsulnamnye.main.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by raehyeong.park on 2017. 11. 30..
 */

public class Category implements Parcelable {

    public static final String ALL = "C00";

    private String code;
    private String name;
    private int index;

    public Category() {
    }

    protected Category(Parcel in) {
        code = in.readString();
        name = in.readString();
        index = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
        dest.writeInt(index);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
