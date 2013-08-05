package se.persandstrom.highscoretracker.internal.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * NICIFIER OF DATES!!!! (ò_Ó)
 */
public class DateNicifier {

    private static SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat("dd MMM -yy");

    public static String nicify(long unixTimeSeconds) {
        Date date = new Date(unixTimeSeconds * 1000);
        return dateformatYYYYMMDD.format(date);
    }
}
