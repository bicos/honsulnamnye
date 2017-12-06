package com.obppamanse.honsulnamnye.user.signup;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.user.model.UserInfo;

/**
 * Created by raehyeong.park on 2017. 4. 28..
 */

public class SignUpModel implements SignUpContract.Model {

    private FirebaseUser firebaseUser;

    private UserInfo userInfo;

    private StorageReference profileStorage;

    private Uri localUri;

    public SignUpModel() {
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        this.profileStorage = FirebaseUtils.getProfileStorageRef();
        this.userInfo = new UserInfo(firebaseUser);
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
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        requestUpdateOptionalData(activity, listener);
                    } else {
                        listener.onFailed(task.getException());
                    }
                });
    }

    private void requestUpdateOptionalData(Activity activity, final SignUpContract.SignUpCompleteListener listener) {
        FirebaseUtils.getUserRef().child(firebaseUser.getUid()).setValue(userInfo)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                    } else {
                        listener.onFailed(task.getException());
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
        userInfo.gender = gender.name();
    }

    @Override
    public void setProfileUri(Uri profileUri) {
        if (profileUri != null) {
            localUri = profileUri;
        }
    }

    @Override
    public String getProfileUri() {
        return localUri != null ? localUri.toString() : null;
    }

    @Override
    public void setProfileImageUrl(Uri downloadUrl) {
        if (downloadUrl != null) {
            userInfo.profileUri = downloadUrl.toString();
        }
    }

    @Override
    public void uploadProfile(Uri imageUri, Activity activity, OnCompleteListener<UploadTask.TaskSnapshot> completeListener) {
        profileStorage
                .child(userInfo.email)
                .putFile(imageUri)
                .addOnCompleteListener(activity, completeListener);
    }
}
