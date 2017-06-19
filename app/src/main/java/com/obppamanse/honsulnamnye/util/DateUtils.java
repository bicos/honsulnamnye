package com.obppamanse.honsulnamnye.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ravy on 2017. 6. 19..
 */

public class DateUtils {

    public static String getDateStr(long timeMillis) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 (EEEE)", Locale.KOREA);
        return format.format(new Date(timeMillis));
    }
}
