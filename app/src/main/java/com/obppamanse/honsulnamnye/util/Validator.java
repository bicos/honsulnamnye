package com.obppamanse.honsulnamnye.util;

/**
 * Created by raehyeong.park on 2017. 7. 11..
 */

public class Validator {

    private Validator() {

    }

    public static final boolean validateId(String id) {
        return id != null && id.contains("@");
    }
}
