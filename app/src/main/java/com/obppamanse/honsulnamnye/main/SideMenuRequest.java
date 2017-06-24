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

    public SideMenuRequest() {
        auth = FirebaseAuth.getInstance();
    }

    private FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    @Override
    public void getUserInfo(final SideMenuContract.RequestUserInfoListener listener) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            return;
        }

        FirebaseUtils.getUserRef().child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                if (userInfo != null) {
                    listener.onSuccess(userInfo);
                } else {
                    listener.onFailed(new PostContract.NotExistAuthUserException());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError.toException());
            }
        });
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
