package com.dlz.comm.util;

import com.dlz.comm.exception.SystemException;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalQuery;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

@Slf4j
public class DateUtil {
    public static final String PATTERN_YEAR = "yyyy";
    public static final String PATTERN_MONTH = "yyyy-MM";
    public static final String PATTERN_MONTH_MINI = "yyyyMM";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_DATE_MINI = "yyyyMMdd";
    public static final String PATTERN_TIME = "HH:mm:ss";
    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATETIME_MINI = "yyyyMMddHHmmss";
    public static final String PATTERN_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final DateFormat YEAR = DateFormat.of(PATTERN_YEAR);
    public static final DateFormat MONTH = DateFormat.of(PATTERN_MONTH);
    public static final DateFormat MONTH_MINI = DateFormat.of(PATTERN_MONTH_MINI);
    public static final DateFormat DATE = DateFormat.of(PATTERN_DATE);
    public static final DateFormat DATE_MINI = DateFormat.of(PATTERN_DATE_MINI);
    public static final DateFormat TIME = DateFormat.of(PATTERN_TIME);
    public static final DateFormat DATETIME = DateFormat.of(PATTERN_DATETIME);
    public static final DateFormat DATETIME_MINI = DateFormat.of(PATTERN_DATETIME_MINI);
    public static final DateFormat UTC = DateFormat.of(PATTERN_UTC,TimeZone.getTimeZone("UTC"));

    public static Map<String, DateFormat> formatterMap=new HashMap<>();
    static {
        formatterMap.put(PATTERN_YEAR, YEAR);
        formatterMap.put(PATTERN_MONTH, MONTH);
        formatterMap.put(PATTERN_MONTH_MINI, MONTH_MINI);
        formatterMap.put(PATTERN_DATE, DATE);
        formatterMap.put(PATTERN_DATE_MINI, DATE_MINI);
        formatterMap.put(PATTERN_TIME, TIME);
        formatterMap.put(PATTERN_DATETIME, DATETIME);
        formatterMap.put(PATTERN_DATETIME_MINI, DATETIME_MINI);
        formatterMap.put(PATTERN_UTC, UTC);
    }

    private final static VAL<String, Pattern>[] date_trans = new VAL[]{
            VAL.of("yyyy-MM-dd HH:mm:ss", Pattern.compile("^\\d{4}-[0,1]?\\d-[0-3]?\\d \\d{2}:\\d{2}:\\d{2}.*")),
            VAL.of("yyyy-MM-dd", Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}$")),
            VAL.of("yyyy-MM", Pattern.compile("^\\d{4}-\\d{1,2}$")),
            VAL.of("yyyy-MM-dd HH:mm", Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}$")),
            VAL.of("yyyy年MM月dd日 HH时mm分ss秒", Pattern.compile("^\\d{4}年[0,1]?\\d月[0-3]?\\d日 \\d{2}时\\d{2}分\\d{2}秒"))
    };
    // 提取为静态常量，避免重复编译
    private static final Pattern TIME_PATTERN_1 = Pattern.compile("^\\d{1,2}:\\d{1,2}$");
    private static final Pattern TIME_PATTERN_2 = Pattern.compile("^\\d{1,2}:\\d{1,2}:\\d{1,2}$");
    private static final Pattern UTC_PATTERN = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{3}Z$");

