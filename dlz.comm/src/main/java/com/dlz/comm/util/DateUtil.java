package com.dlz.comm.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

@Slf4j
public class DateUtil {
    public static DateFormatter formatter(String pattern) {
        return new DateFormatter(pattern);
    }
    public static class DateFormatter {
        public final String pattern;
        public final DateTimeFormatter format;
        private final DateFormat dateFormat;

        private DateFormatter(String pattern) {
            this.pattern = pattern;
            this.format = DateTimeFormatter.ofPattern(pattern);
            this.dateFormat = DateFormat.of(pattern);
        }
        public Date parse(String dateStr) {
            try {
                return dateFormat.parse(dateStr);
            } catch (ParseException e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }
            return null;
        }
        public LocalDateTime parse2LocalDate(String dateStr) {
           return LocalDateTime.parse(dateStr, format);
        }
        /**
         * 格式化日期
         * @return
         */
        public String toString() {
            return pattern;
        }
        /**
         * 格式化日期
         *
         * @param date
         * @return
         */
        public String format(Date date) {
            if (date == null) {
                return "";
            }
            return dateFormat.format(date);
        }
        /**
         * 格式化日期
         *
         * @param date
         * @return
         */
        public String format(TemporalAccessor date) {
            if (date == null) {
                return "";
            }
            return format.format(date);
        }

        public String formatNow() {
            return format(DateUtil.now());
        }
    }

    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final DateFormatter YYYY = formatter("yyyy");
    public static DateFormatter MONTH = formatter("yyyy-MM");
    public static DateFormatter MONTH_MINI = formatter("yyyyMM");
    public static DateFormatter TIME = formatter("HH:mm:ss");
    public static DateFormatter DATE = formatter("yyyy-MM-dd");
    public static DateFormatter DATE_MINI = formatter("yyyyMMdd");
    public static DateFormatter DATETIME_MINI = formatter("yyyyMMddHHmmss");
    public static final DateFormatter DATETIME = formatter(PATTERN_DATETIME);
    private final static VAL<String, Pattern>[] date_trans = new VAL[]{
            VAL.of("yyyy-MM-dd HH:mm:ss", Pattern.compile("^\\d{4}-[0,1]?\\d-[0-3]?\\d \\d{2}:\\d{2}:\\d{2}.*")),
            VAL.of("yyyy-MM-dd", Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}$")),
            VAL.of("yyyy-MM", Pattern.compile("^\\d{4}-\\d{1,2}$")),
            VAL.of("yyyy-MM-dd HH:mm", Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}$")),
            VAL.of("yyyy年MM月dd日 HH时mm分ss秒", Pattern.compile("^\\d{4}年[0,1]?\\d月[0-3]?\\d日 \\d{2}时\\d{2}分\\d{2}秒"))
    };

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
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        if (date == null) {
            return "";
        }
        return format.format(date);
    }

    public static String formatNow(final String format) {
        return format(now(), format);
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        // 设置时区UTC
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return formatter.parse(dateVal);
        } catch (ParseException e) {
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
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
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
        if (input2.matches("^\\d{1,2}:\\d{1,2}$")) {
            return parse(DateUtil.getDateStr() + " " + input2, "yyyy-MM-dd HH:mm");
        } else if (input2.matches("^\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parse(DateUtil.getDateStr() + " " + input2, "yyyy-MM-dd HH:mm:ss");
        } else if (input2.matches("^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{3}Z$")) {
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
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}