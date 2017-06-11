package com.obppamanse.honsulnamnye.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

/**
 * Created by raehyeong.park on 2017. 4. 26..
 */

public class SignInViewModel implements SignInModel.SignInCompleteListener {

    private SignInContract.View view;

    private SignInContract.Model model;

    public SignInViewModel(SignInContract.View view, SignInContract.Model model) {
        this.view = view;
        this.model = model;
    }

    public void inputEmail(CharSequence c) {
        if (!TextUtils.isEmpty(c)) {
            model.setEmail(c.toString());
        }
    }

    public void inputPassword(CharSequence c) {
        if (!TextUtils.isEmpty(c)) {
            model.setPassword(c.toString());
        }
    }

    public void clickLoginWithEmail() {
        model.signInWithEmail(view.getFragment(), this);
    }

    public void clickLoginWithFacebook() {
        model.signInWithFacebook(view.getFragment(), this);
    }

    public void clickLoginWithGoogle() {
        model.signInWithGoogle(view.getFragment(), this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        model.onActivityResult(view.getFragment(), requestCode, resultCode, data);
    }

    private void startSignUp() {
        model.requestSignUp(view.getFragment(), this);
    }

    /**
     * 로그인 및 간이 회원가입 성공
     */
    @Override
    public void onSuccess() {
        model.isUserSignedUp(new SignInModel.SignedUpCompleteListener() {
            @Override
            public void onSuccess(boolean isSignedUp) {
                if (isSignedUp) {
                    view.startMainActivity();
                } else {
                    view.startSignUpActivity();
                }
            }

            @Override
            public void onFailed(Exception e) {
                view.showException(e);
            }
        });
    }

    /**
     * 로그인 실패
     *
     * @param e
     */
    @Override
    public void onFailed(Exception e) {
        if (e instanceof FirebaseAuthInvalidUserException) {
            view.showUserNotFoundAlertDialog(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startSignUp();
                }
            });
        } else if (e instanceof FirebaseAuthUserCollisionException) {
            view.showException(new SignInContract.SignInFailedException("이메일이나 다른 SNS로 가입되어 있습니다."));
        } else {
            view.showException(e);
        }
    }
}
