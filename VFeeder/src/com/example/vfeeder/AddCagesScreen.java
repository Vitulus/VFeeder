package com.example.vfeeder;

import java.util.ArrayList;

import com.example.helperMethods.EmptyStringReviewer;
import com.example.helperMethods.TimeReviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author einsteinboricua
 * This is the logic behind the screen that will
 * allow the user to add or activate cages.
 */
public class AddCagesScreen extends Activity implements OnClickListener{

	//Variables
	private Button add, home;
	private Intent next;
	private EditText cageNum, cageID, foodLevel, waterLevel, time;
	private ArrayList<EditText> list;
	
	//Logic behind screen once launched
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_cage_screen);
		
		//Initiate button
		add=(Button)this.findViewById(R.id.addButton);
		home=(Button)this.findViewById(R.id.homeButtonAC);
		
		//Initiate EditText fields
		cageNum=(EditText)this.findViewById(R.id.cageNumberField);
		cageID=(EditText)this.findViewById(R.id.cageIDField);
		foodLevel=(EditText)this.findViewById(R.id.foodLevelsField);
		waterLevel=(EditText)this.findViewById(R.id.waterLevelsField);
		time=(EditText)this.findViewById(R.id.setTimeField);
		
		
		//Add listener to buttons
		add.setOnClickListener(this);
		home.setOnClickListener(this);
	}
	
	//Show menu once the screen is created
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_cage_screen, menu);
		return true;
	}
	
	//Once clicked, detect which button was clicked and do the appropriate action.
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.addButton: //If add button is pressed
			//TODO
			list=new ArrayList<EditText>();
			list.add(cageNum);
			list.add(cageID);
			list.add(foodLevel);
			list.add(waterLevel);
			list.add(time);
			
			if(new EmptyStringReviewer(list).reviseEmpty())
			{
				Toast.makeText(AddCagesScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();
			}
			else if(new TimeReviewer(time).reviseTime())
			{
				Toast.makeText(AddCagesScreen.this, "Incorrect time format", Toast.LENGTH_SHORT).show();
			}
			else if((Integer.parseInt(cageNum.getText().toString()))<=0)
			{
				Toast.makeText(AddCagesScreen.this, "Cage number cannot be zero or below", Toast.LENGTH_SHORT).show();
			}
			else
			{
				
			}
			
			break;
			
		case R.id.homeButtonAC: //If home button is pressed
			next=new Intent(AddCagesScreen.this,WelcomeScreen.class);
			startActivity(next);
			break;
		}
	}

}
