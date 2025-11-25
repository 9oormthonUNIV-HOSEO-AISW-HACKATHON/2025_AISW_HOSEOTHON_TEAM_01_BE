package com._oormthonUNIV.newnew.global.util;


import java.time.*;
import java.util.Date;


public class TimeUtil {

    public static LocalDateTime getNowSeoulLocalDateTime() {
        Instant instant = Instant.now();
        ZonedDateTime zSeoul = instant.atZone(ZoneId.of("Asia/Seoul"));
        return zSeoul.toLocalDateTime();
    }

    public static Instant getNowSeoulInstant() {
        Instant instant = Instant.now();
        ZonedDateTime zSeoul = instant.atZone(ZoneId.of("Asia/Seoul"));
        return zSeoul.toInstant();
    }


    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) return null;
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul") );
    }

    public static long toEpochMilli(LocalDateTime ldt) {
        ZoneId zone = ZoneId.of("Asia/Seoul");
        if (ldt == null) throw new IllegalArgumentException("ldt and zone must not be null");
        return ldt.atZone(zone).toInstant().toEpochMilli();
    }

    public static String toRelativeTime(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);
        long minutes = duration.toMinutes();
        if (minutes < 1) return "방금 전";
        if (minutes < 60) return minutes + "분 전";
        long hours = duration.toHours();
        if (hours < 24) return hours + "시간 전";
        long days = duration.toDays();
        if (days < 7) return days + "일 전";
        if (days < 30) return (days / 7) + "주 전";
        if (days < 365) return (days / 30) + "개월 전";
        return (days / 365) + "년 전";
    }

    public static String toLatestTime(LocalDateTime createdAt) {
        if (createdAt == null) return null;

        Duration d = Duration.between(createdAt, LocalDateTime.now());

        long minutes = d.toMinutes();
        long hours = d.toHours();
        long days = d.toDays();

        if (minutes < 1) return "방금 전";
        if (hours < 1) return minutes + "분 전";
        if (days < 1) return hours + "시간 전";
        return days + "일 전";
    }
}
