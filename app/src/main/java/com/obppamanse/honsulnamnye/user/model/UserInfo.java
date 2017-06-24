package com.obppamanse.honsulnamnye.user.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by raehyeong.park on 2017. 4. 28..
 */

public class UserInfo implements Parcelable {

    public String email;

    public String password;

    public String nickName;

    public Gender gender;

    public String profileUri;

    public String interestingCategory;

    public UserInfo() {
    }

    public UserInfo(FirebaseUser firebaseUser) {
        email = firebaseUser.getEmail();
        nickName = firebaseUser.getDisplayName();
        profileUri = firebaseUser.getPhotoUrl() == null ? null : firebaseUser.getPhotoUrl().toString();
    }

    public enum Gender {
        MALE, FEMALE
    }

    protected UserInfo(Parcel in) {
        email = in.readString();
        password = in.readString();
        nickName = in.readString();
        profileUri = in.readString();
        interestingCategory = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(nickName);
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
}
