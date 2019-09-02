package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static String getGMTString() {
        Date currentTime = new Date();

        final SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");

        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
//        System.out.println("GMT time: " + sdf.format(currentTime));

//        Tue, 09 Jul 2019 11:21:28 GMT

        return sdf.format(currentTime);
    }

    public static void main(String[] args) {
        getGMTString();
    }
}
