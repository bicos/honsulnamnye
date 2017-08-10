package com.obppamanse.honsulnamnye.post.model;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Ravy on 2017. 6. 18..
 */

@IgnoreExtraProperties
public class Participant extends BaseObservable implements Parcelable {

    private String uid;

    public Participant() {
    }

    public Participant(String uid) {
        this.uid = uid;
    }

    protected Participant(Parcel in) {
        uid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Participant> CREATOR = new Creator<Participant>() {
        @Override
        public Participant createFromParcel(Parcel in) {
            return new Participant(in);
        }

        @Override
        public Participant[] newArray(int size) {
            return new Participant[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Exclude
    @BindingAdapter("setProfileImage")
    public static void setProfileImage(ImageView image, String profileUrl) {
        Glide.with(image.getContext())
                .load(profileUrl)
                .bitmapTransform(new CropCircleTransformation(image.getContext()))
                .into(image);
    }

    @Exclude
    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        return map;
    }
}
