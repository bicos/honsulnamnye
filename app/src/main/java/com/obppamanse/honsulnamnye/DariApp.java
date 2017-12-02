package com.obppamanse.honsulnamnye;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by raehyeong.park on 2017. 7. 24..
 */

public class DariApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        initFirebase();
        initGlide();
    }

    private void initFirebase() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    private void initGlide() {
        Glide.get(this).clearMemory();
    }
}
