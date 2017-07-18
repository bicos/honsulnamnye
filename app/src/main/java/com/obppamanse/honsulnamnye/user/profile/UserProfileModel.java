package com.obppamanse.honsulnamnye.user.profile;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.obppamanse.honsulnamnye.user.model.UserInfo;

/**
 * Created by Ravy on 2017. 6. 24..
 */

public class UserProfileModel implements UserProfileContract.Model {

    private UserInfo user;

    private FirebaseUser firebaseUser;

    private boolean modifyProfileImage = false;

    private boolean modifyUserName = false;

    private boolean modifyUserGender = false;

    public UserProfileModel(UserInfo user, FirebaseUser firebaseUser) {
        this.user = user;
        this.firebaseUser = firebaseUser;
    }

    @Override
    public UserInfo getUser() {
        return user;
    }

    @Override
    public void updateProfile() {
        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();

        if (modifyProfileImage) {
            builder.setPhotoUri(Uri.parse(user.profileUri));
        }
    }

    @Override
    public void modifyUserGender() {

    }

    @Override
    public void withdrawalService() {

    }

    @Override
    public boolean isModifyProfileImage() {
        return modifyProfileImage;
    }

    @Override
    public void setModifyProfileImage(boolean modifyProfileImage) {
        this.modifyProfileImage = modifyProfileImage;
    }

    @Override
    public boolean isModifyUserName() {
        return modifyUserName;
    }

    @Override
    public void setModifyUserName(boolean modifyUserName) {
        this.modifyUserName = modifyUserName;
    }

    @Override
    public boolean isModifyUserGender() {
        return modifyUserGender;
    }

    @Override
    public void setModifyUserGender(boolean modifyUserGender) {
        this.modifyUserGender = modifyUserGender;
    }
}
