package com.obppamanse.honsulnamnye.user.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by raehyeong.park on 2017. 4. 28..
 */

public class UserInfo implements Parcelable {

    public String email;

    public String password;

    public String nickName;

    public String gender;

    public String interestingCategory;

    protected UserInfo(Parcel in) {
        email = in.readString();
        password = in.readString();
        nickName = in.readString();
        gender = in.readString();
        interestingCategory = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(nickName);
        dest.writeString(gender);
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
