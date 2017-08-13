package com.obppamanse.honsulnamnye.user.profile;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.UploadTask;
import com.obppamanse.honsulnamnye.user.model.UserInfo;

/**
 * Created by Ravy on 2017. 6. 24..
 */

public class UserProfileContract {

    public interface View {
        void chooseProfileImage();

        void setUiSuccessWithdrawalService();

        void setUiFailedWithdrawalService(Exception exception);

        void setUiSuccessModifyProfile();

        void setUiFailedModifyProfile(Exception e);

        void showUploadProfileImageProgress();

        void showUpdateFirebaseUserProfileProgress();

        void hideAllProgress();

        void showUpdateUserInfoProgress();
    }

    public interface Model {
        UserInfo getUser();

        UploadTask uploadProfileImage();

        Task<Void> updateFirebaseUserProfile(UserProfileChangeRequest.Builder builder);

        Task<Void> updateProfileDatabase();

        void withdrawalService(Activity activity, OnCompleteListener<Void> listener);

        boolean isModifyProfileImage();

        void setModifyProfileImage(boolean modifyProfileImage);

        boolean isModifyUserName();

        void setModifyUserName(boolean modifyUserName);

        boolean isModifyUserGender();

        void setModifyUserGender(boolean modifyUserGender);
    }

    public static class FailureModifyProfileException extends Exception {
        @Override
        public String getMessage() {
            return "유저 정보 수정 중 에러가 발생하였습니다.";
        }
    }
}
