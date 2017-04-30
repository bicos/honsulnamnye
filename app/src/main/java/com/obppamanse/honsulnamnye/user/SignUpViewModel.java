package com.obppamanse.honsulnamnye.user;

import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;

import com.android.databinding.library.baseAdapters.BR;
import com.bumptech.glide.Glide;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.user.model.UserInfo;

/**
 * Created by raehyeong.park on 2017. 4. 28..
 */

public class SignUpViewModel extends BaseObservable implements SignUpContract.SignUpCompleteListener {

    private SignUpContract.View view;

    private SignUpContract.Model model;

    public SignUpViewModel(SignUpContract.View view) {
        this.view = view;
        this.model = new SignUpModel(this);
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

    @BindingAdapter("bind:loadImage")
    public static void loadImage(ImageView imageView, String url) {
        if (url != null) {
            Glide.with(imageView.getContext()).load(url).into(imageView);
        }
    }

    public void clickSignUp() {
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

    public void setProfileImage(Uri profileImage) {
        model.setProfileUri(profileImage.toString());
        notifyPropertyChanged(BR.profileUri);
    }
}
