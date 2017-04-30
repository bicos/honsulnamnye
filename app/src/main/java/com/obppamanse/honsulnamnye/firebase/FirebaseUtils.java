package com.obppamanse.honsulnamnye.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by raehyeong.park on 2017. 4. 30..
 */

public class FirebaseUtils {

    private static final String USER_REF = "user";

    public static DatabaseReference getUserRef(){
        return FirebaseDatabase.getInstance().getReference().child(USER_REF);
    }
}
