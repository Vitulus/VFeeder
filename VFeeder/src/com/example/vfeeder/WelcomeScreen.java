package com.example.vfeeder;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author einsteinboricua
 *The main class once the user logins.
 */
public class WelcomeScreen extends Activity implements OnClickListener{

	//Variables
	private Button dispense, readData, setData, settings, disconnect, reports, logOut;
	private Intent next;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Android commands to initiate
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_screen);
		
		//Identify and assign the buttons
		dispense=(Button)this.findViewById(R.id.dispenseB);
		readData=(Button)this.findViewById(R.id.readB);
		setData=(Button)this.findViewById(R.id.setB);
		settings=(Button)this.findViewById(R.id.settingsB);
		disconnect=(Button)this.findViewById(R.id.disconnectB);
		reports=(Button)this.findViewById(R.id.reportsButton);
		logOut=(Button)this.findViewById(R.id.logOutButton);
		
		//Set OnClickListeners to each button
		dispense.setOnClickListener(this);
		readData.setOnClickListener(this);
		setData.setOnClickListener(this);
		settings.setOnClickListener(this);
		disconnect.setOnClickListener(this);
		reports.setOnClickListener(this);
		logOut.setOnClickListener(this);
		
		
	}

	//Android method
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome_screen, menu);
		return true;
	}

	//Method to listen and act based on the button pressed.
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//Get ID of the button and enter switch cases
		switch(v.getId()){
		
		//If Dispense button is clicked
		case R.id.dispenseB:
			next=new Intent(WelcomeScreen.this,DispenseScreen.class);
			break;
		
		//If Read button is clicked
		case R.id.readB:
			next=new Intent(WelcomeScreen.this,ReadScreen.class);
			break;
		
		//If Set button is clicked
		case R.id.setB:
			next=new Intent(WelcomeScreen.this,CageSettingsScreen.class);
			break;
		
		//If Disconnect button is clicked
		case R.id.disconnectB:
			next=new Intent(WelcomeScreen.this,ConnectScreen.class);
			break;
		
		//If Settings button is clicked
		case R.id.settingsB:
			next=new Intent(WelcomeScreen.this,ApplicationSettingsScreen.class);
			break;	
		
		//If Reports button is clicked
		case R.id.reportsButton:
			next=new Intent(WelcomeScreen.this,ReportsScreen.class);
			break;
			
		//If Log out is selected
		case R.id.logOutButton:
			next=new Intent(WelcomeScreen.this,LoginScreen.class);
			finish();
			break;
		}			
		//Go to next page
		startActivity(next);
	}

}
