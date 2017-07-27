package com.obppamanse.honsulnamnye.user.profile;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    }

    public interface Model {
        UserInfo getUser();

        void updateProfile(Activity activity,
                           OnSuccessListener<Void> successListener,
                           OnFailureListener failureListener);

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
