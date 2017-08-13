package com.obppamanse.honsulnamnye.user.profile;

import android.app.Activity;
import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.UploadTask;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
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
    public UploadTask uploadProfileImage() {
        return FirebaseUtils.getProfileStorageRef().child(user.email)
                .putFile(Uri.parse(user.profileUri));
    }

    @Override
    public Task<Void> updateFirebaseUserProfile(UserProfileChangeRequest.Builder builder) {
        return firebaseUser.updateProfile(builder.build());
    }

    @Override
    public Task<Void> updateProfileDatabase() {
        return FirebaseUtils.getUserRef().child(firebaseUser.getUid()).setValue(user);
    }

    @Override
    public void withdrawalService(Activity activity, OnCompleteListener<Void> listener) {
        firebaseUser.delete().addOnCompleteListener(activity, listener);
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
