package com.obppamanse.honsulnamnye.main;


import com.google.firebase.storage.StorageReference;
import com.obppamanse.honsulnamnye.user.model.UserInfo;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class SideMenuContract {

    public interface View {
        void successLogout();

        void failedGetUserInfo(Exception e);
    }

    public interface Request {

        void getUserInfo(RequestUserInfoListener listener);

        String getUserName();

        String getUserEmail();

        StorageReference getProfileStorageReference();

        void requestLogout();
    }

    public interface RequestUserInfoListener {
        void onSuccess(UserInfo userInfo);

        void onFailed(Exception e);
    }
}
