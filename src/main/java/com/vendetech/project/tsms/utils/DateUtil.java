package com.vendetech.project.tsms.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
    public static final String PATTERN_YEAR = "yyyy";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_DATE2 = "yyyyMMdd";
    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATETIME2 = "yyyyMMddHHmmss";
    public static final String PATTERN_DATETIME3 = "yyyyMMddHHmmssSSS";
    public static final String PATTERN_TIME = "HH:mm:ss";
    public static final String PATTERN_TIME2 = "HHmmss";

    public DateUtil() {
    }


    public static String getYear() {
        return format(getCurrentTime(), "yyyy");
    }

    public static Date getCurrentTime() {
        return new Date();
    }

    public static String getFormatCurrentTime() {
        return format(getCurrentTime(), "yyyy-MM-dd HH:mm:ss");
    }

    public static Date getToday() {
        return trimDateTime(getCurrentTime());
    }

    public static String getFormatToday() {
        return getFormatToday("yyyy-MM-dd");
    }

    public static String getFormatToday(String pattern) {
        return format(getToday(), pattern);
    }

    public static Date getNextDay() {
        return getNextDay(getCurrentTime());
    }

    public static Date getNextDay(Date date) {
        return getNextDay(date, 1);
    }

    public static Date getNextDay(Date date, int n) {
        return trimDateTime(getNextDayCurrentTime(date, n));
    }

    public static Date getNextDayCurrentTime() {
        return getNextDayCurrentTime(getCurrentTime());
    }

    public static Date getNextDayCurrentTime(Date date) {
        return getNextDayCurrentTime(date, 1);
    }

    public static Date getNextDayCurrentTime(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(6, n);
        return calendar.getTime();
    }
    /**
     * 时间加减天数
     * @param startDate 要处理的时间，Null则为当前时间
     * @param days 加减的天数
     * @return Date
     */
    public static Date dateAddDays(Date startDate, int days) {
        if (startDate == null) {
            startDate = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.DATE, c.get(Calendar.DATE) + days);
        return c.getTime();
    }

    public static String getFormatNextDay() {
        return getFormatNextDay("yyyy-MM-dd");
    }

    public static String getFormatNextDay(String pattern) {
        return format(getNextDay(), pattern);
    }

    public static String getFormatNextDayCurrentTime() {
        return getFormatNextDayCurrentTime("yyyy-MM-dd HH:mm:ss");
    }

    public static String getFormatNextDayCurrentTime(String pattern) {
        return format(getNextDayCurrentTime(), pattern);
    }

    public static Date getPreDay() {
        return getPreDay(getCurrentTime());
    }

    public static Date getPreDay(Date date) {
        return getPreDay(date, 1);
    }

    public static Date getPreDay(Date date, int n) {
        return trimDateTime(getPreDayCurrentTime(date, n));
    }

    public static Date getPreDayCurrentTime() {
        return getPreDayCurrentTime(getCurrentTime());
    }

    public static Date getPreDayCurrentTime(Date date) {
        return getPreDayCurrentTime(date, 1);
    }

    public static Date getPreDayCurrentTime(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(6, -n);
        return calendar.getTime();
    }

    public static String getFormatPreDay() {
        return getFormatPreDay("yyyy-MM-dd");
    }

    public static String getFormatPreDay(String pattern) {
        return format(getPreDay(), pattern);
    }

    public static String getFormatPreDayCurrentTime() {
        return getFormatPreDayCurrentTime("yyyy-MM-dd HH:mm:ss");
    }

    public static String getFormatPreDayCurrentTime(String pattern) {
        return format(getPreDayCurrentTime(), pattern);
    }

    public static Date getFirstDayOfMonth() {
        return getFirstDayOfMonth(getToday());
    }

    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(5, 1);
        return calendar.getTime();
    }

    public static Date firstDayofPreMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(2, -1);
        calendar.set(5, 1);
        return calendar.getTime();
    }

    public static Date lastDayOfPreMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(5, 1);
        return getPreDay(calendar.getTime());
    }

    public static int countOfPreMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastDayOfPreMonth(date));
        return calendar.get(5);
    }

    public static Date getFirstDayOfNextMonth() {
        return getFirstDayOfNextMonth(getToday());
    }

    public static Date getFirstDayOfNextMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(2, 1);
        calendar.set(5, 1);
        return calendar.getTime();
    }

    public static boolean isNextDay(Date date1, Date date2) {
        Date startDate = getNextDay(date1);
        Date endDate = getNextDay(startDate);
        return !date2.before(startDate) && date2.before(endDate);
    }

    public static boolean isPreDay(Date date1, Date date2) {
        Date startDate = getPreDay(date1);
        Date endDate = getNextDay(startDate);
        return !date2.before(startDate) && date2.before(endDate);
    }

    public static Date trimDateTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTime();
    }

    public static String format(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    public static Date toDate(String date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        try {
            return formatter.parse(date);
        } catch (ParseException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static List<Date> getDates(String startDateStr, String endDateStr) {
        Date startDate = toDate(startDateStr, "yyyy-MM-dd");
        Date endDate = toDate(endDateStr, "yyyy-MM-dd");
        List<Date> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (calendar.getTime().getTime() <= endDate.getTime()) {
            Date date = calendar.getTime();
            result.add(date);
            calendar.add(5, 1);
        }

        return result;
    }

    public static Integer getWeekNumber(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(7) - 1 == 0 ? 7 : c.get(7) - 1;
    }
}