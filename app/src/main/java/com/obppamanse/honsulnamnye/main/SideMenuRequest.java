package com.obppamanse.honsulnamnye.main;

import android.net.Uri;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.storage.StorageReference;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class SideMenuRequest implements SideMenuContract.Request {

    private FirebaseAuth auth;

    public SideMenuRequest() {
        auth = FirebaseAuth.getInstance();
    }

    private FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    @Override
    public String getUserName() {
        return getCurrentUser() != null ? getCurrentUser().getDisplayName() : null;
    }

    @Override
    public String getUserEmail() {
        return getCurrentUser() != null ? getCurrentUser().getEmail() : null;
    }

    @Override
    public StorageReference getProfileStorageReference() {
        String child = null;

        FirebaseUser user = getCurrentUser();

        if (user != null) {
            child = user.getEmail();
        }

        return !TextUtils.isEmpty(child) ? FirebaseUtils.getProfileStorageRef().child(user.getEmail()) : null;
    }

    @Override
    public void requestLogout() {
        auth.signOut();
    }

}
