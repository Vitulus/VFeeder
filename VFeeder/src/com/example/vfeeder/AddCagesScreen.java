package com.example.vfeeder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author einsteinboricua
 * This is the logic behind the screen that will
 * allow the user to add or activate cages.
 */
public class AddCagesScreen extends Activity implements OnClickListener{

	//Variables
	private Button add, home;
	private Intent next;
	private EditText cageNum, cageID, foodLevel, waterLevel;
	
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
			break;
			
		case R.id.homeButtonAC: //If home button is pressed
			next=new Intent(AddCagesScreen.this,WelcomeScreen.class);
			startActivity(next);
			break;
		}
	}

}
