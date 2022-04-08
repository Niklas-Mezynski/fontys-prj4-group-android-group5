package org.die6sheeshs.projectx.helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InputVerification {

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

    public static void dateWithoutTime(String s) throws IllegalUserInputException {
        try {
            LocalDateTime birthday = LocalDateTime.parse(s + " 08:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (Exception e) {
            throw new IllegalUserInputException("Invalid date format");
        }
    }
}
