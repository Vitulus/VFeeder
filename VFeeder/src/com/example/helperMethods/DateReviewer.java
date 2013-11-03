package com.example.helperMethods;

import java.util.ArrayList;

import android.widget.EditText;
import android.widget.Toast;

/**
 * @author einsteinboricua
 * A class to help verify date parameters entered
 *
 */
public class DateReviewer {

	//ArrayList of EditText elements
	private int month,day,year;
	private String [] strings;
	private EditText date;


	/**
	 * The contructor that takes an ArrayList of EditText elements
	 * @param list, the ArrayList
	 */
	public DateReviewer(EditText date)
	{
		this.date=date;
	}


	/**
	 * Method to revise the dates
	 * @return true if one element does not comply; false, otherwise.
	 */
	public boolean reviseDate()
	{
		//Split date into strings to check for error.
		try
		{
		strings=date.getText().toString().split("-");
		//Convert text into int for easier analysis
		year=Integer.parseInt(strings[0]);
		month=Integer.parseInt(strings[1]);
		day=Integer.parseInt(strings[2]);
		}
		catch(Exception e)
		{
			return true; //If it's not with a "/", return true.
		}
				
		//If days are "negative", zero, or over 31, return true
		if(day<=0||day>31)
		{
			return true;
		}

		//If the month does not have 31 days but the day exceeds 30.
		if((month==2||month==4||month==6||month==9||month==11) && day>30)
		{
			return true;
		}

		//February case
		//If not a leap year and has over 28 days
		if(month==2 && day>28 && year%4!=0)
		{
			return true;
		}


		return false;//Return false otherwise
	}

}
