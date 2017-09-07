package tech.lihx.demo.core.common.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {

	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

	private static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";

	private static String DATE_FORMAT = "yyyy/MM/dd";

	private static String LONG_DATE_FORMAT = "yyyyMMddHHmmss";

	private static final String[] zodiacArr = { "猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊" };

	private static final String[] constellationArr = { "水瓶座", "双鱼座", "牡羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",
			"天蝎座", "射手座", "魔羯座" };

	private static final int[] constellationEdgeDay = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };

	private static final String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

	private static final Integer[] weekDayNum = { 7, 1, 2, 3, 4, 5, 6 };

	private final static long minute = 60 * 1000;// 1分钟

	private final static long hour = 60 * minute;// 1小时

	private final static long day = 24 * hour;// 1天

	private final static long month = 31 * day;// 月

	private final static long year = 12 * month;// 年


	/**
	 * 返回文字描述的日期 该方法用来生成那种回复留言 或者帖子的回复时间显示 刚刚 几分钟前 等等
	 *
	 * @param date
	 * @return
	 */
	public static String getTimeFormatText( Date date ) {
		if ( date == null ) { return null; }
		long diff = new Date().getTime() - date.getTime();
		long r = 0;
		if ( diff > year ) {
			r = diff / year;
			return r + "年前";
		}
		if ( diff > month ) {
			r = diff / month;
			return r + "个月前";
		}
		if ( diff > day ) {
			r = diff / day;
			return r + "天前";
		}
		if ( diff > hour ) {
			r = diff / hour;
			return r + "个小时前";
		}
		if ( diff > minute ) {
			r = diff / minute;
			return r + "分钟前";
		}
		return "刚刚";
	}


	/**
	 * 返回日期是星期几 文字星期几
	 * <p>
	 *
	 * @param dt
	 * @return TODO(这里描述每个参数,如果有返回值描述返回值,如果有异常描述异常)
	 */
	public static String getWeekOfDate( Date dt ) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if ( w < 0 ) {
			w = 0;
		}

		return weekDays[w];
	}


	/**
	 * 返回日期是星期几 数字
	 * <p>
	 *
	 * @param dt
	 * @return TODO(这里描述每个参数,如果有返回值描述返回值,如果有异常描述异常)
	 */
	public static Integer getWeekOfDateNum( Date dt ) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if ( w < 0 ) {
			w = 1;
		}

		return weekDayNum[w];
	}


	/**
	 * Get the previous time, from how many days to now.
	 *
	 * @param days
	 *            How many days.
	 * @return The new previous time.
	 */
	public static Date previous( int days ) {
		return new Date(System.currentTimeMillis() - days * 3600000L * 24L);
	}


	/**
	 * Convert date and time to string like "yyyy-MM-dd HH:mm:ss".
	 */
	public static String formatDateLong( Date d ) {
		if ( d == null ) { return ""; }
		return new SimpleDateFormat(LONG_DATE_FORMAT).format(d);
	}


	/**
	 * Convert date and time to string like "yyyy-MM-dd HH:mm".
	 */
	public static String formatDateTime( Date d ) {
		if ( d == null ) { return ""; }
		return new SimpleDateFormat(DATETIME_FORMAT).format(d);
	}


	/**
	 * Convert date and time to string like "yyyy-MM-dd HH:mm".
	 */
	public static String formatDateTime( long d ) {
		return new SimpleDateFormat(DATETIME_FORMAT).format(d);
	}


	/**
	 * convert date and time to String like format
	 */
	public static String formatDateTime( String format, Date date ) {
		return new SimpleDateFormat(format).format(date);
	}


	/**
	 * Convert date to String like "yyyy-MM-dd".
	 */
	public static String formatDate( Date d ) {
		if ( d == null ) { return ""; }
		return new SimpleDateFormat(DATE_FORMAT).format(d);
	}


	/**
	 * Parse date like "yyyy-MM-dd".
	 */
	public static Date parseDate( String d ) {
		try {
			return new SimpleDateFormat(DATE_FORMAT).parse(d);
		} catch ( Exception e ) {}
		return null;
	}


	/**
	 * Parse date like "yyyy-MM-dd".
	 */
	public static Date parseyyyyMMddDate( String d ) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(d);
		} catch ( Exception e ) {}
		return null;
	}


	/**
	 * Parse date and time like "yyyy-MM-dd hh:mm".
	 */
	public static Date parseDateTime( String dt ) {
		try {
			return new SimpleDateFormat(DATETIME_FORMAT).parse(dt);
		} catch ( Exception e ) {}
		return null;
	}


	// -----------------获取指定日期的年份，月份，日份，小时，分，秒，毫秒----------------------------

	/**
	 * 获取指定日期的年份
	 *
	 * @param p_date
	 * @return
	 * @author longyin
	 * @Date: 2012-03-07
	 */
	public static int getYearOfDate( Date p_date ) {
		if ( null != p_date ) {
			Calendar c = Calendar.getInstance();
			c.setTime(p_date);
			return c.get(Calendar.YEAR);
		}
		return -1;
	}


	/**
	 * 获取指定日期的月份
	 *
	 * @param p_date
	 * @return
	 * @author longyin
	 * @Date: 2012-03-07
	 */
	public static int getMonthOfDate( Date p_date ) {
		if ( null != p_date ) {
			Calendar c = Calendar.getInstance();
			c.setTime(p_date);
			return c.get(Calendar.MONTH) + 1;
		}
		return -1;
	}


	/**
	 * 获取指定日期的日份
	 *
	 * @param p_date
	 * @return
	 * @author longyin
	 * @Date: 2012-03-07
	 */
	public static int getDayOfDate( Date p_date ) {
		if ( null != p_date ) {
			Calendar c = Calendar.getInstance();
			c.setTime(p_date);
			return c.get(Calendar.DAY_OF_MONTH);
		}
		return -1;
	}


	/**
	 * 获取指定日期的小时
	 *
	 * @param p_date
	 * @return
	 * @author longyin
	 * @Date: 2012-03-07
	 */
	public static int getHourOfDate( Date p_date ) {
		if ( null != p_date ) {
			Calendar c = Calendar.getInstance();
			c.setTime(p_date);
			return c.get(Calendar.HOUR_OF_DAY);
		}
		return -1;
	}


	/**
	 * 获取指定日期的分钟
	 *
	 * @param p_date
	 * @return
	 * @author longyin
	 * @Date: 2012-03-07
	 */
	public static int getMinuteOfDate( Date p_date ) {
		if ( null != p_date ) {
			Calendar c = Calendar.getInstance();
			c.setTime(p_date);
			return c.get(Calendar.MINUTE);
		}
		return -1;
	}


	/**
	 * 获取指定日期的秒钟
	 *
	 * @param p_date
	 * @return
	 * @author longyin
	 * @Date: 2012-03-07
	 */
	public static int getSecondOfDate( Date p_date ) {
		if ( null != p_date ) {
			Calendar c = Calendar.getInstance();
			c.setTime(p_date);
			return c.get(Calendar.SECOND);
		}
		return -1;
	}


	/**
	 * 获取指定日期的毫秒
	 *
	 * @param p_date
	 * @return
	 * @author longyin
	 * @Date: 2012-03-07
	 */
	public static long getMillisOfDate( Date p_date ) {
		if ( null != p_date ) {
			Calendar c = Calendar.getInstance();
			c.setTime(p_date);
			return c.getTimeInMillis();
		}
		return -1;
	}


	/**
	 * 根据日期获取年龄
	 *
	 * @param birthDay
	 * @return
	 * @throws Exception
	 */
	public static int getAge( Date birthDay ) throws Exception {
		Calendar cal = Calendar.getInstance();

		if ( cal.before(birthDay) ) { throw new IllegalArgumentException(
				"The birthDay is before Now.It's unbelievable!"); }

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if ( monthNow <= monthBirth ) {
			if ( monthNow == monthBirth ) {
				// monthNow==monthBirth
				if ( dayOfMonthNow < dayOfMonthBirth ) {
					age--;
				} else {
					// do nothing
				}
			} else {
				// monthNow>monthBirth
				age--;
			}
		} else {
			// monthNow<monthBirth
			// donothing
		}

		return age;
	}


	/**
	 * Java 毫秒转换为（天：时）方法
	 *
	 * @param ms
	 * @return
	 */
	@SuppressWarnings( "hiding" )
	public static String formatDayHour( long ms, int validDay ) {
		int temp = (int) (ms / (60 * 60 * 1000));
		int day = 0;
		int hour = 0;
		if ( temp % 24 == 0 ) {
			day = (validDay * 24 - temp) / 24;
			hour = 0;
		} else {
			day = (validDay * 24 - temp) / 24;
			if ( temp < 24 ) {
				hour = 24 - temp;
			} else {
				hour = 24 - temp % 24;
			}
		}
		return new StringBuffer().append(day).append("天").append(hour).append("小时").toString();
	}


	/**
	 * 根据日期获取生肖
	 *
	 * @return
	 */
	public static String date2Zodica( Calendar time ) {
		return zodiacArr[time.get(Calendar.YEAR) % 12];
	}


	/**
	 * 根据日期获取星座
	 *
	 * @param time
	 * @return
	 */
	@SuppressWarnings( "hiding" )
	public static String date2Constellation( Calendar time ) {
		int month = time.get(Calendar.MONTH);
		int day = time.get(Calendar.DAY_OF_MONTH);
		if ( day < constellationEdgeDay[month] ) {
			month = month - 1;
		}
		if ( month >= 0 ) { return constellationArr[month]; }
		// default to return 魔羯
		return constellationArr[11];
	}


	/**
	 * 获取当前java.sql.Timestamp
	 *
	 * @return 当前java.sql.Timestamp
	 * @see Date
	 */
	public final static Timestamp nowDateTime() {
		return new Timestamp(new Date().getTime());
	}


	/**
	 * 计算两个日期之间相差的天数
	 *
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 */
	public static int daysBetween( Date smdate, Date bdate ) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date newSmdate = null;
		Date newBdate = null;
		try {
			newSmdate = sdf.parse(sdf.format(smdate));
			newBdate = sdf.parse(sdf.format(bdate));
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(newSmdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(newBdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}


	/**
	 *
	 * 比较两个字符串时间 之间相差的天数
	 * <p>
	 *
	 * @param smdate
	 *            小
	 * @param bdate
	 *            大
	 * @return
	 */
	public static int daysBetweenStr( String smdate, String bdate ) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date newSmdate = null;
		Date newBdate = null;
		try {
			newSmdate = sdf.parse(smdate);
			newBdate = sdf.parse(bdate);
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
		}
		return daysBetween(newSmdate, newBdate);
	}


	/**
	 *
	 * 比较传入日期是否已过期(与服务器当前日期比较)
	 *
	 * @param inputDate
	 *            传入日期
	 * @return true 未过期(即传入日期大于当前日期)
	 * @return false 已过期(即传入日期小于等于当前日期)
	 */
	public static Boolean isOverdate( Date inputDate ) {
		Date nowDate = new Date();
		return nowDate.before(inputDate);
	}


	/**
	 *
	 * 返回一个日期加上几个月后的时间
	 *
	 * @param inputDate
	 *            传入日期
	 */
	public static Date getDatePlusMonths( Date inputDate, int monthNum ) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(inputDate);
		calendar.add(Calendar.MONTH, monthNum);
		return calendar.getTime();
	}


	/**
	 * 返回两个日期相差月数(不足一月的不算)
	 *
	 * @param date1
	 * @param date2
	 * @return 相差月数
	 */
	public static int getBetweenMonth( Date date1, Date date2 ) {

		SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();

		cal1.setTime(date1);
		cal2.setTime(date2);

		int yearBetween = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
		int monthBetween = cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);
		int d1 = Integer.parseInt(sdf2.format(cal1.getTime()));
		int d2 = Integer.parseInt(sdf2.format(cal2.getTime()));
		int monthMax = cal2.getActualMaximum(Calendar.DAY_OF_MONTH);
		if ( d1 > monthMax ) {
			d1 = monthMax;
		}
		if ( d1 > d2 ) {
			monthBetween -= 1;
		}
		return 12 * yearBetween + monthBetween;
	}


	/**
	 * @Description 获取当前中国时区的TIMESTAMP日期
	 * @return
	 */
	public static Timestamp getSysTimestamp() {
		final TimeZone zone = TimeZone.getTimeZone("GMT+8");// 获取中国时区
		TimeZone.setDefault(zone);// 设置时区
		return new Timestamp(new Date().getTime());
	}


	/**
	 * 格式日期为字符串内容
	 * <p>
	 *
	 * @param date
	 *            时间
	 * @param pattern
	 *            日期格式,例： yyyyMMddHHmmss
	 * @return String 格式后的字符串日期
	 */
	public static String formatDate( Date date, String pattern ) {
		if ( date == null ) { return null; }
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}


	/**
	 * 格式long类型日期为 Date
	 * <p>
	 *
	 * @param time
	 *            long类型日期
	 * @return Date
	 */
	public static Date formatDate( long time ) {
		return new Date(time);
	}


	public static String formateDateStr( Long time ) {
		return formatDate(formatDate(time), DATETIME_FORMAT);
	}


	public static String timeStampUrl( String url ) {
		if ( url != null ) { return url + "?t=" + System.currentTimeMillis(); }
		return "";
	}

}
