package com.obppamanse.honsulnamnye.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ravy on 2017. 6. 19..
 */

public class DateUtils {

    private static final String COMMON_DATE_FORMAT = "yyyy년 MM월 dd일 (EEEE) a hh:mm";

    public static final String SIMPLE_DATE_FORMAT = "MM.dd kk:mm:ss";

    public static String getDateStr(long timeMillis) {
        return getDateStr(timeMillis, COMMON_DATE_FORMAT);
    }

    public static String getDateStr(long timeMillis, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.KOREA);
        return simpleDateFormat.format(new Date(timeMillis));
    }
}
