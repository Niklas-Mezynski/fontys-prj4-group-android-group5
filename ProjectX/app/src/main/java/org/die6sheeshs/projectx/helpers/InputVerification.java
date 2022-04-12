package org.die6sheeshs.projectx.helpers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class InputVerification {
    private static List<String> formatStrings = Arrays.asList("M/d/y", "M-d-y", "M.d.y",
            "y/M/d", "y-M-d", "y.M.d",
            "d.M.y", "d-M-y", "d/M/y");

    public static void stringToInt(String s) throws IllegalUserInputException {
        try {
            Integer.parseInt(s);
        } catch (Exception e) {
            throw new IllegalUserInputException("Not a valid Integer");
        }
    }

    public static void stringNotEmpty(String s) throws IllegalUserInputException {
        if (s == null || s.isEmpty()) {
            throw new IllegalUserInputException("Empty string");
        }
    }

    public static LocalDateTime dateWithoutTime(String s) throws IllegalUserInputException {
        LocalDateTime date = null;
        boolean valid = false;

        for (String formatString : formatStrings) {
            try {
//            LocalDateTime birthday = LocalDateTime.parse(s + " 08:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                date = LocalDate.parse(s, DateTimeFormatter.ofPattern(formatString)).atStartOfDay();
                valid = true;
                break;
            } catch (Exception e) {
//
            }
        }
        if (!valid) {
            throw new IllegalUserInputException("Invalid date format");
        }
        return date;
    }
}
