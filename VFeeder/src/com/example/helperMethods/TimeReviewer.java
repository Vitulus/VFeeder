package com.example.helperMethods;

import android.widget.EditText;

/**
 * @author einsteinboricua
 *A class to verify time string formats
 */
public class TimeReviewer {

	//Variables
	private String timeString;
	private EditText time;
	private String[] array;
	private int hour,minute;

	//Constructor
	public TimeReviewer(EditText time)
	{
		this.time=time;
	}

	//Method to revise the time parameters
	public boolean reviseTime()
	{
		timeString=time.getText().toString();
		array=timeString.split(":");
		hour=Integer.parseInt(array[0]);
		minute=Integer.parseInt(array[1]);

		if((hour<0||hour>24) || (minute<0||minute>=60))
		{
			return true;
		}

		return false;

	}

}
