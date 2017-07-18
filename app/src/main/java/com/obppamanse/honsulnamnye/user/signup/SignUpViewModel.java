package com.obppamanse.honsulnamnye.user.signup;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;
import com.obppamanse.honsulnamnye.BR;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.user.model.UserInfo;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by raehyeong.park on 2017. 4. 28..
 */

public class SignUpViewModel extends BaseObservable implements SignUpContract.SignUpCompleteListener {

    private SignUpContract.View view;

    private SignUpContract.Model model;

    public SignUpViewModel(SignUpContract.View view, SignUpContract.Model model) {
        this.view = view;
        this.model = model;
    }

    public void inputNickName(CharSequence nickName){
        model.setNickName(nickName);
    }

    public void changeGender(int checkedId) {
        if (checkedId == R.id.radio_man) {
            model.setGender(UserInfo.Gender.MALE);
        } else {
            model.setGender(UserInfo.Gender.FEMALE);
        }
    }

    @Bindable
    public String getProfileName(){
        return model.getProfileName();
    }

    @Bindable
    public String getProfileUri(){
        return model.getProfileUri();
    }

    @BindingAdapter("loadImage")
    public static void loadImage(ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(imageView.getContext())
                    .load(url)
                    .bitmapTransform(new CropCircleTransformation(imageView.getContext()))
                    .into(imageView);
        } else {
            Glide.with(imageView.getContext())
                    .load(R.drawable.profile_blank)
                    .bitmapTransform(new CropCircleTransformation(imageView.getContext()))
                    .into(imageView);
        }
    }

    public void clickSignUp() {

        if (TextUtils.isEmpty(model.getProfileName())) {
            view.showException(new SignUpContract.NickNameIsEmptyException());
            return;
        }

        model.requestSignUp(view.getActivity(), this);
    }

    public void clickProfileImage() {
        view.chooseProfileImage();
    }

    @Override
    public void onSuccess() {
        view.startMainActivity();
    }

    @Override
    public void onFailed(Exception e) {
        view.showException(e);
    }

    public void setProfileImage(final Uri profileImage) {
        if (profileImage != null) {
            model.uploadProfile(profileImage, view.getActivity(), new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        model.setProfileUri(profileImage);
                        model.setProfileImageUrl(task.getResult().getDownloadUrl());
                        notifyPropertyChanged(BR.profileUri);
                    } else {
                        view.showException(task.getException());
                    }
                }
            });

        }
    }
}
