package com.obppamanse.honsulnamnye.main;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.user.model.UserInfo;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class SideMenuRequest implements SideMenuContract.Request {

    private FirebaseAuth auth;

    private UserInfo userInfo;

    public SideMenuRequest() {
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public UserInfo getCurrentUser() {
        return userInfo;
    }

    @Override
    public void startSyncUserInfo(ValueEventListener listener) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            return;
        }

        FirebaseUtils.getUserRef().child(user.getUid()).addValueEventListener(listener);
    }

    @Override
    public void stopSyncUserInfo(ValueEventListener listener) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            return;
        }

        FirebaseUtils.getUserRef().child(user.getUid()).removeEventListener(listener);
    }

    @Override
    public String getUserName() {
        return getCurrentUser() != null ? getCurrentUser().nickName : null;
    }

    @Override
    public String getUserEmail() {
        return getCurrentUser() != null ? getCurrentUser().email : null;
    }

    @Override
    public void requestLogout() {
        auth.signOut();
    }

    @Override
    public String getProfileUrl() {
        return getCurrentUser() != null ? getCurrentUser().profileUri : null;
    }
}
