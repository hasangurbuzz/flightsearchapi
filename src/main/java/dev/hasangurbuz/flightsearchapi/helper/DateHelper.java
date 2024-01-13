package dev.hasangurbuz.flightsearchapi.helper;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class DateHelper {
    public static Date toUTC(LocalDate date) {
        if (date == null) return null;
        return Date.from(date.atStartOfDay().toInstant(ZoneOffset.UTC));
    }

    public static OffsetDateTime toUTC(OffsetDateTime date) {
        if (date == null) return null;
        return date.withOffsetSameInstant(ZoneOffset.UTC);
    }

    public static boolean isDateAfterThanCurrent(OffsetDateTime dateTime) {
        return dateTime.isAfter(OffsetDateTime.now());
    }
}
