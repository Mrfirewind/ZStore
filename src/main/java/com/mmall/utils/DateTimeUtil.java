package com.mmall.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;

public class DateTimeUtil {

    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将字符串转为时间
     *
     * @param dateTimeStr
     * @param formateStr
     * @return
     */
    public static Date strToDate(String dateTimeStr, String formateStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formateStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    /**
     * 将时间转为字符串
     *
     * @param date
     * @param formateStr
     * @return
     */
    public static String dateToStr(Date date, String formateStr) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formateStr);
    }

    /**
     * 将字符串转为时间
     * 默认时间格式"yyyy-MM-dd HH:mm:ss"
     *
     * @param dateTimeStr
     * @return
     */
    public static Date strToDate(String dateTimeStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    /**
     * 将时间转为字符串
     * 默认时间格式"yyyy-MM-dd HH:mm:ss"
     *
     * @param date
     * @return
     */
    public static String dateToStr(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }
}
