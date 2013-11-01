package com.example.helperMethods;

import java.util.ArrayList;

import android.widget.EditText;

/**
 * @author einsteinboricua
 * A class to help verify date parameters entered
 *
 */
public class DateReviewer {

	//ArrayList of EditText elements
	private ArrayList<EditText> list;
	private int month,day,year;



	/**
	 * The contructor that takes an ArrayList of EditText elements
	 * @param list, the ArrayList
	 */
	public DateReviewer(ArrayList<EditText> list)
	{
		this.list=list;
	}


	/**
	 * Method to revise the dates
	 * @return true if one element does not comply; false, otherwise.
	 */
	public boolean reviseDate()
	{
		//Convert text into int for easier analysis
		month=Integer.parseInt(list.get(0).getText().toString());
		day=Integer.parseInt(list.get(1).getText().toString());
		year=Integer.parseInt(list.get(2).getText().toString());

		//If days are "negative", zero, or over 31, return true
		if(day<=0||day>31)
		{
			return true;
		}

		//If the month does not have 31 days but the day exceeds 31.
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
