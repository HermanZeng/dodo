package utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fan on 7/19/2016.
 */
public class TimeUtil {
    public static String getCurrentDate() {
        Date date = new Date();
        DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = formater.format(date);
        return now;
    }
}
