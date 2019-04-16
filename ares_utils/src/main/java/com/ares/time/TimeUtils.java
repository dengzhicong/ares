/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ares.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 服务器系统时间工具
 * 
 * @author HW-fanjaiwei
 *
 */
public class TimeUtils {

	/**
	 * 跳过的时间（秒）
	 */
	private static long skipSeconds = 0;

	// 用ThreadLocal处理SimpleDateFormat非线程安全的问题/////////////////////////
	private static final ThreadLocal<DateFormat> sdfNoSecond = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm");
		}
	};

	public static DateFormat getSdfNoSecond() {
		return sdfNoSecond.get();
	}
	////////////////////////////////////////////////////////////////////////////

	/**
	 * 获取当天剩余秒数
	 * 
	 * @return
	 */
	public static long getTodayRemainSeconds() {
		LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
		return ChronoUnit.SECONDS.between(LocalDateTime.now(), midnight);
	}

	/**
	 * 获取游戏当前时间
	 * 
	 * @return
	 */
	public static long currentTime() {
		return System.currentTimeMillis() + skipSeconds * 1000;
	}

	public static long getSkipSeconds() {
		return skipSeconds;
	}

	public static void setSkipSeconds(int skipSeconds) {
		TimeUtils.skipSeconds = skipSeconds;
	}

	/**
	 * 获取当天时间字符串
	 * 
	 * @return 如：20180612
	 */
	public static String getTodayTime() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);// 获取年份
		int month = cal.get(Calendar.MONTH) + 1;// 获取月份
		int day = cal.get(Calendar.DATE);// 获取日
		StringBuilder sb = new StringBuilder();
		sb.append(year);
		if (month < 10) {
			sb.append("0" + month);
		} else {
			sb.append(month);
		}
		if (day < 10) {
			sb.append("0" + day);
		} else {
			sb.append(day);
		}
		return sb.toString();
	}

	/**
	 * 获取当天0时0分0秒 到现在流逝的时间
	 *
	 * @return
	 */
	public static long getTodayPassMillis() {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(currentTime());
		int hour = instance.get(Calendar.HOUR_OF_DAY);
		int min = instance.get(Calendar.MINUTE);
		int second = instance.get(Calendar.SECOND);
		int millis = instance.get(Calendar.MILLISECOND);

		return hour * 3600000 + min * 60000 + second * 1000 + millis;
	}

	/**
	 * 获取今天开始时间
	 *
	 * @return
	 */
	public static long getTodayBeginTime() {
		long curTime = currentTime();
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(curTime);
		instance.set(Calendar.MILLISECOND, 0);
		int year = instance.get(Calendar.YEAR);
		int month = instance.get(Calendar.MONTH);
		int date = instance.get(Calendar.DATE);
		instance.set(year, month, date, 0, 0, 0);
		return instance.getTimeInMillis();
	}

	public static Date getTodayBeginTime2() {
		long todayBegin = getTodayBeginTime();
		return new Date(todayBegin);
	}

	/**
	 * 获取当前时间的后一天的开始时间
	 * 
	 * @return
	 */
