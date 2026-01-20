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
 * 日期工具类
 *
 * @author dk
 */
public class DateCalcUtil {
	/**
	 * 添加年
	 *
	 * @param date       时间
	 * @param yearsToAdd 添加的年数
	 * @return 设置后的时间
	 */
	public static Date plusYears(Date date, int yearsToAdd) {
		return set(date, Calendar.YEAR, yearsToAdd);
	}

	/**
	 * 添加月
	 *
	 * @param date        时间
	 * @param monthsToAdd 添加的月数
	 * @return 设置后的时间
	 */
	public static Date plusMonths(Date date, int monthsToAdd) {
		return set(date, Calendar.MONTH, monthsToAdd);
	}

	/**
	 * 添加周
	 *
	 * @param date       时间
	 * @param weeksToAdd 添加的周数
	 * @return 设置后的时间
	 */
	public static Date plusWeeks(Date date, int weeksToAdd) {
		return plus(date, Period.ofWeeks(weeksToAdd));
	}

	/**
	 * 添加天
	 *
	 * @param date      时间
	 * @param daysToAdd 添加的天数
	 * @return 设置后的时间
	 */
	public static Date plusDays(Date date, long daysToAdd) {
		return plus(date, Duration.ofDays(daysToAdd));
	}

	/**
	 * 添加小时
	 *
	 * @param date       时间
	 * @param hoursToAdd 添加的小时数
	 * @return 设置后的时间
	 */
	public static Date plusHours(Date date, long hoursToAdd) {
		return plus(date, Duration.ofHours(hoursToAdd));
	}

	/**
	 * 添加分钟
	 *
	 * @param date         时间
	 * @param minutesToAdd 添加的分钟数
	 * @return 设置后的时间
	 */
	public static Date plusMinutes(Date date, long minutesToAdd) {
		return plus(date, Duration.ofMinutes(minutesToAdd));
	}

	/**
	 * 添加秒
	 *
	 * @param date         时间
	 * @param secondsToAdd 添加的秒数
	 * @return 设置后的时间
	 */
	public static Date plusSeconds(Date date, long secondsToAdd) {
		return plus(date, Duration.ofSeconds(secondsToAdd));
	}

	/**
	 * 添加毫秒
	 *
	 * @param date        时间
	 * @param millisToAdd 添加的毫秒数
	 * @return 设置后的时间
	 */
	public static Date plusMillis(Date date, long millisToAdd) {
		return plus(date, Duration.ofMillis(millisToAdd));
	}

	/**
	 * 添加纳秒
	 *
	 * @param date       时间
	 * @param nanosToAdd 添加的纳秒数
	 * @return 设置后的时间
	 */
	public static Date plusNanos(Date date, long nanosToAdd) {
		return plus(date, Duration.ofNanos(nanosToAdd));
	}

	/**
	 * 日期添加时间量
	 *
	 * @param date   时间
	 * @param amount 时间量
	 * @return 设置后的时间
	 */
	public static Date plus(Date date, TemporalAmount amount) {
		Instant instant = date.toInstant();
		return Date.from(instant.plus(amount));
	}

	/**
	 * 减少年
	 *
	 * @param date  时间
	 * @param years 减少的年数
	 * @return 设置后的时间
	 */
	public static Date minusYears(Date date, int years) {
		return set(date, Calendar.YEAR, -years);
	}

	/**
	 * 减少月
	 *
	 * @param date   时间
	 * @param months 减少的月数
	 * @return 设置后的时间
	 */
	public static Date minusMonths(Date date, int months) {
		return set(date, Calendar.MONTH, -months);
	}

	/**
	 * 减少周
	 *
	 * @param date  时间
	 * @param weeks 减少的周数
	 * @return 设置后的时间
	 */
	public static Date minusWeeks(Date date, int weeks) {
		return minus(date, Period.ofWeeks(weeks));
	}

	/**
	 * 减少天
	 *
	 * @param date 时间
	 * @param days 减少的天数
	 * @return 设置后的时间
	 */
	public static Date minusDays(Date date, long days) {
		return minus(date, Duration.ofDays(days));
	}

	/**
	 * 减少小时
	 *
	 * @param date  时间
	 * @param hours 减少的小时数
	 * @return 设置后的时间
	 */
	public static Date minusHours(Date date, long hours) {
		return minus(date, Duration.ofHours(hours));
	}

	/**
	 * 减少分钟
	 *
	 * @param date    时间
	 * @param minutes 减少的分钟数
	 * @return 设置后的时间
	 */
	public static Date minusMinutes(Date date, long minutes) {
		return minus(date, Duration.ofMinutes(minutes));
	}

	/**
	 * 减少秒
	 *
	 * @param date    时间
	 * @param seconds 减少的秒数
	 * @return 设置后的时间
	 */
	public static Date minusSeconds(Date date, long seconds) {
		return minus(date, Duration.ofSeconds(seconds));
	}

	/**
	 * 减少毫秒
	 *
	 * @param date   时间
	 * @param millis 减少的毫秒数
	 * @return 设置后的时间
	 */
	public static Date minusMillis(Date date, long millis) {
		return minus(date, Duration.ofMillis(millis));
	}

	/**
	 * 减少纳秒
	 *
	 * @param date  时间
	 * @param nanos 减少的纳秒数
	 * @return 设置后的时间
	 */
	public static Date minusNanos(Date date, long nanos) {
		return minus(date, Duration.ofNanos(nanos));
	}

	/**
	 * 日期减少时间量
	 *
	 * @param date   时间
	 * @param amount 时间量
	 * @return 设置后的时间
	 */
	public static Date minus(Date date, TemporalAmount amount) {
		Instant instant = date.toInstant();
		return Date.from(instant.minus(amount));
	}

	/**
	 * 设置日期属性
	 *
	 * @param date          时间
	 * @param calendarField 更改的属性
	 * @param amount        更改数，-1表示减少
	 * @return 设置后的时间
	 */
	private static Date set(Date date, int calendarField, int amount) {
		Calendar c = Calendar.getInstance();
		c.setLenient(false);
		c.setTime(date);
		c.add(calendarField, amount);
		return c.getTime();
	}



	/**
	 * 比较2个时间差，跨度比较小
	 *
	 * @param startInclusive 开始时间
	 * @param endExclusive   结束时间
	 * @return 时间间隔
	 */
	public static Duration between(Temporal startInclusive, Temporal endExclusive) {
		return Duration.between(startInclusive, endExclusive);
	}

	/**
	 * 比较2个时间差，跨度比较大，年月日为单位
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @return 时间间隔
	 */
	public static Period between(LocalDate startDate, LocalDate endDate) {
		return Period.between(startDate, endDate);
	}

	/**
	 * 比较2个 时间差
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @return 时间间隔
	 */
	public static Duration between(Date startDate, Date endDate) {
		Duration between = Duration.between(startDate.toInstant(), endDate.toInstant());
		return between;
	}

	/**
	 * 将秒数转换为日时分秒
	 *
	 * @param second 秒数
	 * @return 时间
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
	 * 获取当前时间戳(秒)
	 *
	 * @return 时间戳
	 */
	public static long dateline() {
		return System.currentTimeMillis() / 1000;
	}


	/**
	 * 获取服务器启动时间
	 */
	public static Date getServerStartDate() {
		long time = ManagementFactory.getRuntimeMXBean().getStartTime();
		return new Date(time);
	}

	/**
	 * 计算相差天数
	 */
	public static int differentDaysByMillisecond(Date date1, Date date2) {
		return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
	}

	/**
	 * 计算两个时间差
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
