package com.encora.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationUtils {

    /**
     * Cast a duration ISO 8601
     * To a format "1d 1h 1m".
     */

    public static String formatDuration(String isoDuration) {
        if (isoDuration == null || isoDuration.isEmpty()) {
            return "";
        }

        Pattern pattern = Pattern.compile("P(?:(\\d+)D)?(?:T(?:(\\h+)H)?(?:(\\m+)M)?)?");
        Matcher matcher = pattern.matcher(isoDuration);

        int days = 0, hours = 0, minutes = 0;

        if (matcher.matches()) {
            if (matcher.group(1) != null) days = Integer.parseInt(matcher.group(1));
            if (matcher.group(2) != null) hours = Integer.parseInt(matcher.group(2));
            if (matcher.group(3) != null) minutes = Integer.parseInt(matcher.group(3));
        }

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m");

        return sb.toString().trim();
    }
}
