package com.obppamanse.honsulnamnye.main;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

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

    @BindingAdapter("setProfileImage")
    public static void setProfileImage(ImageView image,@Nullable StorageReference ref) {

        Glide.with(image.getContext())
                .using(new FirebaseImageLoader())
                .load(ref)
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
