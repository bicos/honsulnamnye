package com.obppamanse.honsulnamnye.user;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

/**
 * Created by raehyeong.park on 2017. 4. 26..
 */

public class SignInModel implements SignInContract.Model, OnCompleteListener<AuthResult> {

    private String email;

    private String password;

    private SignInCompleteListener listener;

    // 파이어베이스 로그인 모듈
    private FirebaseAuth auth;

    // 페이스북 로그인 모듈
    private LoginManager facebookLoginManager;
    private CallbackManager manager;
    private Fragment fragment;

    public SignInModel(final Fragment fragment) {
        auth = FirebaseAuth.getInstance();

        this.fragment = fragment;
        this.facebookLoginManager = LoginManager.getInstance();
        this.manager = CallbackManager.Factory.create();

        this.facebookLoginManager.registerCallback(manager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                requestSignIn(credential);
            }

            @Override
            public void onCancel() {
                if (listener != null) {
                    listener.onFailed(new FacebookOperationCanceledException());
                }
            }

            @Override
            public void onError(FacebookException error) {
                if (listener != null) {
                    listener.onFailed(error);
                }
            }
        });
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void signInWithFacebook(SignInCompleteListener listener) {
        this.listener = listener;
        facebookLoginManager.logInWithReadPermissions(fragment, Arrays.asList("email", "public_profile"));
    }

    @Override
    public void signInWithGoogle(SignInCompleteListener listener) {
        this.listener = listener;
    }

    @Override
    public void signInWithEmail(SignInCompleteListener listener) {
        this.listener = listener;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        manager.onActivityResult(requestCode, resultCode, data);
    }

    private void requestSignIn(AuthCredential authCredential) {
        auth.signInWithCredential(authCredential).addOnCompleteListener(fragment.getActivity(), this);
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (listener == null) {
            return;
        }

        if (task.isSuccessful()) {
            listener.onSuccess();
        } else {
            listener.onFailed(task.getException());
        }
    }

    public interface SignInCompleteListener {

        void onSuccess();

        void onFailed(Exception e);
    }
}
