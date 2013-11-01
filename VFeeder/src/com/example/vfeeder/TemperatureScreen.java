package com.example.vfeeder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class TemperatureScreen extends Activity implements OnClickListener{

	private Button compute,back;
	private Intent next;
	private EditText cage,day,month,year;
	
	protected void onCreate(Bundle savedInstanceState) {
		//Android commands to initiate
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temperature_screen);
		
		compute=(Button)this.findViewById(R.id.computeTemperature);
		back=(Button)this.findViewById(R.id.backButtonTemperature);
		
		day=(EditText)this.findViewById(R.id.dayActiveCages);
		month=(EditText)this.findViewById(R.id.monthActiveCages);
		year=(EditText)this.findViewById(R.id.yearActiveCages);
		
		cage=(EditText)this.findViewById(R.id.selectCageFieldTemperature);
		
		compute.setOnClickListener(this);
		back.setOnClickListener(this);
		
	}
	
	
	//Android method
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.temperature_screen, menu);
		return true;
	}
	
	
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.backButtonTemperature:
			next=new Intent(TemperatureScreen.this,ReportsScreen.class);
			startActivity(next);
			break;
			
		case R.id.computeTemperature:
			//TODO
			break;
		}
	}
	

}
