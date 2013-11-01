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

public class CageSettingsScreen extends Activity implements OnClickListener{
	
	private Button set, home;
	private Intent next;
	private EditText cageNumber, foodLevels, waterLevels, time;
	private ArrayList<EditText> list;
	
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
			list=new ArrayList<EditText>();
			list.add(cageNumber);
			list.add(foodLevels);
			list.add(waterLevels);
			list.add(time);
			
			if(new EmptyStringReviewer(list).reviseEmpty())
			{
				Toast.makeText(CageSettingsScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();
			}
			else if(new TimeReviewer(time).reviseTime())
			{
				Toast.makeText(CageSettingsScreen.this, "Incorrect time format", Toast.LENGTH_SHORT).show();
			}
			else if((Integer.parseInt(cageNumber.getText().toString()))<=0)
			{
				Toast.makeText(CageSettingsScreen.this, "Cage number cannot be zero or below", Toast.LENGTH_SHORT).show();
			}
			else
			{
				//TODO
				
			}
			break;
		case R.id.homeButtonSet:
			next=new Intent(CageSettingsScreen.this, WelcomeScreen.class);
			startActivity(next);
			break;
			
		}
	}

}
