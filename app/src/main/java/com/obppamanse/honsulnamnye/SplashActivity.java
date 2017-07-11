package com.obppamanse.honsulnamnye;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.main.MainActivity;
import com.obppamanse.honsulnamnye.user.SignInFragment;
import com.obppamanse.honsulnamnye.util.ActivityUtils;

/**
 * Created by Ravy on 2017. 4. 15..
 */

public class SplashActivity extends AppCompatActivity {

    public static final String PARAM_ACTION = "action";

    public static final String ACTION_START_MAIN_ACTIVITY = "start_main_activity";

    private ContentLoadingProgressBar progressBar;

    private DatabaseReference reference;

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            progressBar.hide();
            if (dataSnapshot.exists()) {
                startMainActivity(SplashActivity.this);
            } else {
                Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // do nothing
            progressBar.hide();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = (ContentLoadingProgressBar) findViewById(R.id.loading_progress);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            progressBar.hide();
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container_splash);
            if (currentFragment == null || !(currentFragment instanceof SignInFragment))
                ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(),
                        SignInFragment.newInstance(),
                        R.id.container_splash);
        } else {
            requestExistCurrentUser(FirebaseAuth.getInstance().getCurrentUser());
        }
    }

    @Override
    protected void onDestroy() {
        if (reference != null) {
            reference.removeEventListener(listener);
        }
        super.onDestroy();
    }

    private void requestExistCurrentUser(FirebaseUser currentUser) {
        progressBar.show();
        reference = FirebaseUtils.getUserRef().child(currentUser.getUid());
        reference.addListenerForSingleValueEvent(listener);
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
