package ninja.taskbook.util.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//----------------------------------------------------------------------------------------------------
public class Helper {

    //----------------------------------------------------------------------------------------------------
    public static String calendarToDateString(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd", Locale.CHINESE);
        return formatter.format(calendar.getTime());
    }

    //----------------------------------------------------------------------------------------------------
    public static String calendarToTimeString(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.CHINESE);
        return formatter.format(calendar.getTime());
    }

    //----------------------------------------------------------------------------------------------------
    public static String calendarToString(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd HH:mm", Locale.CHINESE);
        return formatter.format(calendar.getTime());
    }

    //----------------------------------------------------------------------------------------------------
    public static Calendar dateStringToCalendar(String dateString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd", Locale.CHINESE);
            Date date = formatter.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //----------------------------------------------------------------------------------------------------
    public static Calendar timeStringToCalendar(String timeString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.CHINESE);
            Date date = formatter.parse(timeString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //----------------------------------------------------------------------------------------------------
    public static Calendar stringToCalendar(String string) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd HH:mm", Locale.CHINESE);
            Date date = formatter.parse(string);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
