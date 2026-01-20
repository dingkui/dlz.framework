package com.dlz.comm.util;

import com.dlz.comm.consts.Str;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期计算工具类
 *
 * 提供便捷的日期时间计算功能，包括加减年月日时分秒等操作
 * 同时提供日期差值计算、时间戳获取等功能
 * 
 * @author dk
 * @since 2023
 */
public class DateCalcUtil {
	/**
	 * 为指定日期添加年数
	 *
	 * @param date       原始日期
	 * @param yearsToAdd 要添加的年数，负数表示减少
	 * @return 新的日期
	 */
	public static Date plusYears(Date date, int yearsToAdd) {
		return set(date, Calendar.YEAR, yearsToAdd);
	}

	/**
	 * 为指定日期添加月数
	 *
	 * @param date        原始日期
	 * @param monthsToAdd 要添加的月数，负数表示减少
	 * @return 新的日期
	 */
	public static Date plusMonths(Date date, int monthsToAdd) {
		return set(date, Calendar.MONTH, monthsToAdd);
	}

	/**
	 * 为指定日期添加周数
	 *
	 * @param date       原始日期
	 * @param weeksToAdd 要添加的周数，负数表示减少
	 * @return 新的日期
	 */
	public static Date plusWeeks(Date date, int weeksToAdd) {
		return plus(date, Period.ofWeeks(weeksToAdd));
	}

	/**
	 * 为指定日期添加天数
	 *
	 * @param date      原始日期
	 * @param daysToAdd 要添加的天数，负数表示减少
	 * @return 新的日期
	 */
	public static Date plusDays(Date date, long daysToAdd) {
		return plus(date, Duration.ofDays(daysToAdd));
	}

	/**
	 * 为指定日期添加小时数
	 *
	 * @param date       原始日期
	 * @param hoursToAdd 要添加的小时数，负数表示减少
	 * @return 新的日期
	 */
	public static Date plusHours(Date date, long hoursToAdd) {
		return plus(date, Duration.ofHours(hoursToAdd));
	}

	/**
	 * 为指定日期添加分钟数
	 *
	 * @param date         原始日期
	 * @param minutesToAdd 要添加的分钟数，负数表示减少
	 * @return 新的日期
	 */
	public static Date plusMinutes(Date date, long minutesToAdd) {
		return plus(date, Duration.ofMinutes(minutesToAdd));
	}

	/**
	 * 为指定日期添加秒数
	 *
	 * @param date         原始日期
	 * @param secondsToAdd 要添加的秒数，负数表示减少
	 * @return 新的日期
	 */
	public static Date plusSeconds(Date date, long secondsToAdd) {
		return plus(date, Duration.ofSeconds(secondsToAdd));
	}

	/**
	 * 为指定日期添加毫秒数
	 *
	 * @param date        原始日期
	 * @param millisToAdd 要添加的毫秒数，负数表示减少
	 * @return 新的日期
	 */
	public static Date plusMillis(Date date, long millisToAdd) {
		return plus(date, Duration.ofMillis(millisToAdd));
	}

	/**
	 * 为指定日期添加纳秒数
	 *
	 * @param date       原始日期
	 * @param nanosToAdd 要添加的纳秒数，负数表示减少
	 * @return 新的日期
	 */
	public static Date plusNanos(Date date, long nanosToAdd) {
		return plus(date, Duration.ofNanos(nanosToAdd));
	}

	/**
	 * 为指定日期添加时间量
	 *
	 * @param date   原始日期
	 * @param amount 要添加的时间量
	 * @return 新的日期
	 */
	public static Date plus(Date date, TemporalAmount amount) {
		Instant instant = date.toInstant();
		return Date.from(instant.plus(amount));
	}

	/**
	 * 从指定日期减少年数
	 *
	 * @param date  原始日期
	 * @param years 要减少的年数，负数表示增加
	 * @return 新的日期
	 */
	public static Date minusYears(Date date, int years) {
		return set(date, Calendar.YEAR, -years);
	}

	/**
	 * 从指定日期减少月数
	 *
	 * @param date   原始日期
	 * @param months 要减少的月数，负数表示增加
	 * @return 新的日期
	 */
	public static Date minusMonths(Date date, int months) {
		return set(date, Calendar.MONTH, -months);
	}

	/**
	 * 从指定日期减少周数
	 *
	 * @param date  原始日期
	 * @param weeks 要减少的周数，负数表示增加
	 * @return 新的日期
	 */
	public static Date minusWeeks(Date date, int weeks) {
		return minus(date, Period.ofWeeks(weeks));
	}

	/**
	 * 从指定日期减少天数
	 *
	 * @param date 原始日期
	 * @param days 要减少的天数，负数表示增加
	 * @return 新的日期
	 */
	public static Date minusDays(Date date, long days) {
		return minus(date, Duration.ofDays(days));
	}

	/**
	 * 从指定日期减少小时数
	 *
	 * @param date  原始日期
	 * @param hours 要减少的小时数，负数表示增加
	 * @return 新的日期
	 */
	public static Date minusHours(Date date, long hours) {
		return minus(date, Duration.ofHours(hours));
	}

