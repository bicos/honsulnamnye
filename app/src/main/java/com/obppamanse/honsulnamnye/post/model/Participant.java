package com.obppamanse.honsulnamnye.post.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
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

    private String profileUrl;

    private String userName;

    public Participant() {
    }

    public Participant(String uid, String profileUrl, String userName) {
        this.uid = uid;
        this.profileUrl = profileUrl;
        this.userName = userName;
    }

    protected Participant(Parcel in) {
        uid = in.readString();
        profileUrl = in.readString();
        userName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(profileUrl);
        dest.writeString(userName);
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

    @Bindable
    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        map.put("profileUrl", profileUrl);
        map.put("userName", userName);
        return map;
    }
}
