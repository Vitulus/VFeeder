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
 *Menu screen for Application Settings.
 */
public class ApplicationSettingsScreen extends Activity implements OnClickListener{

	//Variables
	private Button addCages, deleteCages, eraseData, home, reactivate;
	private Intent next;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_application_settings_screen);
		
		//Initialize variables
		addCages=(Button)this.findViewById(R.id.addCagesB);
		deleteCages=(Button)this.findViewById(R.id.deleteCagesB);
		eraseData=(Button)this.findViewById(R.id.eraseDataB);
		home=(Button)this.findViewById(R.id.homeB);
		reactivate=(Button)this.findViewById(R.id.reactivateButton);
		
		//Assign listeners
		addCages.setOnClickListener(this);
		deleteCages.setOnClickListener(this);
		eraseData.setOnClickListener(this);
		home.setOnClickListener(this);
		reactivate.setOnClickListener(this);
		
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.application_settings_screen, menu);
		return true;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		//Add cages
		case R.id.addCagesB:
			next=new Intent(ApplicationSettingsScreen.this,AddCagesScreen.class);
			break;
			//Deactivate cages
		case R.id.deleteCagesB:
			next=new Intent(ApplicationSettingsScreen.this,DeactivateCagesScreen.class);
			break;
			//Erase data
		case R.id.eraseDataB:
			next=new Intent(ApplicationSettingsScreen.this,EraseDataScreen.class);
			break;
			//Reactivate a cage
		case R.id.reactivateButton:
			next=new Intent(ApplicationSettingsScreen.this,ReactivateCageScreen.class);
			break;
			//Go back to home screen
		case R.id.homeB:
			next=new Intent(ApplicationSettingsScreen.this,WelcomeScreen.class);
			break;
		}
		startActivity(next);
		}
	}


