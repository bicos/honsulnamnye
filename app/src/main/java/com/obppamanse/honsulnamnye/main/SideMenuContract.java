package com.obppamanse.honsulnamnye.main;

import android.net.Uri;

import com.google.firebase.storage.StorageReference;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class SideMenuContract {

    public interface View {
        void successLogout();
    }

    public interface Request {
        String getUserName();

        String getUserEmail();

        StorageReference getProfileStorageReference();

        void requestLogout();
    }
}