	/**
	 * 从指定日期减少分钟数
	 *
	 * @param date    原始日期
	 * @param minutes 要减少的分钟数，负数表示增加
	 * @return 新的日期
	 */
	public static Date minusMinutes(Date date, long minutes) {
		return minus(date, Duration.ofMinutes(minutes));
	}

	/**
	 * 从指定日期减少秒数
	 *
	 * @param date    原始日期
	 * @param seconds 要减少的秒数，负数表示增加
	 * @return 新的日期
	 */
	public static Date minusSeconds(Date date, long seconds) {
		return minus(date, Duration.ofSeconds(seconds));
	}

	/**
	 * 从指定日期减少毫秒数
	 *
	 * @param date   原始日期
	 * @param millis 要减少的毫秒数，负数表示增加
	 * @return 新的日期
	 */
	public static Date minusMillis(Date date, long millis) {
		return minus(date, Duration.ofMillis(millis));
	}

	/**
	 * 从指定日期减少纳秒数
	 *
	 * @param date  原始日期
	 * @param nanos 要减少的纳秒数，负数表示增加
	 * @return 新的日期
	 */
	public static Date minusNanos(Date date, long nanos) {
		return minus(date, Duration.ofNanos(nanos));
	}

	/**
	 * 从指定日期减少时间量
	 *
	 * @param date   原始日期
	 * @param amount 要减少的时间量
	 * @return 新的日期
	 */
	public static Date minus(Date date, TemporalAmount amount) {
		Instant instant = date.toInstant();
		return Date.from(instant.minus(amount));
	}

	/**
	 * 设置日期属性
	 *
	 * @param date          原始日期
	 * @param calendarField 要修改的日历字段
	 * @param amount        修改的数量，负数表示减少
	 * @return 新的日期
	 */
	private static Date set(Date date, int calendarField, int amount) {
		Calendar c = Calendar.getInstance();
		c.setLenient(false);
		c.setTime(date);
		c.add(calendarField, amount);
		return c.getTime();
	}



	/**
	 * 计算两个时间点之间的时间差（适用于较小时间跨度）
	 *
	 * @param startInclusive 开始时间（包含）
	 * @param endExclusive   结束时间（排除）
	 * @return 时间差
	 */
	public static Duration between(Temporal startInclusive, Temporal endExclusive) {
		return Duration.between(startInclusive, endExclusive);
	}

	/**
	 * 计算两个日期之间的时间差（适用于较大时间跨度，以年月日为单位）
	 *
	 * @param startDate 开始日期
	 * @param endDate   结束日期
	 * @return 时间差
	 */
	public static Period between(LocalDate startDate, LocalDate endDate) {
		return Period.between(startDate, endDate);
	}

	/**
	 * 计算两个日期之间的时间差
	 *
	 * @param startDate 开始日期
	 * @param endDate   结束日期
	 * @return 时间差
	 */
	public static Duration between(Date startDate, Date endDate) {
		Duration between = Duration.between(startDate.toInstant(), endDate.toInstant());
		return between;
	}

	/**
	 * 将秒数转换为天时分秒格式
	 *
	 * @param second 秒数
	 * @return 格式化的天时分秒字符串
	 */
	public static String secondToTime(Long second) {
		// 判断是否为空
		if (second == null || second == 0L) {
			return  Str.EMPTY;
		}
		//转换天数
		long days = second / 86400;
		//剩余秒数
		second = second % 86400;
		//转换小时
		long hours = second / 3600;
		//剩余秒数
		second = second % 3600;
		//转换分钟
		long minutes = second / 60;
		//剩余秒数
		second = second % 60;
		if (days > 0) {
			return StringUtils.formatMsg("{}天{}小时{}分{}秒", days, hours, minutes, second);
		} else {
			return StringUtils.formatMsg("{}小时{}分{}秒", hours, minutes, second);
		}
	}

	/**
	 * 获取当前时间戳（秒）
	 *
	 * @return 当前时间戳（秒）
	 */
	public static long dateline() {
		return System.currentTimeMillis() / 1000;
	}


	/**
	 * 获取服务器启动时间
	 * 
	 * @return 服务器启动时间
	 */
	public static Date getServerStartDate() {
		long time = ManagementFactory.getRuntimeMXBean().getStartTime();
		return new Date(time);
	}

	/**
	 * 计算两个日期相差的天数（绝对值）
	 * 
	 * @param date1 第一个日期
	 * @param date2 第二个日期
	 * @return 相差的天数
	 */
	public static int differentDaysByMillisecond(Date date1, Date date2) {
		return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
	}

	/**
	 * 计算两个日期之间的时间差，并格式化为天时分字符串
	 * 
	 * @param endDate 结束日期
	 * @param nowDate 起始日期
	 * @return 格式化的时间差字符串
	 */
	public static String getDatePoor(Date endDate, Date nowDate) {
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - nowDate.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		// 计算差多少秒//输出结果
		// long sec = diff % nd % nh % nm / ns;
		return day + "天" + hour + "小时" + min + "分钟";
	}
}