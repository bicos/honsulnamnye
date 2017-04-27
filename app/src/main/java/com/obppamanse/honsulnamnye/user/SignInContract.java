package com.obppamanse.honsulnamnye.user;

import android.content.Context;
import android.content.Intent;

/**
 * Created by raehyeong.park on 2017. 4. 25..
 */

public class SignInContract {

    public interface View {
        Context getContext();
    }

    public interface Model {

        void setEmail(String email);

        void setPassword(String password);

        void signInWithFacebook(SignInModel.SignInCompleteListener listener);

        void signInWithGoogle(SignInModel.SignInCompleteListener listener);

        void signInWithEmail(SignInModel.SignInCompleteListener listener);

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
