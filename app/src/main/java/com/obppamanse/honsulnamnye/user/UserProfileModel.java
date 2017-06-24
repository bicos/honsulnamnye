package com.obppamanse.honsulnamnye.user;

import com.obppamanse.honsulnamnye.user.model.UserInfo;

/**
 * Created by Ravy on 2017. 6. 24..
 */

public class UserProfileModel implements UserProfileContract.Model {

    private UserInfo user;

    public UserProfileModel(UserInfo user) {
        this.user = user;
    }

    @Override
    public UserInfo getUser() {
        return user;
    }
}
