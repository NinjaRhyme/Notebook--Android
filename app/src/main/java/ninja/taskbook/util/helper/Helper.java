package ninja.taskbook.util.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//----------------------------------------------------------------------------------------------------
public class Helper {

    //----------------------------------------------------------------------------------------------------
    public static Date stringToDate(String style, String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    //----------------------------------------------------------------------------------------------------
    public static String dateToString(String style, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        return formatter.format(date);
    }

}
