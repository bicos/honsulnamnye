package com.obppamanse.honsulnamnye.main;


import com.google.firebase.database.ValueEventListener;
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

        void startSyncUserInfo(ValueEventListener listener);

        void stopSyncUserInfo(ValueEventListener listener);

        String getUserName();

        String getUserEmail();

        void requestLogout();

        String getProfileUrl();

        void setUserInfo(UserInfo userInfo);

        UserInfo getCurrentUser();
    }

    public static class FailedGetUserInfo extends Exception{
        @Override
        public String getMessage() {
            return "유저 정보를 가져오지 못하였습니다.";
        }
    }
}
