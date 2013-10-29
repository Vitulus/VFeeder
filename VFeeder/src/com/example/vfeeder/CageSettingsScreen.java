package com.example.vfeeder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CageSettingsScreen extends Activity implements OnClickListener{
	
	private Button set, home;
	private Intent next;
	private EditText cageNumber, foodLevels, waterLevels, time;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cage_settings_screen);
		
		set=(Button)this.findViewById(R.id.setButton);
		home=(Button)this.findViewById(R.id.homeButtonSet);
		
		cageNumber=(EditText)this.findViewById(R.id.cageNumberField);
		foodLevels=(EditText)this.findViewById(R.id.foodLevelsEdit);
		waterLevels=(EditText)this.findViewById(R.id.waterLevelsEdit);
		time=(EditText)this.findViewById(R.id.timeEdit);
		
		set.setOnClickListener(this);
		home.setOnClickListener(this);
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cage_settings_screen, menu);
		return true;
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.setButton:
			//TODO
			break;
		case R.id.homeButtonSet:
			next=new Intent(CageSettingsScreen.this, WelcomeScreen.class);
			startActivity(next);
			break;
			
		}
	}

}
