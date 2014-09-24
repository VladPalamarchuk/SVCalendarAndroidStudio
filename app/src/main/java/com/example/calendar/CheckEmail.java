package com.example.calendar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckEmail {
    public static final Pattern pattern = Pattern
            .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    public static boolean Check(String word) {
        Matcher matcher = pattern.matcher(word);
        if (matcher.matches())
            return true;
        else
            return false;
    }
}