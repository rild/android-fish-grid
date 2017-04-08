package rimp.rild.com.android.android_blocks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rild on 2017/04/08.
 */

public class LogMessageManager {
    private static String[] messages = new String[5];

    // FIFO
    public static void add(String message) {
        for (int i = messages.length - 1; i > 0; i--) {
            messages[i] = messages[i - 1];
        }
        messages[0] = getNowDate() + "\n" + message;
    }

    public static void add(String tag, String message) {
        add("    [" + tag + "]:" + message);
    }

    public static String getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    public static void clear() {
        for (int i = 0; i < messages.length; i++) {
            messages[i] = "";
        }
    }

    public static String getAllLogs() {
        for (int i = 0; i < messages.length; i++) {
            if (messages[i] == null) messages[i] = "";
        }
        return messages[0] + "\n" +
                messages[1] + "\n" +
                messages[2] + "\n" +
                messages[3] + "\n" +
                messages[4];
    }
}
