package com.ares.time;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * 服务器系统时间工具
 * 
 * 使用jdk8时间API<br>
 * <br>
 * 
 * {@link Instant} : 与Date类似<br>
 * {@link LocalDate} : 不带时间的日期<br>
 * {@link LocalTime} : 不带日期的时间<br>
 * {@link LocalDateTime} : 带日期的时间的类<br>
 * {@link ZoneId} : 时区<br>
 * {@link ZonedDateTime} : 一个带时区的完整时间<br>
 * {@link Instant} : unix时间,代表时间戳,如2018-01-13T02:20:13.592Z<br>
 * {@link Clock} : 获取某个时区下当前的瞬时时间,日期或时间<br>
 * {@link Duration} : 并表示一个绝对的精度跨度,使用毫秒为单位<br>
 * {@link Period} : 这个类与Duration小童的概念,但以熟悉的单位表示,如年,月,周<br>
 * {@link DateTimeFormatter} : 格式化输出<br>
 * {@link TemporalAdjusters} : 获得指定日期时间,如当月的第一天,今年的最后一天<br>
 */
public class TimeUtils {
	/**
	 * 时间单位
	 */
	public interface Unit {
		int YEAR = 1;
		int MONTH = 2;
		int WEEK = 3;
		int DAY = 4;
		int HOURS = 5;
		int MINUTES = 6;
		int SECONDS = 7;
		int MILLIS = 8;
	}

//	public static void main(String[] args) {
//		Date date1 = format2Date("2019-07-30 23:10:43", "yyyy-MM-dd HH:mm:ss");
//		Date date2 = format2Date("2019-08-01 23:11:43", "yyyy-MM-dd HH:mm:ss");
//		System.out.println(getBetweenDateTime(date1.getTime(), date2.getTime(), Unit.DAY));
//		System.out.println(getBetweenDateTime(date1.getTime(), date2.getTime(), Unit.MONTH));
//		System.out.println(getBetweenDateTime(date1.getTime(), date2.getTime(), Unit.YEAR));
//		System.out.println(getBetweenDateTime(date1.getTime(), date2.getTime(), Unit.WEEK));
//		System.out.println(getBetweenDateTime(date1.getTime(), date2.getTime(), Unit.HOURS));
//		System.out.println(getBetweenDateTime(date1.getTime(), date2.getTime(), Unit.MINUTES));
//		System.out.println(getBetweenDateTime(date1.getTime(), date2.getTime(), Unit.SECONDS));
//		System.out.println(getBetweenDateTime(date1.getTime(), date2.getTime(), Unit.MILLIS));
//		System.out.println(getDayOfYear(date1.getTime()));
//		System.out.println(isSameWeek(date1, date2));
//		System.out.println(format2string(new Date()));
//	}

	/**
	 * 获取当天剩余秒数
	 * 
	 * @return
	 */
	public static long getTodayRemainSeconds() {
		LocalDateTime midnight = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
		return ChronoUnit.SECONDS.between(LocalDateTime.now(), midnight);
	}

	/**
	 * 获取游戏当前时间
	 * 
	 * @return
	 */
	public static long getCurrentTime() {
		return System.currentTimeMillis();
	}

	/**
	 * 获取当天0时0分0秒 到现在流逝的时间
	 *
	 * @return 毫秒
	 */
	public static long getTodayPassMillis() {
		LocalTime localTime = LocalTime.now();
		int hour = localTime.getHour();
		int minute = localTime.getMinute();
		int second = localTime.getSecond();
		int nano = localTime.getNano();

		return hour * 3600000 + minute * 60000 + second * 1000 + nano / 1000000;
	}

	/**
	 * 获取今天开始时间
	 *
	 * @return
	 */
	public static long getTodayBeginTime() {
		LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

		return localDateTimeToInstant(localDateTime).toEpochMilli();
	}

	/**
	 * 获取当前时间的后一天的开始时间
	 * 
	 * @return
	 */
	public static Date getNextBeginTime() {
		LocalDate localDate = LocalDate.now();
		LocalDate plusDays = localDate.plusDays(1);

		LocalDateTime nextDay = LocalDateTime.of(plusDays, LocalTime.MIN);
		return localDateTimeToDate(nextDay);
	}

	/**
	 * 获取本周开始时间
	 *
	 * @return
	 */
	public static long getCurWeekBeginTime() {
		LocalDate localDate = LocalDate.now().with(DayOfWeek.MONDAY);
		return localDateToDate(localDate).getTime();
	}

