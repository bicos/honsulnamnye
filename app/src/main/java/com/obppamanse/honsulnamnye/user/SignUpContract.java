package com.obppamanse.honsulnamnye.user;

import android.app.Activity;

import com.obppamanse.honsulnamnye.user.model.UserInfo;

/**
 * Created by raehyeong.park on 2017. 4. 28..
 */

public class SignUpContract {

    public interface View {

        Activity getActivity();

        void showException(Exception e);

        void startMainActivity();

        void chooseProfileImage();
    }

    public interface Model {

        void requestSignUp(Activity activity, SignUpContract.SignUpCompleteListener listener);

        void setNickName(CharSequence nickName);

        void setGender(UserInfo.Gender gender);

        void setProfileUri(String profileUri);

        String getProfileName();

        String getProfileUri();
    }

    public interface SignUpCompleteListener {

        void onSuccess();

        void onFailed(Exception e);
    }

    public static class NickNameIsEmptyException extends Exception {
        @Override
        public String getMessage() {
            return "닉네임을 입력하여 주세요.";
        }
    }

    public static class NotSignedUpException extends Exception {
        @Override
        public String getMessage() {
            return "알 수 없는 오류가 발생하였습니다.";
        }
    }

    public static class UpdateFailedUserInfoException extends Exception {

        private String extra;

        public UpdateFailedUserInfoException(String extra) {
            this.extra = extra;
        }

        @Override
        public String getMessage() {
            return "회원가입을 실패하였습니다. 원인[" + extra + "]";
        }
    }
}
