package com.example.helperMethods;

import java.util.ArrayList;

import android.widget.EditText;

/**
 * @author einsteinboricua
 *A helper class to review if the EditText fields are empty
 */
public class EmptyStringReviewer {

	//ArrayList of EditText elements
	private ArrayList<EditText> list;
	
	
	/**
	 * The constructor that takes an ArrayList of EditText elements
	 * @param list, the ArrayList
	 */
	public EmptyStringReviewer(ArrayList<EditText> list)
	{
		this.list=list;
	}
	
	//Boolean method to check if a string is empty
	public boolean reviseEmpty()
	{
		for(EditText et: list)//Loop through all elements
		{
			if(et.getText().toString().length()==0)//If the string is empty, return true
				return true;
		}
		return false;//Return false otherwise
	}
}
