package com.dap.sequence.api.util;

import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.SequenceException;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

/**
 * 日期转换类
 * @author Sunshine
 *
 */
public class DateUtils {

    public static final String UTC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
    public static final String DATE_FORMAT = "yyyyMMdd";

    public static final String DATE_FORMAT_MONTH = "yyyyMM";

    public static final String DATE_FORMAT_YEAR = "yyyy";
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";
    public static final String ISO_DATETIME_NO_T_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static DateTimeFormatter DAY_CALLBACK_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");


    /**
     * 时间格式转 UTC 
     * @param date  yyyy-MM-dd HH:mm:ss
     * @return yyyy-MM-dd'T'HH:mm:ss.SSSZZ
     */
    public static String dateFormatToUTC(String date) {
        Date d = converToDate(date, ISO_DATETIME_NO_T_FORMAT);
        return convertToString(d, UTC_DATE_FORMAT);
    }

    /**
     * 将字符串按照指定格式转换为日期
     * @param source 字符串
     * @param pattern 指定格式
     * @return 日期
     */
    public static Date converToDate(final String source, final String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(source);
        } catch (ParseException e) {
            throw  new SequenceException(ExceptionEnum.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 将日期按照指定模式转换为字符串
     *
     * @param date 日期
     * @param pattern 指定模式
     * @return 转换后的字符串
     */
    public static String convertToString(final Date date, final String pattern) {
        return Optional.ofNullable(date)
                .map(time -> new SimpleDateFormat(pattern).format(date))
                .orElseThrow(() -> new SequenceException(ExceptionEnum.INTERNAL_SERVER_ERROR));
    }

    /**
     * 取得今天对应的日期字符串
     *
     * @return String
     */
    public static String getCurrentDate() {
        return convertToString(new Date(), DATE_FORMAT);
    }

    /**
     * 取得之前对应的日期字符串
     *
     * @param day day
     * @param num num
     * @return String
     */
    public static String getPrveDay(String day, Integer num) {
        Calendar calendar = new GregorianCalendar();
        Date date = converToDate(day, DATE_FORMAT);
        return Optional.ofNullable(date)
                .map(time -> {
                    calendar.setTime(time);
                    calendar.add(Calendar.DATE, num == null ? -1 : num);
                    return convertToString(calendar.getTime(), DATE_FORMAT);
                })
                .orElseThrow(() -> new SequenceException(ExceptionEnum.INTERNAL_SERVER_ERROR));
    }

    /**
     * 取得之前对应的日期毫秒字符串
     *
     * @param day day
     * @param num num
     * @return String
     */
    public static String getPrveDateWithMill(Long day, Integer num) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(day);
        calendar.add(Calendar.DATE, num == null ? -1 : num);
        return convertToString(calendar.getTime(), DATE_FORMAT);
    }

    /**
     * 获取明天的时间串
     *
     * @param day day
     * @return String
     */
    public static String getNextDate(String day) {
        Calendar calendar = new GregorianCalendar();
        Date date = converToDate(day, DATE_FORMAT);
        return Optional.ofNullable(date)
                .map(time -> {
                    calendar.setTime(time);
                    calendar.add(Calendar.DATE, 1);
                    return convertToString(calendar.getTime(), DATE_FORMAT);
                })
                .orElse(null);

    }

    /**
     * 获取明天的毫秒时间串
     *
     * @param day day
     * @return String
     */
    public static String getNextDateWithMill(Long day) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(day);
        calendar.add(Calendar.DATE, 1);
        return convertToString(calendar.getTime(), DATE_FORMAT);
    }

    /**
     * 获取明天的毫秒时间串
     *
     * @param day day
     * @return String
     */
    public static long getNextMillWithMill(Long day) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(day);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取系统明天时间
     *
     * @return String
     */
    public static String getNextDate() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        return convertToString(calendar.getTime(), DATE_FORMAT);
    }

    /**
     * 取得今天当前现在对应的日期时间字符串  yyyy-MM-dd HH:mm:ss
     *
     * @return String
     */
    public static String getCurrentDateTime() {
        return convertToString(new Date(), ISO_DATETIME_NO_T_FORMAT);
    }


    /**
     * 将日期转换为当日开始的日期
     *
     * @param date date
     * @return String
     */
    public static String getBeginDateOfDay(final Date date) {
        try {
            return convertToString(new Date(), ISO_DATE_FORMAT) + " 00:00:00";
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 获取某个时间段内所有日期
     *
     * @param dBegin dBegin
     * @param dEnd dEnd
     * @return List
     */
    public static List<String> getDayBetweenDates(Date dBegin, Date dEnd) {
        List<String> lDate = new ArrayList<String>();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        lDate.add(sd.format(dBegin));
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(sd.format(calBegin.getTime()));
        }
        return lDate;
    }

    /**
     *
     * 日期追加天数
     *
     * @param date date
     * @param addNum  X天后  -X天之前
     * @return Date
     */
    public static Date dateAddDay(Date date, int addNum) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, addNum);
        return cal.getTime();
    }

    /**
     *
     * 年份增加
     *
     * @param date date
     * @param addNum  X天后  -X天之前
     * @return Date
     */
    public static Date dateAddYear(Date date, int addNum) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, addNum);
        return cal.getTime();
    }

    /**
     *
     * 获取X小时后的时间
     *
     * @param month month
     * @return String
     */
    public static String getMonthNum(int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, month);
        return convertToString(cal.getTime(), ISO_DATETIME_NO_T_FORMAT);
    }

    /**
     *
     * 获取X分后的时间  -X分钟之前
     *
     * @param minute minute
     * @return Date
     */
    public static Date getMinuteNum(int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, minute);
        return cal.getTime();
    }

    /**
     * 获取之前的月份
     *
     * @param time time
     * @param num num
     * @return String
     */
    public static String getPrveMonth(String time, Integer num) {
        Calendar calendar = new GregorianCalendar();
        Date date = converToDate(time, DATE_FORMAT_MONTH);
        return Optional.ofNullable(date)
                .map(ti -> {
                    calendar.setTime(ti);
                    calendar.add(Calendar.MONTH, num == null ? -1 : num);
                    return convertToString(calendar.getTime(), DATE_FORMAT_MONTH);
                })
                .orElseThrow(() -> new SequenceException(ExceptionEnum.INTERNAL_SERVER_ERROR));
    }

    /**
     * 获取之前的年份
     *
     * @param time time
     * @param num num
     * @return String
     */
    public static String getPrveYear(String time, Integer num) {
        Calendar calendar = new GregorianCalendar();
        Date date = converToDate(time, DATE_FORMAT_YEAR);
        return Optional.ofNullable(date)
                .map(ti -> {
                    calendar.setTime(ti);
                    calendar.add(Calendar.YEAR, num == null ? -1 : num);
                    return convertToString(calendar.getTime(), DATE_FORMAT_YEAR);
                })
                .orElseThrow(() -> new SequenceException(ExceptionEnum.INTERNAL_SERVER_ERROR));
    }

    public static LocalDate getLocalDateBy(String day, String callbackMode) {
        if (StringUtils.isBlank(day) || StringUtils.isBlank(callbackMode)) {
            return null;
        }

        LocalDate localDate=null;
        if (StringUtils.equals(SequenceConstant.CALLBACK_MODE_DAY, callbackMode)) {
            localDate = LocalDate.parse(day, DAY_CALLBACK_DATETIME_FORMATTER);
        }

        return localDate;
    }
}
