package com.obppamanse.honsulnamnye.user;

import com.obppamanse.honsulnamnye.user.model.UserInfo;

/**
 * Created by Ravy on 2017. 6. 24..
 */

public class UserProfileContract {

    public interface View {

    }

    public interface Model {
        UserInfo getUser();
    }
}
