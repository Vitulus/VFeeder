package com.example.vfeeder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author einsteinboricua
 * The Reports screen class
 */
public class ReportsScreen extends Activity implements OnClickListener{

	//Variables
	private Button length, weight, temperature, dispense, home;//, active;
	private Intent next;
	
	protected void onCreate(Bundle savedInstanceState) {
		//Android commands to initiate
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reports_screen);
		//Initialize variables
		length=(Button)this.findViewById(R.id.activeCagesButton);
		weight=(Button)this.findViewById(R.id.throughWeightButton);
		temperature=(Button)this.findViewById(R.id.temperatureButton);
		dispense=(Button)this.findViewById(R.id.dispenseDataButton);
		home=(Button)this.findViewById(R.id.homeButtonReports);
		
		//Set listeners
		length.setOnClickListener(this);
		weight.setOnClickListener(this);
		temperature.setOnClickListener(this);
		dispense.setOnClickListener(this);
		home.setOnClickListener(this);
	
	}
	
	//Android method
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.reports_screen, menu);
			return true;
		}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		//Length of stay button clicked
		case R.id.activeCagesButton:
			next=new Intent(ReportsScreen.this,LengthOfStayScreen.class);
			//next=new Intent(ReportsScreen.this,ActiveCagesScreen.class);
			break;
		//Trough weight button clicked	
		case R.id.throughWeightButton:
			next=new Intent(ReportsScreen.this,TroughWeightScreen.class);
			break;
		//Temperature button clicked	
		case R.id.temperatureButton:
			next=new Intent(ReportsScreen.this,TemperatureScreen.class);
			break;
		//Dispense data button clicked	
		case R.id.dispenseDataButton:
			next=new Intent(ReportsScreen.this,DispenseDataScreen.class);
			break;
		//Home button clicked.	
		case R.id.homeButtonReports:
			next=new Intent(ReportsScreen.this,WelcomeScreen.class);
			break;
		}
		startActivity(next);
	}

}
