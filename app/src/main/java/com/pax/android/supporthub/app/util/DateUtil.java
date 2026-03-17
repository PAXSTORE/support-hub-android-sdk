package com.pax.android.supporthub.app.util;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    private static final String TAG = DateUtil.class.getSimpleName();

    public static Long getTimeStamp(String time) {
        if (TextUtils.isEmpty(time)) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date parsedDate = sdf.parse(time);
            if (null != parsedDate) {
                return parsedDate.getTime();
            } else {
                Log.e(TAG,"parsedDate is null");
            }
        } catch (ParseException e) {
            Log.e(TAG, "getTimeStamp err: " + e);
        }
        return null;
    }

    public static String getDateStr(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public static String getDateStr(Long timeStamp) {
        Date date = new Date(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(date);
    }


    public static boolean isDateBefore(String date1, String date2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);
            if (d1 != null && d2 != null) {
                return d1.before(d2);   // 只判断「之前」
            }
        } catch (ParseException e) {
            Log.w(TAG, "isDateBefore err: " + e);
        }
        return false;   // 解析失败也视为「不早于」
    }

}
