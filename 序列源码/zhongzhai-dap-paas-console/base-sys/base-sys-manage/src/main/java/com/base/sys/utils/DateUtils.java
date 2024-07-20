package com.base.sys.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @Author: scalor
 * @Date: 2021/1/27:09:40
 * @Descricption:
 */
public class DateUtils {
    public static String dealDateFormat(String oldDate) {
        Date date1 = null;
        DateFormat df2 = null;
        try {
            oldDate= oldDate.replace("Z", " UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            Date date = df.parse(oldDate);
            SimpleDateFormat df1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
            date1 = df1.parse(date.toString());
            df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df2.format(date1);
}
  public static String esbDate() {
      String dateT="";
        try {
            DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
            Date date = new Date();
            dateT = df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateT;
}
public static String esbTime() {
      String dateT="";
        try {
            DateFormat df = new SimpleDateFormat("hhmmssSSS");
            Date date = new Date();
            dateT = df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateT;
}

    public static String nowTime(String formate) {
        String dateT="";
        try {
            DateFormat df = new SimpleDateFormat(formate);
            Date date = new Date();
            dateT = df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateT;
    }


}
