package com.obppamanse.honsulnamnye.user.profile;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.obppamanse.honsulnamnye.BR;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.user.model.UserInfo;
import com.obppamanse.honsulnamnye.util.ActivityUtils;

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
    public String getGender(){
        return model.getUser() != null ? model.getUser().gender : null;
    }

    public void inputNickName(String nickName) {
        if (nickName != null) {
            model.setModifyUserName(!model.getUser().nickName.equals(nickName));
            if (model.getUser() != null) {
                model.getUser().nickName = nickName;
            }
        }
    }

    public void changeGender(int resId) {
        if (model.getUser() != null) {
            model.setModifyUserGender(!getGenderFromResId(resId).equals(model.getUser().gender));
            if (resId == R.id.radio_man) {
                model.getUser().gender = UserInfo.Gender.MALE.name();
            } else if (resId == R.id.radio_woman){
                model.getUser().gender = UserInfo.Gender.FEMALE.name();
            }
        }
    }

    public boolean currentUserIsMale(){
        Log.i("test", "getUser.gender : " + model.getUser().gender +", UserInfo.Gender.MALE.name() : "+UserInfo.Gender.MALE.name());
        return model.getUser().gender != null && model.getUser().gender.equals(UserInfo.Gender.MALE.name());
    }

    private String getGenderFromResId(int resId) {
        if (resId == R.id.radio_man) {
            return UserInfo.Gender.MALE.name();
        } else {
            return UserInfo.Gender.FEMALE.name();
        }
    }

    public void changeProfileImage(Uri uri) {
        if (uri != null) {
            model.setModifyProfileImage(!model.getUser().profileUri.equals(uri.toString()));
            model.getUser().profileUri = uri.toString();
            notifyPropertyChanged(BR.profileUrl);
        }
    }

    public void clickChangeProfileImage() {

        view.chooseProfileImage();
    }

    public void clickWithdrawal(Context context){
        model.withdrawalService(ActivityUtils.getActivity(context), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    view.setUiSuccessWithdrawalService();
                } else {
                    view.setUiFailedWithdrawalService(task.getException());
                }
            }
        });
    }

    public void clickModifyProfile(Context context){
        model.updateProfile(ActivityUtils.getActivity(context), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                view.setUiSuccessModifyProfile();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.setUiFailedModifyProfile(e);
            }
        });
    }

    @BindingAdapter("setProfileImage")
    public static void setProfileImage(ImageView image, String userProfileUrl) {
        Glide.with(image.getContext())
                .load(userProfileUrl)
                .bitmapTransform(new CropCircleTransformation(image.getContext()))
                .into(image);
    }

    public boolean isModifyUserInfo() {
        return model.isModifyUserGender() || model.isModifyProfileImage() || model.isModifyUserName();
    }

    public void initChangeFlag() {
        model.setModifyProfileImage(false);
        model.setModifyUserGender(false);
        model.setModifyUserName(false);
    }
}
