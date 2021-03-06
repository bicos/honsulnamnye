package com.obppamanse.honsulnamnye.user.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by raehyeong.park on 2017. 4. 28..
 */

@IgnoreExtraProperties
public class UserInfo implements Parcelable {

    public String uid;

    public String email;

    @Exclude
    public String password;

    public String nickName;

    public String gender;

    public String profileUri = "https://firebasestorage.googleapis.com/v0/b/honsulnamnyeo.appspot.com/o/profile%2Fprofile_blank.png?alt=media&token=306850ed-d06d-4174-a157-2288f06ee7a7";

    public String interestingCategory;

    public UserInfo() {
    }

    public UserInfo(FirebaseUser firebaseUser) {
        uid = firebaseUser.getUid();
        email = firebaseUser.getEmail();
        nickName = firebaseUser.getDisplayName();
        profileUri = firebaseUser.getPhotoUrl() == null ? null : firebaseUser.getPhotoUrl().toString();
    }

    public enum Gender {
        MALE, FEMALE
    }

    protected UserInfo(Parcel in) {
        uid = in.readString();
        email = in.readString();
        password = in.readString();
        nickName = in.readString();
        gender = in.readString();
        profileUri = in.readString();
        interestingCategory = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(nickName);
        dest.writeString(gender);
        dest.writeString(profileUri);
        dest.writeString(interestingCategory);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("email", email);
        result.put("nickName", nickName);
        result.put("gender", gender);
        result.put("profileUri", profileUri);
        result.put("interestingCategory", interestingCategory);
        return result;
    }
}
