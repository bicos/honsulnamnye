package com.obppamanse.honsulnamnye.user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.google.firebase.auth.AuthCredential;

/**
 * Created by raehyeong.park on 2017. 4. 25..
 */

public class SignInContract {

    public interface View {

        Context getContext();

        void startMainActivity();

        void showUserNotFoundAlertDialog(DialogInterface.OnClickListener positiveClickListener);

        void startSignUpActivity(AuthCredential credential);
    }

    public interface Model {

        void setEmail(String email);

        void setPassword(String password);

        void signInWithFacebook(SignInModel.SignInCompleteListener listener);

        void signInWithGoogle(SignInModel.SignInCompleteListener listener);

        void signInWithEmail(SignInModel.SignInCompleteListener listener);

        void onActivityResult(int requestCode, int resultCode, Intent data);
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
}
