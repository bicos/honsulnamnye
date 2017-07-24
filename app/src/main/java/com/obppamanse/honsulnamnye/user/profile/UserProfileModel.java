package com.obppamanse.honsulnamnye.user.profile;

import android.app.Activity;
import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    public void updateProfile(final Activity activity,
                              final OnSuccessListener<Void> successListener,
                              final OnFailureListener failureListener) {
        final UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();

        if (modifyProfileImage) {
            modifyProfileImage = false;

            FirebaseUtils.getProfileStorageRef().child(user.email)
                    .putFile(Uri.parse(user.profileUri))
                    .addOnSuccessListener(activity, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot.getDownloadUrl() != null) {
                                builder.setPhotoUri(taskSnapshot.getDownloadUrl());
                                user.profileUri = taskSnapshot.getDownloadUrl().toString();
                                updateProfile(builder, activity, successListener, failureListener);
                            } else {
                                failureListener.onFailure(new UserProfileContract.FailureModifyProfileException());
                            }
                        }
                    })
                    .addOnFailureListener(activity, failureListener);
        } else {
            updateProfile(builder, activity, successListener, failureListener);
        }
    }

    private void updateProfile(UserProfileChangeRequest.Builder builder, final Activity activity,
                               final OnSuccessListener<Void> successListener,
                               final OnFailureListener failureListener) {

        if (modifyUserName) {
            modifyUserName = false;
            builder.setDisplayName(user.nickName);
        }

        firebaseUser.updateProfile(builder.build()).addOnSuccessListener(activity, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (modifyUserGender) {
                    modifyUserGender = false;
                    FirebaseUtils.getUserRef().child(firebaseUser.getUid()).setValue(user)
                            .addOnSuccessListener(activity, successListener)
                            .addOnFailureListener(activity, failureListener);
                } else {
                    successListener.onSuccess(aVoid);
                }
            }
        }).addOnFailureListener(activity, failureListener);
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
