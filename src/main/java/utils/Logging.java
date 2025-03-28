package utils;

import java.io.*;
import java.time.LocalDate;
import java.util.Date;

public class Logging implements AutoCloseable {
    private static final BufferedWriter log_file = initialize();

    public static String messagePrefix(Object class_obj) {
        StringBuilder sb = new StringBuilder();

        Date currentDate = new Date();

        sb.append('[');
        sb.append(currentDate);
        sb.append("][");
        if (class_obj instanceof Class) {
            sb.append(((Class) class_obj).getName());
        } else {
            sb.append(class_obj.getClass().getName());
        }
        sb.append("] ");

        return sb.toString();
    }

    private static BufferedWriter initialize() {
        BufferedWriter log_obj;

        String append = ".txt";
        LocalDate ld = LocalDate.now();

        for (int i = 1; true; i++) {
            if (!new File("log/" + ld + append).exists()) {
                break;
            }
            append = " (" + i + ").txt";
        }

        try {
            new File("log").mkdir();
            log_obj = new BufferedWriter(new FileWriter("log/" + ld + append));
        } catch (Exception e) {
            System.err.println("[ ERROR ]" + messagePrefix(Logging.class) + "Log file unable to be initialized");
            return null;
        }

        return log_obj;
    }

    private static void writeToLog(String message) {
        try {
            if (log_file != null) {
                log_file.write(message + '\n');
                log_file.flush();
            }
        } catch (IOException e) {
            System.err.println("[ ERROR ]" + messagePrefix(Logging.class) + "Unable to write to log file");
        }
    }

    public static void error(Object this_obj, String error_message) {
        String message = "[ ERROR ]" + messagePrefix(this_obj) + error_message;

        writeToLog(message);
        System.err.println(message);
    }

    public static void write(Object this_obj, String message) {
        message = "[  OUT  ]" + messagePrefix(this_obj) + message;

        writeToLog(message);
        System.out.println(message);
    }

    @Override
    public void close() throws Exception {
        if (log_file != null) {
            log_file.close();
        };
    }
}
