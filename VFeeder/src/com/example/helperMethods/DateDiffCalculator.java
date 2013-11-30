package com.example.helperMethods;

import java.text.SimpleDateFormat;
//import java.util.ArrayList;
import java.util.Date;

import org.joda.time.*;
//import org.joda.convert.*;

/**
 * @author einsteinboricua
 * A class to calculate the difference in dates.
 * The JodaTime library was used for this
 *
 */
public class DateDiffCalculator {
	
	//Variables
	private String then, now;
	private Date date1,date2;
	//private String[] first,last;
	//private ArrayList<Integer> dateThen, dateNow;
	
	//Constructor
	/**
	 * A calculator object to find the difference between dates
	 * @param now, the end date
	 * @param then, the start date
	 */
	public DateDiffCalculator(String now, String then)
	{
		this.then=then;
		this.now=now;
	}
	

//	public String calculateLength()
//	{
//		first=then.split("-");
//		last=now.split("-");
//		dateThen=new ArrayList<Integer>();
//		dateNow=new ArrayList<Integer>();
//		
//		try
//		{
//			//Convert Strings to Int
//			for(int i=0;i<first.length;i++)
//			{
//				dateThen.add(Integer.parseInt(first[i]));
//				dateNow.add(Integer.parseInt(last[i]));
//			}
//			
//			//If in the same year
//			if(dateThen.get(0)==dateNow.get(0))
//			{
//				//Same month
//				if(dateThen.get(1)==dateNow.get(1))
//				{
//					//Subtract one day from the other and return it
//					return Integer.toString(dateNow.get(2)-dateThen.get(2));
//				}
//				else
//				{
//					if(dateNow.get(1)==1||dateNow.get(1)==5||dateNow.get(1)==7
//							||dateNow.get(1)==10||dateNow.get(1)==12)
//					{
//						return Integer.toString(dateNow.get(2)+30-dateThen.get(2));
//					}
//					else if()
//					{
//						re
//					}
//				}
//			}
//		}
//		catch(Exception e)
//		{
//			return "Parsing error";
//		}
//	}
	
	/**
	 * Calculate the difference in dates
	 * @return the number in String format, Error if an error occurs.
	 */
	public String calculate()
	{
		try
		{
		date1=new SimpleDateFormat("yyyy-MM-dd").parse(then);
		date2=new SimpleDateFormat("yyyy-MM-dd").parse(now);
		DateTime dateT2=new DateTime(date2.getTime());
		DateTime dateT1=new DateTime(date1.getTime());
		int days=Days.daysBetween(dateT1,dateT2).getDays();
		
		return Integer.toString(days);
		
		}
		catch(Exception e)
		{
			return "Error";
		}
		
	}
	

}
