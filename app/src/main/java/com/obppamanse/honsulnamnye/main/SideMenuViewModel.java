package com.obppamanse.honsulnamnye.main;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.obppamanse.honsulnamnye.user.model.UserInfo;
import com.obppamanse.honsulnamnye.user.profile.UserProfileActivity;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class SideMenuViewModel extends BaseObservable {

    private SideMenuContract.View view;

    private SideMenuContract.Request request;

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                request.setUserInfo(dataSnapshot.getValue(UserInfo.class));
                notifyChange();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public SideMenuViewModel(SideMenuContract.View view, SideMenuContract.Request request) {
        this.view = view;
        this.request = request;
    }

    public void startSyncUserInfo() {
        request.startSyncUserInfo(listener);
    }

    public void stopSyncUserInfo(){
        request.stopSyncUserInfo(listener);
    }

    public void clickLogoutButton() {
        request.requestLogout();
        view.successLogout();
    }

    public void clickProfileImage(final Context context) {
        if (request.getCurrentUser() != null) {
            UserProfileActivity.startUserProfileActivity(context, request.getCurrentUser());
        } else {
            view.failedGetUserInfo(new SideMenuContract.FailedGetUserInfo());
        }
    }

    @BindingAdapter("setProfileImage")
    public static void setProfileImage(ImageView image, String profileUrl) {
        Glide.with(image.getContext())
                .load(profileUrl)
                .bitmapTransform(new CropCircleTransformation(image.getContext()))
                .into(image);
    }

    @Bindable
    public String getUserName() {
        return request.getUserName();
    }

    @Bindable
    public String getUserEmail() {
        return request.getUserEmail();
    }

    @Bindable
    public String getProfileUrl() {
        return request.getProfileUrl();
    }
}
