package com.obppamanse.honsulnamnye.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.SplashActivity;
import com.obppamanse.honsulnamnye.personal.PersonalFragment;
import com.obppamanse.honsulnamnye.post.write.PostWriteActivity;
import com.obppamanse.honsulnamnye.search.SearchFragment;
import com.obppamanse.honsulnamnye.timeline.TimeLineFragment;
import com.obppamanse.honsulnamnye.util.ActivityUtils;

public class MainActivity extends AppCompatActivity {

    private static final int INDEX_TIMELINE = 0;
    private static final int INDEX_PERSONAL = 1;
    private static final int INDEX_SEARCH = 2;

    private int currentIndex = 0;

    private FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if (firebaseAuth.getCurrentUser() == null) {
                Toast.makeText(getApplicationContext(), R.string.msg_logout, Toast.LENGTH_SHORT).show();
                SplashActivity.start(MainActivity.this);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PostWriteActivity.class));
            }
        });

        DrawerLayout layout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                layout, toolbar, R.string.open_drawer, R.string.close_drawer);
        toggle.syncState();

        if (savedInstanceState != null) {
            selectTab(savedInstanceState.getInt("current_index"));
        } else {
            selectTab(INDEX_TIMELINE);
        }

        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("current_index", currentIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
        super.onDestroy();
    }

    private void selectTab(int index) {
        if (INDEX_TIMELINE == index) {
            ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(),
                    TimeLineFragment.newInstance(),
                    R.id.container_main);
        } else if (INDEX_PERSONAL == index) {
            ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(),
                    PersonalFragment.newInstance(),
                    R.id.container_main);
        } else if (INDEX_SEARCH == index) {
            ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(),
                    SearchFragment.newInstance(),
                    R.id.container_main);
        }
        currentIndex = index;
    }

    public void clickTimeLine(View view) {
        selectTab(INDEX_TIMELINE);
    }

    public void clickPersonal(View view) {
        selectTab(INDEX_PERSONAL);
    }

    public void clickSearch(View view) {
        selectTab(INDEX_SEARCH);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
