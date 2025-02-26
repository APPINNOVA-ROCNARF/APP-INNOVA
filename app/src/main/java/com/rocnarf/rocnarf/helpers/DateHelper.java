package com.rocnarf.rocnarf.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
    private static final String DATE_FORMAT = "dd/MM/yyyy"; //

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        return sdf.format(date);
    }

    public static Date parseDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
            return sdf.parse(dateString);
        } catch (ParseException e) {
            return null; // Retorna null si la fecha no es válida
        }
    }
}
