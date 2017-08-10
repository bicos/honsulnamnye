package com.obppamanse.honsulnamnye.push;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;

/**
 * Created by Ravy on 2017. 8. 10..
 */

public class DariInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "DariInstanceIDService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            FirebaseUtils.getUserRef().child(currentUser.getUid()).child(FirebaseUtils.PUSH_TOKEN_REF).setValue(refreshedToken);
        }
    }
}