	/**
	 * 获取本月开始时间
	 * 
	 * @return
	 */
	public static long getMonthBeginTime() {
		LocalDate today = LocalDate.now();

		LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
		return localDateToDate(firstDayOfMonth).getTime();
	}

	/**
	 * 获取下个月开始时间
	 * 
	 * @return
	 */
	public static long getNextMonthBeginTime() {
		LocalDate today = LocalDate.now();

		// 默认获取下月最后一天的开始时间
		LocalDate plusMonths = today.plusMonths(1);
		// 获取下月第一天开始时间
		LocalDate _plusMonths = plusMonths.with(TemporalAdjusters.firstDayOfMonth());
		return localDateToDate(_plusMonths).getTime();
	}

	/**
	 * 获得格式化的时间值 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param time 可为Date或Long
	 * @return
	 */
	public static String format2string(Object time) {
		LocalDateTime localDateTime = convertLocalDateTime(time);

		if (localDateTime == null) {
			return "null";
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return localDateTime.format(formatter);
	}

	/**
	 * 获得格式化的时间值
	 *
	 * @param time   可为Date或Long
	 * @param format "yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static String format2string(Object time, String format) {
		LocalDateTime localDateTime = convertLocalDateTime(time);

		if (localDateTime == null) {
			return "null";
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return localDateTime.format(formatter);
	}

	/**
	 * 获得格式化的时间值
	 *
	 * @param time
	 * @param format "yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static Date format2Date(String date, String format) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
			LocalDateTime parseDate = LocalDateTime.parse(date, formatter);

			return localDateTimeToDate(parseDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * netbeans/eclipse等IDE下 运行/调试 关闭时IDE内部调用Process.destroy() <br />
	 * 无法触发到JVM shutdown hook, 导致关闭时无法回存 <br />
	 * 故在IDE环境下添加了system property: ideDebug
	 *
	 * @return
	 */
	public static boolean isIDEEnvironment() {
		String val = System.getProperty("ideDebug");
		return val != null && val.equals("true");
	}

	/**
	 * 判断两个时间中间所差的日期时间(天数,周数,月数,年数,小时,分钟,秒,毫秒)
	 * 
	 * @param time1 可为Date或Long
	 * @param time2 可为Date或Long
	 * @param unit  {@link Unit}
	 * @return
	 */
	public static long getBetweenDateTime(Object time1, Object time2, int unit) {
		LocalDateTime localDate1 = convertLocalDateTime(time1);
		LocalDateTime localDate2 = convertLocalDateTime(time2);

		switch (unit) {
		case Unit.DAY:
			return localDate1.until(localDate2, ChronoUnit.DAYS);
		case Unit.MONTH:
			return localDate1.until(localDate2, ChronoUnit.MONTHS);
		case Unit.YEAR:
			return localDate1.until(localDate2, ChronoUnit.YEARS);
		case Unit.WEEK:
			return localDate1.until(localDate2, ChronoUnit.WEEKS);
		case Unit.HOURS:
			return localDate1.until(localDate2, ChronoUnit.HOURS);
		case Unit.MINUTES:
			return localDate1.until(localDate2, ChronoUnit.MINUTES);
		case Unit.SECONDS:
			return localDate1.until(localDate2, ChronoUnit.SECONDS);
		case Unit.MILLIS:
			return localDate1.until(localDate2, ChronoUnit.MILLIS);
		default:
			return -1;
		}
	}

	/**
	 * 判断两个时间是否在同一天
	 *
	 * @param time
	 * @param time2
	 * @return
	 */
	public static boolean isSameDay(Object time1, Object time2) {
		return getBetweenDateTime(time1, time2, Unit.DAY) == 0;
	}

	/**
	 * 指定时间的年份
	 *
	 * @param time 可为Date或Long
	 * @return
	 */
	public static int getYear(Object time) {
		LocalDate localDate = convertLocalDate(time);
		if (localDate == null) {
			return -1;
		}
		return localDate.getYear();
	}

	/**
	 * 指定时间的月份
	 *
	 * @param time 可为Date或Long
	 * @return
	 */
	public static int getMonth(Object time) {
		LocalDate localDate = convertLocalDate(time);
		if (localDate == null) {
			return -1;
		}
		return localDate.getMonthValue();
	}

	/**
	 * 获取日期(一个月内的第几天)
	 *
	 * @param time 可为Date或Long
	 * @return
	 */
	public static int getDayOfMonth(Object time) {
		LocalDate localDate = convertLocalDate(time);
		if (localDate == null) {
			return -1;
		}
		return localDate.getDayOfMonth();
	}

	/**
	 * 获取小时
	 *
	 * @param time 可为Date或Long
	 * @return
	 */
	public static int getDayOfHour(Object time) {
		LocalDateTime localDateTime = convertLocalDateTime(time);
		if (localDateTime == null) {
			return -1;
		}

		return localDateTime.getHour();
	}

	/**
	 * 获取分钟
	 *
	 * @param time 可为Date或Long
	 * @return
	 */
	public static int getDayOfMin(Object time) {
		LocalDateTime localDateTime = convertLocalDateTime(time);
		if (localDateTime == null) {
			return -1;
		}

		return localDateTime.getMinute();
	}

	/**
	 * 获取秒
	 *
	 * @param time 可为Date或Long
	 * @return
	 */
	public static int getDayOfSecond(Object time) {
		LocalDateTime localDateTime = convertLocalDateTime(time);
		if (localDateTime == null) {
			return -1;
		}

		return localDateTime.getSecond();
	}

	/**
	 * 获取指定时间 是一月内的第几周
	 *
	 * @param time 可为Date或Long
	 * @return
	 */
	public static int getDayOfWeekInMonth(Object time) {
		LocalDate localDate = convertLocalDate(time);
		if (localDate == null) {
			return -1;
		}

		return localDate.get(ChronoField.ALIGNED_WEEK_OF_MONTH);
	}

	/**
	 * 获取星期几
	 *
	 * @param time 可为Date或Long
	 * @return
	 */
	public static int getDayOfWeek(Object time) {
		LocalDate localDate = convertLocalDate(time);
		if (localDate == null) {
			return -1;
		}

		return localDate.getDayOfWeek().getValue();
	}

	/**
	 * 获取一年内的第几天
	 * 
	 * @param time 可为Date或Long
	 * @return
	 */
	public static int getDayOfYear(Object time) {
		LocalDate localDate = convertLocalDate(time);
		if (localDate == null) {
			return -1;
		}

		return localDate.get(ChronoField.DAY_OF_YEAR);
	}

	/**
	 * 判断两个时间是否为一周
	 * 
	 * @param time1 可为Date或Long
	 * @param time2 可为Date或Long
	 * @return
	 */
	public static boolean isSameWeek(Object time1, Object time2) {
		return getBetweenDateTime(time1, time2, Unit.WEEK) == 0;
	}

	/**
	 * 将localDateTime转为Instant
	 */
	public static Instant localDateTimeToInstant(LocalDateTime localDateTime) {
		return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
	}

	/**
	 * 将localDate转为Instant
	 */
	public static Instant localDateToInstant(LocalDate localDate) {
		return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
	}

	/**
	 * 将localDateTime转为Date
	 */
	public static Date localDateTimeToDate(LocalDateTime localDateTime) {
		return Date.from(localDateTimeToInstant(localDateTime));
	}

	/**
	 * 将Date转为localDateTime
	 */
	public static LocalDateTime dateToLocalDateTime(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	/**
	 * 将localDate转为Date
	 */
	public static Date localDateToDate(LocalDate localDate) {
		return Date.from(localDateToInstant(localDate));
	}

	/**
	 * 将Date转为LocalDate
	 */
	public static LocalDate dateToLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	/**
	 * 将Date转为LocalTime
	 */
	public static LocalTime dateToLocalTime(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
	}

	/**
	 * 将time转换成LocalDate
	 * 
	 * @param time
	 * @return
	 */
	private static LocalDate convertLocalDate(Object time) {
		LocalDate localDate = null;
		if (time instanceof Date) {
			Date date = (Date) time;
			localDate = dateToLocalDate(date);
		} else if (time instanceof Long) {
			long timestamp = (long) time;
			localDate = dateToLocalDate(new Date(timestamp));
		}
		return localDate;
	}

	/**
	 * 将time转换成LocalDateTime
	 * 
	 * @param time
	 * @return
	 */
	private static LocalDateTime convertLocalDateTime(Object time) {
		LocalDateTime localDateTime = null;

		if (time instanceof Date) {
			Date date = (Date) time;
			localDateTime = dateToLocalDateTime(date);
		} else if (time instanceof Long) {
			long timestamp = (long) time;
			Instant instant = Instant.ofEpochMilli(timestamp);
			localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		}
		return localDateTime;
	}

}
