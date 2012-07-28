package net.bloody.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 데이터 연동을 위한 Date 포멧변환 유틸
 * 
Letter	Date or Time Component	Presentation	Examples
G	Era designator	Text	AD
y	Year	Year	1996; 96
M	Month in year	Month	July; Jul; 07
w	Week in year	Number	27
W	Week in month	Number	2
D	Day in year	Number	189
d	Day in month	Number	10
F	Day of week in month	Number	2
E	Day in week	Text	Tuesday; Tue
a	Am/pm marker	Text	PM
H	Hour in day (0-23)	Number	0
k	Hour in day (1-24)	Number	24
K	Hour in am/pm (0-11)	Number	0
h	Hour in am/pm (1-12)	Number	12
m	Minute in hour	Number	30
s	Second in minute	Number	55
S	Millisecond	Number	978
z	Time zone	General time zone	Pacific Standard Time; PST; GMT-08:00
Z	Time zone	RFC 822 time zone	-0800
 *
 */
public class DateUtils {
	/**
	 * Date를 String형태로 관련 포멧으로 변환
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateString(Date date,String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);

		String dateString = dateFormat.format(date);
		
		return dateString;
	}
	
	/**
	 * String를 DATE의 형태로 관련 포멧으로 변환
	 * @param value
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date getDate(String value,String format) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date date = null;
		
		date = dateFormat.parse(value);
		
		return date;
	}
	
	// Month
	public static Date getLastOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(year, month - 1, 1, 0, 0, 0); // Months are 0 to 11
		
		
		int lastday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		//System.out.println("lastDay: " + lastday + " for month " + month);
		calendar.set(year, month - 1, lastday, 0, 0, 0);
		
		Date lastDay = new Date();
		
		lastDay.setTime(calendar.getTimeInMillis());
		return lastDay;
	}
	
	public static Date getYesterday(Date date) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		
		Date yesterday = new Date();
		
		yesterday.setTime(calendar.getTimeInMillis());
		
		return yesterday;
	}
	
	
	public static Date getLastWeekDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		
		Date lastWeekDay = new Date();
		
		lastWeekDay.setTime(calendar.getTimeInMillis());
		
		return lastWeekDay;
	}
	
	
	public static Date getLastMonthDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -30);
		
		Date lastWeekDay = new Date();
		
		lastWeekDay.setTime(calendar.getTimeInMillis());
		
		return lastWeekDay;
	}
	
	public static Date getLastOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(date);
		
		final int year = calendar.get(Calendar.YEAR);
		final int month = calendar.get(Calendar.MONTH);
		return getLastOfMonth(year, month + 1);
	}
	
	public static int getLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(date);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public static Date getFirstOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(year, month - 1, 1, 0, 0, 0); // Months are 0 to 11
		Date lastDay = new Date();
		
		lastDay.setTime(calendar.getTimeInMillis());
		return lastDay;
	}
	
	public static Date getFirstOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(date);
		
		final int year = calendar.get(Calendar.YEAR);
		final int month = calendar.get(Calendar.MONTH);
		return getFirstOfMonth(year, month + 1);
	}
	
	public static Date getNextMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(date);
		
		calendar.add(Calendar.MONTH, 1);
		
		final int year = calendar.get(Calendar.YEAR);
		final int month = calendar.get(Calendar.MONTH);
		return getFirstOfMonth(year, month + 1);
	}
	
	public static Date getDateAfterMonths(Date date, int afterMonths) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(date);
		
		calendar.add(Calendar.MONTH, afterMonths);
		
		final int year = calendar.get(Calendar.YEAR);
		final int month = calendar.get(Calendar.MONTH);
		return getFirstOfMonth(year, month + 1);
	}
	
	public static Date getDateBeforeMonths(Date date, int beforeMonths) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(date);
		
		calendar.add(Calendar.MONTH, -beforeMonths);
		
		final int year = calendar.get(Calendar.YEAR);
		final int month = calendar.get(Calendar.MONTH);
		return getFirstOfMonth(year, month + 1);
	}
	
	// Year
	
	public static Date getLastOfYear(int year) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(year, 11, 1, 0, 0, 0); // Months are 0 to 11
		
		
		int lastday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(year, 11, lastday, 0, 0, 0);
		
		Date lastDay = new Date();
		
		lastDay.setTime(calendar.getTimeInMillis());
		return lastDay;
	}
	
	public static Date getLastOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(date);
		
		final int year = calendar.get(Calendar.YEAR);
		return getLastOfYear(year);
	}
	
	public static Date getFirstOfYear(int year) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(year, 0, 1, 0, 0, 0); // Months are 0 to 11
		Date lastDay = new Date();
		
		lastDay.setTime(calendar.getTimeInMillis());
		return lastDay;
	}
	
	public static Date getFirstOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(date);
		
		final int year = calendar.get(Calendar.YEAR);
		return getFirstOfYear(year);
	}
	
	public static Date getNextYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(date);
		
		calendar.add(Calendar.YEAR, 1);
		
		final int year = calendar.get(Calendar.YEAR);
		return getFirstOfYear(year);
	}
}
