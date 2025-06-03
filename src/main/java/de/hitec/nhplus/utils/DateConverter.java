package de.hitec.nhplus.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for converting between {@link String}, {@link LocalDate} and {@link LocalTime}
 * using fixed date and time formats.
 */

public class DateConverter {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm";

    /**
     * Converts a date string into a {@link LocalDate} object.
     *
     * @param date the date string to convert (format: yyyy-MM-dd)
     * @return a {@link LocalDate} representing the given date string
     */

    public static LocalDate convertStringToLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    /**
     * Converts a time string into a {@link LocalTime} object.
     *
     * @param time the time string to convert (format: HH:mm)
     * @return a {@link LocalTime} representing the given time string
     */

    public static LocalTime convertStringToLocalTime(String time) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

    /**
     * Converts a {@link LocalDate} into a formatted string.
     *
     * @param date the {@link LocalDate} to convert
     * @return the date as a string in the format yyyy-MM-dd
     */

    public static String convertLocalDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static String convertLocalTimeToString(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

}
