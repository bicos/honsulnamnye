package com.obppamanse.honsulnamnye.user.profile;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.obppamanse.honsulnamnye.BR;
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
        if (nickName != null) {
            model.setModifyUserName(true);
            if (model.getUser() != null) {
                model.getUser().nickName = nickName;
            }
        }
    }

    public void changeGender(int resId) {
        if (model.getUser() != null) {
            model.setModifyUserGender(true);
            if (resId == R.id.radio_man) {
                model.getUser().gender = UserInfo.Gender.MALE;
            } else if (resId == R.id.radio_woman){
                model.getUser().gender = UserInfo.Gender.FEMALE;
            }
        }
    }

    public void changeProfileImage(Uri uri) {
        model.setModifyProfileImage(true);
        if (uri != null) {
            model.getUser().profileUri = uri.toString();
        }
        notifyPropertyChanged(BR.profileUrl);
    }

    public void clickChangeProfileImage() {
        view.chooseProfileImage();
    }

    public void clickWithdrawal(Context context){

    }

    public void clickModifyProfile(Context context){
        model.updateProfile();
    }

    @BindingAdapter("setProfileImage")
    public static void setProfileImage(ImageView image, String userProfileUrl) {
        Glide.with(image.getContext())
                .load(userProfileUrl)
                .bitmapTransform(new CropCircleTransformation(image.getContext()))
                .into(image);
    }
}
