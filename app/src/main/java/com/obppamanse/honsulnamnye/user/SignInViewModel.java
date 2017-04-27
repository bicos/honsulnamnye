package com.obppamanse.honsulnamnye.user;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

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

    public void clickLoginWithEmail(){
        model.signInWithEmail(this);
    }

    public void clickLoginWithFacebook(){
        model.signInWithFacebook(this);
    }

    public void clickLoginWithGoogle(){
        model.signInWithGoogle(this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // facebook callback listener
        model.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 로그인 성공
     */
    @Override
    public void onSuccess() {

    }

    /**
     * 로그인 실패
     *
     * @param e
     */
    @Override
    public void onFailed(Exception e) {
        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
