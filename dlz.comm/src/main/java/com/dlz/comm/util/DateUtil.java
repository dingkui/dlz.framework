package com.dlz.comm.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

@Slf4j
public class DateUtil {
    public static String format(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        if (date == null) {
            return "";
        }
        return format.format(date);
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

    /**
     * getDate get a string with format YYYY-MM-DD from a Date object
     *
     * @param date date
     * @return String
     */
    public static String getDateStr(Date date) {
        return format(date,"yyyy-MM-dd");
    }

    public static String getDateStr() {
        return format(new Date(),"yyyy-MM-dd");
    }

    /**
     * getDateStr get a string with format YYYY-MM-DD HH:mm:ss from a Date object
     *
     * @param date date
     * @return String
     */
    static public String getDateTimeStr(Date date) {
        return format(date,"yyyy-MM-dd HH:mm:ss");
    }

    static public String getDateTimeStr() {
        return format(new Date(),"yyyy-MM-dd HH:mm:ss");
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

	private final static KV<String,Pattern>[] date_trans = new KV[]{
		new KV("yyyy-MM-dd HH:mm:ss",Pattern.compile("^\\d{4}-[0,1]?\\d-[0-3]?\\d \\d{2}:\\d{2}:\\d{2}.*")),
		new KV("yyyy-MM-dd",Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}$")),
		new KV("yyyy-MM",Pattern.compile("^\\d{4}-\\d{1,2}$")),
		new KV("yyyy-MM-dd HH:mm",Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}$")),
		new KV("yyyy年MM月dd日 HH时mm分ss秒",Pattern.compile("^\\d{4}年[0,1]?\\d月[0-3]?\\d日 \\d{2}时\\d{2}分\\d{2}秒"))
	};

	public static Date getDateStr(String input, String format){
		if(input==null){
			return null;
		}
		if(format!=null){
			return parse(input,format);
		}
		String input2 = input.replaceAll("/", "-").replaceAll("\"", "");
		for (int i = 0; i < date_trans.length; i++) {
			if(date_trans[i].v.matcher(input2).matches()){
				return parse(input2,date_trans[i].k);
			}
		}
		if (input2.matches("^\\d{1,2}:\\d{1,2}$")) {
			return parse(DateUtil.getDateStr()+ " " + input2, "yyyy-MM-dd HH:mm");
		} else if (input2.matches("^\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
			return parse(DateUtil.getDateStr()+ " " + input2, "yyyy-MM-dd HH:mm:ss");
		}else if(input2.matches("^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{3}Z$")){
			return parseUTCDate(input2);
		}
		log.error("转换日期格式未识别："+input);
		return null;
	}

	public static long getDateline() {
		return System.currentTimeMillis()/1000;
	}
}