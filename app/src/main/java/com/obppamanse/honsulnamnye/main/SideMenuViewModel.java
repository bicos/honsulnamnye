package com.obppamanse.honsulnamnye.main;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.obppamanse.honsulnamnye.user.UserProfileActivity;
import com.obppamanse.honsulnamnye.user.model.UserInfo;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class SideMenuViewModel extends BaseObservable {

    private SideMenuContract.View view;

    private SideMenuContract.Request request;

    public SideMenuViewModel(SideMenuContract.View view, SideMenuContract.Request request) {
        this.view = view;
        this.request = request;
    }

    public void clickLogoutButton() {
        request.requestLogout();
        view.successLogout();
    }

    public void clickProfileImage(final Context context) {
        request.getUserInfo(new SideMenuContract.RequestUserInfoListener() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                UserProfileActivity.startUserProfileActivity(context, userInfo);
            }

            @Override
            public void onFailed(Exception e) {
                view.failedGetUserInfo(e);
            }
        });
    }

    @BindingAdapter("setProfileImage")
    public static void setProfileImage(ImageView image,@Nullable StorageReference ref) {

        Glide.with(image.getContext())
                .using(new FirebaseImageLoader())
                .load(ref)
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
    public StorageReference getProfileStorageReference() {
        return request.getProfileStorageReference();
    }
}
