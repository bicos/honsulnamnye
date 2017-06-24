package com.obppamanse.honsulnamnye.user;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.user.model.UserInfo;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Ravy on 2017. 6. 24..
 */

public class UserProfileViewModel extends BaseObservable {

    private UserProfileContract.View view;

    private UserProfileContract.Model model;

    public UserProfileViewModel(UserProfileContract.View view, UserProfileContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Bindable
    public String getProfileName(){
        return model.getUser() != null ? model.getUser().nickName : null;
    }

    @Bindable
    public String getUserEmail(){
        return model.getUser() != null ? model.getUser().email : null;
    }

    @Bindable
    public String getProfileUrl(){
        return  model.getUser() != null ? model.getUser().profileUri : null;
    }

    @Bindable
    public UserInfo.Gender getGender(){
        return model.getUser() != null ? model.getUser().gender : null;
    }

    public void inputNickName(String nickName) {
        if (model.getUser() != null) {
            model.getUser().nickName = nickName;
        }
    }

    public void changeGender(int resId) {
        if (model.getUser() != null) {
            if (resId == R.id.radio_man) {
                model.getUser().gender = UserInfo.Gender.MALE;
            } else if (resId == R.id.radio_woman){
                model.getUser().gender = UserInfo.Gender.FEMALE;
            }
        }
    }

    public void clickChangeProfileImage(Context context) {

    }

    public void clickWithdrawalButton(Context context){

    }

    @BindingAdapter("setProfileImage")
    public static void setProfileImage(ImageView image, String userProfileUrl) {
        Glide.with(image.getContext())
                .load(userProfileUrl)
                .bitmapTransform(new CropCircleTransformation(image.getContext()))
                .into(image);
    }
}
