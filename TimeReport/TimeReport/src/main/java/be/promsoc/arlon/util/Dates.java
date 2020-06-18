package be.promsoc.arlon.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Dates {

	
//	 convert a string to a date
	public static Date StringToDate(String s) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");      
       return dateFormat.parse(s);
	}
	
//	 convert a string to a date
	public static Date CalculatedDate(String s) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );   
		Calendar cal = Calendar.getInstance();    
		cal.setTime( dateFormat.parse(s)); 
		return cal.getTime();
	}
	
//	 convert a string to a date, and add a number of days 
	public static Date CalculatedDate(String s, int addDays) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );   
		Calendar cal = Calendar.getInstance();    
		cal.setTime( dateFormat.parse(s)); 
		cal.add( Calendar.DATE, addDays );    
		return cal.getTime();
	}
	
}
