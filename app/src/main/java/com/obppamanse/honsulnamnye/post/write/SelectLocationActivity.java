package com.obppamanse.honsulnamnye.post.write;

import android.os.Bundle;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapView;

/**
 * Created by Ravy on 2017. 6. 20..
 */

public class SelectLocationActivity extends NMapActivity {

    private NMapView mMapView;

    private static final String CLIENT_ID = "fKAxiF8wSbyaUvKpcODM";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        mMapView = new NMapView(this);
        setContentView(mMapView);
        mMapView.setClientId(CLIENT_ID); // 클라이언트 아이디 값 설정
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();
    }


}
