package com.vtidc.mymail.ultis;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DatetimeUtils {

    public static String convertDateTime(String dateStr) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss.SSSX");

        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateStr, inputFormatter);

        ZonedDateTime utcPlus7Time = zonedDateTime.withZoneSameInstant(ZoneOffset.ofHours(7));

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return utcPlus7Time.format(outputFormatter);
    }

}
