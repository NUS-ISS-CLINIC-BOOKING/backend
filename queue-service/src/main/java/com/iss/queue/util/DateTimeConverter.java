package com.iss.queue.util;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 日期时间转换工具类
 */
public final class DateTimeConverter {

    // 私有构造方法，禁止实例化
    private DateTimeConverter() {}

    // 默认日期格式（yyyy-MM-dd）
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // 默认时间格式（HH:mm）
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * 将日期字符串（yyyy-MM-dd）和时间字符串（HH:mm）合并为 Timestamp
     * @param dateStr 日期字符串（如 "2023-11-02"）
     * @param timeStr 时间字符串（如 "11:00"）
     * @return java.sql.Timestamp（可直接用于 MySQL datetime 查询）
     * @throws IllegalArgumentException 如果日期或时间格式非法
     */
    public static Timestamp toTimestamp(String dateStr, String timeStr) {
        try {
            // 1. 解析日期和时间
            LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
            LocalTime time = LocalTime.parse(timeStr, TIME_FORMATTER);

            // 2. 合并为 LocalDateTime
            LocalDateTime dateTime = LocalDateTime.of(date, time);

            // 3. 转换为 Timestamp
            return Timestamp.valueOf(dateTime);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid date or time format. Expected date=yyyy-MM-dd, time=HH:mm. " +
                            "Actual: date=" + dateStr + ", time=" + timeStr
            );
        }
    }

    /**
     * 快速验证日期和时间格式是否合法
     */
    public static boolean isValidDateTime(String dateStr, String timeStr) {
        try {
            LocalDate.parse(dateStr, DATE_FORMATTER);
            LocalTime.parse(timeStr, TIME_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}