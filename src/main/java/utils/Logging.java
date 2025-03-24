package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Date;

public class Logging implements AutoCloseable {
    private static final BufferedWriter log_file = initialize();

    public static String currentDateTime() {
        StringBuilder sb = new StringBuilder();

        Date currentDate = new Date();

        sb.append('[');
        sb.append(currentDate);
        sb.append(']');

        return sb.toString();
    }

    private static BufferedWriter initialize() {
        BufferedWriter log_obj;

        try {


            log_obj = new BufferedWriter(new FileWriter("log/"));
        } catch (Exception e) {
            return null;
        }

        return log_obj;
    }

    public static void error(Object classObj, String s) {
        classObj.getClass().getName();
    }

    @Override
    public void close() throws Exception {
        log_file.close();
    }
}
