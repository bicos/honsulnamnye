package com.obppamanse.honsulnamnye;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.obppamanse.honsulnamnye.personal.PersonalFragment;
import com.obppamanse.honsulnamnye.search.SearchFragment;
import com.obppamanse.honsulnamnye.timeline.TimeLineFragment;
import com.obppamanse.honsulnamnye.util.ActivityUtils;

public class MainActivity extends AppCompatActivity {

    private static final int INDEX_TIMELINE = 0;
    private static final int INDEX_PERSONAL = 1;
    private static final int INDEX_SEARCH = 2;

    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (savedInstanceState != null) {
            selectTab(savedInstanceState.getInt("current_index"));
        } else {
            selectTab(INDEX_TIMELINE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("current_index", currentIndex);
        super.onSaveInstanceState(outState);
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
}
