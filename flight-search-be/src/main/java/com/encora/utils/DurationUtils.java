package com.encora.utils;

public class DurationUtils {

    /**
     * Cast a duration ISO 8601
     * To a format "1d 1h 1m".
     */

    public static String formatDuration(String isoDuration) {
        if (isoDuration == null || isoDuration.isEmpty()) {
            return "";
        }

        int days = 0, hours = 0, minutes = 0;

        String duration = isoDuration.substring(1);

        if (duration.contains("D")) {
            String[] parts = duration.split("D");
            days = Integer.parseInt(parts[0]);
            duration = parts[1]; 
        }

        if (duration.startsWith("T")) {
            duration = duration.substring(1);
        }

        if (duration.contains("H")) {
            int indexH = duration.indexOf("H");
            hours = Integer.parseInt(duration.substring(0, indexH));
            duration = duration.substring(indexH + 1);
        }

        if (duration.contains("M")) {
            int indexM = duration.indexOf("M");
            minutes = Integer.parseInt(duration.substring(0, indexM));
        }
        
        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m");

        return sb.toString().trim();
    }
}
