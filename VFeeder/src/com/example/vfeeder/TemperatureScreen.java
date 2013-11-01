package com.example.vfeeder;

import java.util.ArrayList;

import com.example.helperMethods.DateReviewer;
import com.example.helperMethods.EmptyStringReviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TemperatureScreen extends Activity implements OnClickListener{

	private Button compute,back;
	private Intent next;
	private EditText cageNumber,day,month,year;
	private ArrayList<EditText> list;
	private TextView reading;
	
	protected void onCreate(Bundle savedInstanceState) {
		//Android commands to initiate
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temperature_screen);
		
		compute=(Button)this.findViewById(R.id.computeTemperature);
		back=(Button)this.findViewById(R.id.backButtonTemperature);
		
		day=(EditText)this.findViewById(R.id.dayActiveCages);
		month=(EditText)this.findViewById(R.id.monthActiveCages);
		year=(EditText)this.findViewById(R.id.yearActiveCages);		
		cageNumber=(EditText)this.findViewById(R.id.selectCageFieldTemperature);
		
		reading=(TextView)this.findViewById(R.id.readingTxtTemperature);
		
		compute.setOnClickListener(this);
		back.setOnClickListener(this);
		reading.setText("");
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
			list=new ArrayList<EditText>();
			list.add(month);
			list.add(day);
			list.add(year);
			list.add(cageNumber);
			
			if(new EmptyStringReviewer(list).reviseEmpty())
			{
				Toast.makeText(TemperatureScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();
			}
			else if(new DateReviewer(list).reviseDate())
			{
				Toast.makeText(TemperatureScreen.this, "Incorrect date format", Toast.LENGTH_SHORT).show();
			}
			else if((Integer.parseInt(cageNumber.getText().toString()))<=0)
			{
				Toast.makeText(TemperatureScreen.this, "Cage number cannot be zero or below",
						Toast.LENGTH_SHORT).show();
			}
			else
			{
				
			}
			break;
		}
	}
	

}
