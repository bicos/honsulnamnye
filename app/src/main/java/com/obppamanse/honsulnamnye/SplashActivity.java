package com.obppamanse.honsulnamnye;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.main.MainActivity;
import com.obppamanse.honsulnamnye.user.model.UserInfo;
import com.obppamanse.honsulnamnye.user.signin.SignInFragment;
import com.obppamanse.honsulnamnye.util.ActivityUtils;

/**
 * Created by Ravy on 2017. 4. 15..
 */

public class SplashActivity extends AppCompatActivity {

    private ContentLoadingProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.loading_progress);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            progressBar.hide();
            startSignInFragment();
        } else {
            progressBar.show();
            requestExistCurrentUser(FirebaseAuth.getInstance().getCurrentUser());
        }
    }

    private void startSignInFragment() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container_splash);
        if (currentFragment == null || !(currentFragment instanceof SignInFragment)) {
            ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(),
                    SignInFragment.newInstance(),
                    R.id.container_splash);
        }
    }

    private void requestExistCurrentUser(FirebaseUser currentUser) {
        FirebaseUtils.getUserRef()
                .child(currentUser.getUid())
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                progressBar.hide();
                                if (dataSnapshot.exists()) {
                                    sendRegistrationToServer(dataSnapshot.getValue(UserInfo.class));
                                    MainActivity.start(SplashActivity.this);
                                    finish();
                                } else {
                                    SignUpActivity.start(SplashActivity.this);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                progressBar.hide();
                                Toast.makeText(getApplicationContext(), "유저 정보를 가져오는 중 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                );
    }

    private void sendRegistrationToServer(UserInfo userInfo) {
        if (userInfo != null && userInfo.uid != null) {
            FirebaseUtils.getUserRef()
                    .child(userInfo.uid)
                    .child(FirebaseUtils.PUSH_TOKEN_REF)
                    .setValue(FirebaseInstanceId.getInstance().getToken());
        }
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SplashActivity.class);
        activity.startActivity(intent);
    }
}