    public static DateFormat formatter(String pattern) {
        final DateFormat dateFormatter = formatterMap.get(pattern);
        return dateFormatter!=null?dateFormatter:DateFormat.of(pattern);
    }
    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        return formatter(pattern).format(date);
    }

    public static String formatNow(final String format) {
        return formatter(format).formatNow();
    }


    /**
     * getDate get a string with format YYYY-MM-DD from a Date object
     *
     * @param date date
     * @return String
     */
    public static String getDateStr(Date date) {
        return DATE.format(date);
    }

    public static String getDateStr() {
        return DATE.formatNow();
    }

    /**
     * getDateStr get a string with format YYYY-MM-DD HH:mm:ss from a Date object
     *
     * @param date date
     * @return String
     */
    public static String getDateTimeStr(Date date) {
        return DATETIME.format(date);
    }

    public static String getDateTimeStr() {
        return DATETIME.formatNow();
    }

    public static Date parseUTCDate(String dateVal) {
        try {
            return UTC.parse(dateVal);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @param pattern 表达式
     * @return 时间
     */
    public static <T> T parse(String dateStr, String pattern, TemporalQuery<T> query) {
        return DateTimeFormatter.ofPattern(pattern).parse(dateStr, query);
    }
    private static Date parse(String dateStr, String pattern) {
        try {
            return DateUtil.formatter(pattern).parse(dateStr);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }
    private static LocalDateTime parseLocalDateTime(String dateStr, String pattern) {
        try {
            return DateUtil.formatter(pattern).parse2LocalDate(dateStr);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    public static Date getDate(String input) {
        if (input == null) {
            return null;
        }
        String input2 = input.replaceAll("/", "-").replaceAll("\"", "");
        for (int i = 0; i < date_trans.length; i++) {
            if (date_trans[i].v2.matcher(input2).matches()) {
                return parse(input2, date_trans[i].v1);
            }
        }
        if (TIME_PATTERN_1.matcher(input2).matches()) {
            return DATETIME.parse(DateUtil.getDateStr() + " " + input2+":00");
        } else if (TIME_PATTERN_2.matcher(input2).matches()) {
            return DATETIME.parse(DateUtil.getDateStr() + " " + input2);
        } else if (UTC_PATTERN.matcher(input2).matches()) {
            return parseUTCDate(input2);
        }
        log.error("转换日期格式未识别：" + input);
        return null;
    }

    public static Date getDate(String input, String format) {
        if (input == null) {
            return null;
        }
        if (format != null) {
            return parse(input, format);
        }
        return getDate(input);
    }
    /**
     * 转换成 date
     *
     * @param dateTime LocalDateTime
     * @return Date
     */
    public static Date getDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    /**
     * 转换成 date
     *
     * @param localDate LocalDate
     * @return Date
     */
    public static Date getDate(final LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime getLocalDateTime(String input, String format) {
        if (input == null) {
            return null;
        }
        if (format != null) {
            return formatter(format).parse2LocalDate(input);
        }
        return getLocalDateTime(input);
    }

    public static LocalDateTime getLocalDateTime(Date input) {
        if (input == null) {
            return null;
        }
        return input.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    public static LocalDateTime getLocalDateTime(String input) {
        if (input == null) {
            return null;
        }
        String input2 = input.replaceAll("/", "-").replaceAll("\"", "");
        for (int i = 0; i < date_trans.length; i++) {
            if (date_trans[i].v2.matcher(input2).matches()) {
                return DateUtil.formatter(date_trans[i].v1).parse2LocalDate(input2);
            }
        }
        if (TIME_PATTERN_1.matcher(input2).matches()) {
            return DateUtil.DATETIME.parse2LocalDate(DateUtil.getDateStr() + " " + input2+":00");
        } else if (TIME_PATTERN_2.matcher(input2).matches()) {
            return DateUtil.DATETIME.parse2LocalDate(DateUtil.getDateStr() + " " + input2);
        } else if (UTC_PATTERN.matcher(input2).matches()) {
            return parseUTCLocalDateTime(input2);
        }
        log.error("转换日期格式未识别：" + input);
        return null;
    }
    /**
     * 将符合 ISO 8601 格式的 UTC 时间字符串转换为 LocalDateTime（使用系统默认时区）
     *
     * @param utcTimeStr UTC 时间字符串，如 "2023-12-25T14:30:00.000Z"
     * @return 解析后的 LocalDateTime 对象，失败返回 null
     */
    public static LocalDateTime parseUTCLocalDateTime(String utcTimeStr) {
        try {
            // 将字符串解析为 Instant（UTC 时间）
            Instant instant = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(utcTimeStr));
            // 转换为系统默认时区的 LocalDateTime
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        } catch (DateTimeParseException e) {
            // 可选：记录日志
            log.error(ExceptionUtils.getStackTrace(new SystemException("无法解析 UTC 时间字符串: " + utcTimeStr,e)));
            return null;
        }
    }

}