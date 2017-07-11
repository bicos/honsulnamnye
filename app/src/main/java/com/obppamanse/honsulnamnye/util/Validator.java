package com.obppamanse.honsulnamnye.util;

import android.text.TextUtils;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * Created by raehyeong.park on 2017. 7. 11..
 */

public class Validator {

    private Validator() {

    }

    public static final boolean validateId(String id) {
        return EmailValidator.getInstance().isValid(id);
    }
}
