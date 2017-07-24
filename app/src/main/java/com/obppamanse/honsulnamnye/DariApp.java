package com.obppamanse.honsulnamnye;

import android.app.Application;

import com.bumptech.glide.Glide;

/**
 * Created by raehyeong.park on 2017. 7. 24..
 */

public class DariApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        initGlide();
    }

    private void initGlide() {
        Glide.get(this).clearMemory();
    }
}
