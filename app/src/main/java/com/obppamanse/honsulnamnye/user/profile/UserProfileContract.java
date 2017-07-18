package com.obppamanse.honsulnamnye.user.profile;

import android.net.Uri;

import com.obppamanse.honsulnamnye.user.model.UserInfo;

/**
 * Created by Ravy on 2017. 6. 24..
 */

public class UserProfileContract {

    public interface View {
        void chooseProfileImage();
    }

    public interface Model {
        UserInfo getUser();

        void modifyUserGender();

        void withdrawalService();

        void updateProfile();

        boolean isModifyProfileImage();

        void setModifyProfileImage(boolean modifyProfileImage);

        boolean isModifyUserName();

        void setModifyUserName(boolean modifyUserName);

        boolean isModifyUserGender();

        void setModifyUserGender(boolean modifyUserGender);
    }
}
