package com.obppamanse.honsulnamnye.user.model;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by raehyeong.park on 2017. 4. 28..
 */

public class UserInfo {

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
}
