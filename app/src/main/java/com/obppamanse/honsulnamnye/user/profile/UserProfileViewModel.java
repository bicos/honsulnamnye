package com.obppamanse.honsulnamnye.user.profile;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.UploadTask;
import com.obppamanse.honsulnamnye.BR;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.user.model.UserInfo;
import com.obppamanse.honsulnamnye.util.ActivityUtils;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Ravy on 2017. 6. 24..
 */

public class UserProfileViewModel extends BaseObservable {

    private static final String TAG = "UserProfileViewModel";

    private UserProfileContract.View view;

    private UserProfileContract.Model model;

    public UserProfileViewModel(UserProfileContract.View view, UserProfileContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Bindable
    public String getProfileName() {
        return model.getUser() != null ? model.getUser().nickName : null;
    }

    @Bindable
    public String getUserEmail() {
        return model.getUser() != null ? model.getUser().email : null;
    }

    @Bindable
    public String getProfileUrl() {
        return model.getUser() != null ? model.getUser().profileUri : null;
    }

    @Bindable
    public String getGender() {
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
            } else if (resId == R.id.radio_woman) {
                model.getUser().gender = UserInfo.Gender.FEMALE.name();
            }
        }
    }

    public boolean currentUserIsMale() {
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
            model.setModifyProfileImage(!uri.toString().equals(model.getUser().profileUri));
            model.getUser().profileUri = uri.toString();
            notifyPropertyChanged(BR.profileUrl);
        }
    }

    public void clickChangeProfileImage() {
        view.chooseProfileImage();
    }

    public void clickWithdrawal(Context context) {
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

    public void clickModifyProfile(Context context) {
        Activity activity = ActivityUtils.getActivity(context);

        if (activity == null) {
            view.setUiFailedModifyProfile(new UserProfileContract.FailureModifyProfileException());
            return;
        }

        UploadTask uploadTask = null;

        if (model.isModifyProfileImage()) {
            view.showUploadProfileImageProgress();
            uploadTask = model.uploadProfileImage();
        }

        Task<Void> firebaseProfileUploadTask;

        if (uploadTask != null) { // 수정된 이미지가 있을 경우
            firebaseProfileUploadTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Void>>() {
                @Override
                public Task<Void> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    view.hideAllProgress();

                    if (task.isSuccessful()) {
                        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
                        builder.setPhotoUri(task.getResult().getDownloadUrl());

                        if (model.isModifyUserName()) {
                            builder.setDisplayName(model.getUser().nickName);
                        }

                        view.showUpdateFirebaseUserProfileProgress();

                        return model.updateFirebaseUserProfile(builder);
                    } else {
                        throw task.getException();
                    }
                }
            }).addOnFailureListener(activity, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "profile image upload error : ", e);
                    view.hideAllProgress();
                    view.setUiFailedWithdrawalService(e);
                }
            });
        } else { // 수정된 이미지가 없을 경우
            UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
            if (model.isModifyUserName()) {
                builder.setDisplayName(model.getUser().nickName);
            }
            view.showUpdateFirebaseUserProfileProgress();
            firebaseProfileUploadTask = model.updateFirebaseUserProfile(builder);
        }

        if (firebaseProfileUploadTask == null) {
            view.hideAllProgress();
            view.setUiFailedModifyProfile(new UserProfileContract.FailureModifyProfileException());
            return;
        }

        firebaseProfileUploadTask.continueWithTask(new Continuation<Void, Task<Void>>() {
            @Override
            public Task<Void> then(@NonNull Task<Void> task) throws Exception {
                view.hideAllProgress();

                if (task.isSuccessful()) {
                    view.showUpdateUserInfoProgress();
                    return model.updateProfileDatabase();
                } else {
                    throw new UserProfileContract.FailureModifyProfileException();
                }
            }
        }).addOnSuccessListener(activity, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                model.setModifyUserName(false);
                model.setModifyProfileImage(false);
                model.setModifyUserGender(false);

                view.hideAllProgress();
                view.setUiSuccessModifyProfile();
            }
        }).addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.hideAllProgress();
                Log.e(TAG, "database update failed : ", e);
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