//	public static Date getNextBeginTime() {
//		return new DateTime(getTodayBeginTime()).plusDays(1).toDate();
//	}

	/**
	 * 取得当前日期所在周的第一天
	 *
	 * @return
	 */
	public static long getFirstDayOfWeek() {
		long curTime = currentTime();
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(curTime);
		instance.setFirstDayOfWeek(Calendar.MONDAY);
		instance.set(Calendar.DAY_OF_WEEK, instance.getFirstDayOfWeek());
		return instance.getTimeInMillis();
	}

	/**
	 * 获取本周开始时间
	 *
	 * @return
	 */
	public static long getCurWeekBeginTime() {
		long curTime = currentTime();
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(curTime);
		instance.setFirstDayOfWeek(Calendar.MONDAY);
		instance.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		instance.set(Calendar.HOUR_OF_DAY, 0);
		instance.set(Calendar.MINUTE, 0);
		instance.set(Calendar.SECOND, 0);
		instance.set(Calendar.MILLISECOND, 0);
		return instance.getTimeInMillis();
	}

	// 获取本月开始时间
	public static long getMonthBeginTime() {
		long curTime = currentTime();
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(curTime);
		instance.set(Calendar.MILLISECOND, 0);
		int year = instance.get(Calendar.YEAR);
		int month = instance.get(Calendar.MONTH);
		instance.set(year, month, 1, 0, 0, 0);
		return instance.getTimeInMillis();
	}

	// 获取下个月开始时间
	public static long getNextMonthBeginTime() {
		long curTime = currentTime();
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(curTime);
		instance.set(Calendar.MILLISECOND, 0);
		int year = instance.get(Calendar.YEAR);
		int month = instance.get(Calendar.MONTH);
		if (month >= 11) {
			year += 1;
			month = 0;
		} else {
			month += 1;
		}
		instance.set(year, month, 1, 0, 0, 0);
		return instance.getTimeInMillis();
	}

	// 获得格式化的时间值
	public static String NowToString() {
		long now = currentTime();
		Date date = new Date(now);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(date);
	}

	// 获得格式化的时间值
	public static String NowToString(String df) {
		long now = currentTime();
		Date date = new Date(now);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(df);
		return simpleDateFormat.format(date);
	}

	// 获得格式化的时间值
	public static String NowTGToString() {
		long now = currentTime();
		Date date = new Date(now);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return simpleDateFormat.format(date);
	}

	/**
	 * 获得格式化的时间值 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param time
	 * @return
	 */
	public static String format2string(long time) {
		Date date = new Date(time);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(date);
	}

	/**
	 * 获得格式化的时间值
	 *
	 * @param time
	 * @param format "yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static String format2string(long time, String format) {
		Date date = new Date(time);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(date);
	}

	/**
	 * 获得格式化的时间值
	 *
	 * @param time
	 * @param format "yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static Date format2string(String date, String format) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			return simpleDateFormat.parse(date);
		} catch (ParseException e) {
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
	 * 判断两个时间是否在同一天
	 *
	 * @param time
	 * @param time2
	 * @return
	 */
	public static boolean isSameDay(long time, long time2) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		int d1 = instance.get(Calendar.DAY_OF_YEAR);
		int y1 = instance.get(Calendar.YEAR);
		instance.setTimeInMillis(time2);
		int d2 = instance.get(Calendar.DAY_OF_YEAR);
		int y2 = instance.get(Calendar.YEAR);
		return d1 == d2 && y1 == y2;
	}

	/**
	 * 获取1970至今的天数 （计数会在在每天指定的小时+1，用来判断每天X点清数据之类的）
	 *
	 * @param hour 每天第X个小时+1
	 * @return
	 */
	public static int getCurDay(int hour) {
		TimeZone zone = TimeZone.getDefault(); // 默认时区
		long s = currentTime() / 1000 - hour * 3600;
		if (zone.getRawOffset() != 0) {
			s = s + zone.getRawOffset() / 1000;
		}
		s = s / 86400; // 86400 = 24 * 60 * 60 (一天时间的秒数)
		return (int) s;
	}

	/**
	 * 指定时间的年份
	 *
	 * @param time
	 * @return
	 */
	public static int getYear(long time) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		return instance.get(Calendar.YEAR);
	}

	/**
	 * 指定时间的月份,0-11
	 *
	 * @param time
	 * @return
	 */
	public static int getMonth(long time) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		return instance.get(Calendar.MONTH);
	}

	/**
	 * 获取日期(一个月内的第几天)
	 *
	 * @param time
	 * @return
	 */
	public static int getDayOfMonth(long time) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		return instance.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取小时
	 *
	 * @param time
	 * @return
	 */
	public static int getDayOfHour(long time) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		return instance.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取分钟
	 *
	 * @param time
	 * @return
	 */
	public static int getDayOfMin(long time) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		return instance.get(Calendar.MINUTE);
	}

	/**
	 * 获取秒
	 *
	 * @param time
	 * @return
	 */
	public static int getDayOfSecond(long time) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		return instance.get(Calendar.SECOND);
	}

	/**
	 * 获取指定时间 是一月内的第几周
	 *
	 * @param time
	 * @return
	 */
	public static int getDayOfWeekInMonth(long time) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		return instance.get(Calendar.DAY_OF_WEEK_IN_MONTH);
	}

	/**
	 * 获取星期几
	 *
	 * @param time
	 * @return
	 */
	public static int getDayOfWeek(long time) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		int i = instance.get(Calendar.DAY_OF_WEEK);
		if (i == 1) {
			return 7;
		} else {
			i -= 1;
		}
		return i;
	}

	/**
	 * 获取一年内的第几天
	 *
	 * @param time
	 * @return
	 */
	public static int getDayOfYear(long time) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		return instance.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 字符串转日期("yyyy-MM-dd HH:mm:ss");
	 *
	 * @param date
	 * @return
	 * @throws java.text.ParseException
	 */
	public static Date getDateByString(String date) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.parse(date);
	}

	/**
	 * 判断两个时间中间所差天数
	 *
	 * @param time1 大的时间
	 * @param time2 小的时间
	 * @return
	 */
	public static int getBetweenDays(long time1, long time2) {
		Calendar instance1 = Calendar.getInstance();
		instance1.setTimeInMillis(time1);
		instance1.set(Calendar.HOUR_OF_DAY, 0);
		instance1.set(Calendar.MINUTE, 0);
		instance1.set(Calendar.SECOND, 0);
		instance1.set(Calendar.MILLISECOND, 0);
		Calendar instance2 = Calendar.getInstance();
		instance2.setTimeInMillis(time2);
		instance2.set(Calendar.HOUR_OF_DAY, 0);
		instance2.set(Calendar.MINUTE, 0);
		instance2.set(Calendar.SECOND, 0);
		instance2.set(Calendar.MILLISECOND, 0);
		return (int) ((instance1.getTimeInMillis() - instance2.getTimeInMillis()) / (24 * 60 * 60 * 1000));
	}

	/**
	 * 判断两个时间中间所差分钟
	 *
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static long getBetweenMinutes(long time1, long time2) {
		Calendar instance1 = Calendar.getInstance();
		instance1.setTimeInMillis(time1);
		instance1.set(Calendar.HOUR_OF_DAY, 0);
		instance1.set(Calendar.MINUTE, 0);
		instance1.set(Calendar.SECOND, 0);
		instance1.set(Calendar.MILLISECOND, 0);
		Calendar instance2 = Calendar.getInstance();
		instance2.setTimeInMillis(time2);
		instance2.set(Calendar.HOUR_OF_DAY, 0);
		instance2.set(Calendar.MINUTE, 0);
		instance2.set(Calendar.SECOND, 0);
		instance2.set(Calendar.MILLISECOND, 0);
		return (instance1.getTimeInMillis() - instance2.getTimeInMillis()) / (60 * 1000);
	}

	/**
	 * 获取1970至今的时间, 1获取秒，2 分钟，3小时，4天数,5周数
	 *
	 * @param x
	 * @param time
	 * @return
	 */
	public static long GetCurTimeInMin(int x, long time) {
		TimeZone zone = TimeZone.getDefault(); // 默认时区
		long s = time / 1000;
		if (zone.getRawOffset() != 0) {
			s = s + zone.getRawOffset() / 1000;
		}
		switch (x) {
		case 1:
			break;
		case 2:
			s = s / 60;
			break;
		case 3:
			s = s / 3600;
			break;
		case 4:
			s = s / 86400;
			break;
		case 5:
			s = s / 86400 + 3;// 补足天数，星期1到7算一周
			s = s / 7;
			break;
		default:
			break;
		}
		return s;
	}

	/**
	 * 指定小时与分，秒，计算与当前时间的差值， 不跨天
	 *
	 * @param hour
	 * @param min
	 * @param sec
	 * @return
	 */
	public static int getDecNowToTime(int hour, int min, int sec) {
		long now = currentTime();
		Calendar instance1 = Calendar.getInstance();
		instance1.setTimeInMillis(now);
		instance1.set(Calendar.HOUR_OF_DAY, hour);
		instance1.set(Calendar.MINUTE, min);
		instance1.set(Calendar.SECOND, sec);
		instance1.set(Calendar.MILLISECOND, 0);
		int res = (int) ((instance1.getTimeInMillis() - now) / 1000);
		return res > 0 ? res : 0;
	}

	/**
	 * 指定小时与分，秒，返回当天指定的小时分秒的当前时间值
	 *
	 * @param hour
	 * @param min
	 * @param sec
	 * @return
	 */
	public static long getToTime(int hour, int min, int sec) {
		long now = currentTime();
		Calendar instance1 = Calendar.getInstance();
		instance1.setTimeInMillis(now);
		instance1.set(Calendar.HOUR_OF_DAY, hour);
		instance1.set(Calendar.MINUTE, min);
		instance1.set(Calendar.SECOND, sec);
		instance1.set(Calendar.MILLISECOND, 0);
		return instance1.getTimeInMillis();
	}

	/**
	 * 返回离下一个星期几还剩的秒值
	 *
	 * @param weekday
	 * @param hour
	 * @param min
	 * @param sec
	 * @return
	 */
	public static int getDecNowToTime(int weekday, int hour, int min, int sec) {
		long now = currentTime();

		int wk = getDayOfWeek(now);
		Calendar instance1 = Calendar.getInstance();
		instance1.setTimeInMillis(now);

		if (wk < weekday) {
			// 本周
			instance1.add(Calendar.DAY_OF_MONTH, weekday - wk);
		} else if (wk > weekday) {
			// 下一周
			instance1.add(Calendar.DAY_OF_MONTH, 7 - wk + weekday);
		} else {
			// 当天
			int time = getDecNowToTime(hour, min, sec);
			if (time == 0)// 等于0 表示下一周去了
			{
				instance1.add(Calendar.DAY_OF_MONTH, 7 - wk + weekday);
			} else {
				return time;// 不是就返回当前时间
			}
		}

		instance1.set(Calendar.HOUR_OF_DAY, hour);
		instance1.set(Calendar.MINUTE, min);
		instance1.set(Calendar.SECOND, sec);
		instance1.set(Calendar.MILLISECOND, 0);
		int res = (int) ((instance1.getTimeInMillis() - now) / 1000);
		return res > 0 ? res : 0;
	}

	/**
	 * 判断两个时间是否为一周
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isSameWeek(long time1, long time2) {
		Date d1 = new Date(time1);
		Date d2 = new Date(time2);

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setFirstDayOfWeek(Calendar.MONDAY);// 西方周日为一周的第一天，咱得将周一设为一周第一天
		cal2.setFirstDayOfWeek(Calendar.MONDAY);
		cal1.setTime(d1);
		cal2.setTime(d2);
		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		if (subYear == 0)// subYear==0,说明是同一年
		{
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (subYear == 1 && cal2.get(Calendar.MONTH) == 11) // subYear==1,说明cal比cal2大一年;java的一月用"0"标识，那么12月用"11"
		{
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (subYear == -1 && cal1.get(Calendar.MONTH) == 11)// subYear==-1,说明cal比cal2小一年
		{
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
				return true;
		}
		return false;
	}

	public static void main(String[] args) {
		long remainTime = TimeUtils.getTodayRemainSeconds();
		System.out.println("remainTime:" + remainTime);
	}

}
