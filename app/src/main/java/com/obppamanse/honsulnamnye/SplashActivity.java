package com.obppamanse.honsulnamnye;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.obppamanse.honsulnamnye.user.SignInFragment;
import com.obppamanse.honsulnamnye.util.ActivityUtils;

/**
 * Created by Ravy on 2017. 4. 15..
 */

public class SplashActivity extends AppCompatActivity {

    public static final String PARAM_ACTION = "action";

    public static final String ACTION_START_MAIN_ACTIVITY = "start_main_activity";

    private FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if(firebaseAuth.getCurrentUser() == null) {
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                        SignInFragment.newInstance(),
                        R.id.container_splash);
            } else {
                startMainActivity(SplashActivity.this);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
        super.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        switch (intent.getStringExtra(PARAM_ACTION)) {
            case ACTION_START_MAIN_ACTIVITY:
                Intent newIntent = new Intent(this, MainActivity.class);
                startActivity(newIntent);
                finish();
                break;

            default:
                break;
        }
    }

    public static void startMainActivity(Activity activity) {
        Intent intent = new Intent(activity, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(SplashActivity.PARAM_ACTION, SplashActivity.ACTION_START_MAIN_ACTIVITY);
        activity.startActivity(intent);
    }
}
