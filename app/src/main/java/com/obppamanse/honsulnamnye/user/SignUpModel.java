package com.obppamanse.honsulnamnye.user;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.user.model.UserInfo;

/**
 * Created by raehyeong.park on 2017. 4. 28..
 */

public class SignUpModel implements SignUpContract.Model {

    private FirebaseUser firebaseUser;

    private UserInfo userInfo;

    public SignUpModel(SignUpContract.SignUpCompleteListener listener) {
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        this.userInfo = new UserInfo(firebaseUser);

        if (listener != null && firebaseUser == null) {
            listener.onFailed(new SignUpContract.NotSignedUpException());
        }
    }

    @Override
    public void requestSignUp(final Activity activity, final SignUpContract.SignUpCompleteListener listener) {
        if (TextUtils.isEmpty(userInfo.nickName)) {
            listener.onFailed(new SignUpContract.NotSignedUpException());
            return;
        }

        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder()
                .setDisplayName(userInfo.nickName);

        if (!TextUtils.isEmpty(userInfo.profileUri)) { // 필수정보는 아님
            builder.setPhotoUri(Uri.parse(userInfo.profileUri));
        }

        firebaseUser.updateProfile(builder.build())
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            requestUpdateOptionalData(activity, listener);
                        } else {
                            listener.onFailed(task.getException());
                        }
                    }
                });
    }

    private void requestUpdateOptionalData(Activity activity, final SignUpContract.SignUpCompleteListener listener) {
        FirebaseUtils.getUserRef().child(firebaseUser.getUid()).push()
                .setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onSuccess();
                } else {
                    listener.onFailed(task.getException());
                }
            }
        });
    }

    @Override
    public void setNickName(CharSequence nickName) {
        if (!TextUtils.isEmpty(nickName)) {
            userInfo.nickName = nickName.toString();
        }
    }

    @Override
    public String getProfileName() {
        return userInfo.nickName;
    }

    @Override
    public void setGender(UserInfo.Gender gender) {
        userInfo.gender = gender;
    }

    @Override
    public void setProfileUri(String profileUri) {
        if (!TextUtils.isEmpty(profileUri)) {
            userInfo.profileUri = profileUri;
        }
    }

    @Override
    public String getProfileUri() {
        return userInfo.profileUri;
    }
}
