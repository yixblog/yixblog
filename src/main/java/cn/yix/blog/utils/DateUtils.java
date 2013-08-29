package cn.yix.blog.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-26
 * Time: 下午9:54
 *
 * class do date format job
 */
public class DateUtils {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static String getDateString(long timeMillis,String format){
        Date date = new Date(timeMillis);
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static long getTimeMillis(String dateString,String format){
        DateFormat dateFormat = new SimpleDateFormat(format);
        try {
            Date date = dateFormat.parse(dateString);
            return date.getTime();
        } catch (ParseException ignored) {
        }
        return 0;
    }

    public static Date parseDate(String dateString,String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException ignored) {
        }
        return null;
    }
}
