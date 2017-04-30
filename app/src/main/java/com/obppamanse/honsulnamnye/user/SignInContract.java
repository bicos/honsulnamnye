package com.obppamanse.honsulnamnye.user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by raehyeong.park on 2017. 4. 25..
 */

public class SignInContract {

    public interface View {

        Context getContext();

        void startMainActivity();

        void showUserNotFoundAlertDialog(DialogInterface.OnClickListener positiveClickListener);

        void startSignUpActivity();

        Fragment getFragment();

        void showException(Exception e);
    }

    public interface Model {

        void setEmail(String email);

        void setPassword(String password);

        void signInWithFacebook(Fragment fragment, SignInModel.SignInCompleteListener listener);

        void signInWithGoogle(Fragment fragment, SignInModel.SignInCompleteListener listener);

        void signInWithEmail(Fragment fragment, SignInModel.SignInCompleteListener listener);

        void onActivityResult(Fragment fragment, int requestCode, int resultCode, Intent data);

        void requestSignUp(Fragment fragment, SignInModel.SignInCompleteListener listener);

        void isUserSignedUp(ValueEventListener listener);
    }

    public static class EmailNotValidException extends Exception {
        @Override
        public String getMessage() {
            return "이메일 형식이 올바르지 않습니다.";
        }
    }

    public static class EmailIsEmptyException extends Exception {
        @Override
        public String getMessage() {
            return "이메일을 입력해주세요.";
        }
    }

    public static class PasswordIdEmptyException extends Exception {
        @Override
        public String getMessage() {
            return "패스워드를 입력해주세요.";
        }
    }

    public static class SignInCancelException extends Exception {
        @Override
        public String getMessage() {
            return "로그인을 취소하였습니다.";
        }
    }

    public static class SignInFailedException extends Exception {

        private String extraInfo;

        public SignInFailedException(String extraInfo) {
            this.extraInfo = extraInfo;
        }

        @Override
        public String getMessage() {
            return "로그인을 실패하였습니다. 원인[" + extraInfo + "]";
        }
    }
}
