package com.obppamanse.honsulnamnye.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.SplashActivity;
import com.obppamanse.honsulnamnye.main.model.Category;
import com.obppamanse.honsulnamnye.post.write.PostWriteActivity;
import com.obppamanse.honsulnamnye.timeline.TimeLineFragment;
import com.obppamanse.honsulnamnye.util.ActivityUtils;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private static final String PARAM_CATEGORY = "category";

    FloatingActionButton fab;

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

        fab = findViewById(R.id.fab);
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

        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(),
                CategorySelectFragment.newInstance(),
                R.id.container_main,
                MainActivity.class.getSimpleName());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Category category = null;

        if (intent.hasExtra(PARAM_CATEGORY)) {
            category = intent.getParcelableExtra(PARAM_CATEGORY);
        }

        ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(),
                TimeLineFragment.newInstance(category),
                R.id.container_main,
                MainActivity.class.getSimpleName());
    }

    @Override
    protected void onDestroy() {
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
        super.onDestroy();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void moveCategory(Context context, Category category) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(MainActivity.PARAM_CATEGORY, category);
        context.startActivity(intent);
    }

    @Override
    public void onBackStackChanged() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container_main);
        if (currentFragment instanceof CategorySelectFragment) {
            fab.hide();
        } else {
            fab.show();
        }
    }
}
