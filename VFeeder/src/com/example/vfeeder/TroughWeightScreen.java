package com.example.vfeeder;

import java.util.ArrayList;

import com.example.helperMethods.DateReviewer;
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
import android.widget.TextView;
import android.widget.Toast;

public class TroughWeightScreen extends Activity implements OnClickListener{

	private Button read,back;
	private EditText cageNumber, month,day,year,time;
	private Intent next;
	private TextView food,water;
	private ArrayList<EditText> list;
	
	protected void onCreate(Bundle savedInstanceState) {
		//Android commands to initiate
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trough_weight_screen);
		
		read=(Button)this.findViewById(R.id.readButtonTroughWeight);
		back=(Button)this.findViewById(R.id.backButtonTroughWeight);
		
		cageNumber=(EditText)this.findViewById(R.id.selectCageFieldTroughWeight);
		month=(EditText)this.findViewById(R.id.monthActiveCages);
		day=(EditText)this.findViewById(R.id.dayActiveCages);
		year=(EditText)this.findViewById(R.id.yearActiveCages);
		time=(EditText)this.findViewById(R.id.selectTimeFieldTroughWeight);
		
		food=(TextView)this.findViewById(R.id.foodWeightReading);
		water=(TextView)this.findViewById(R.id.waterLevelsReading);
		
		read.setOnClickListener(this);
		back.setOnClickListener(this);
						
	}
	
	//Android method
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.trough_weight_screen, menu);
			return true;
		}
		
	
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.readButtonTroughWeight:
			//TODO
			list=new ArrayList<EditText>();
			
			list.add(month);
			list.add(day);
			list.add(year);
			list.add(cageNumber);
			list.add(time);
			
			if(new EmptyStringReviewer(list).reviseEmpty())
			{
				Toast.makeText(TroughWeightScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();	
			}
			else if(new DateReviewer(list).reviseDate())
			{
				Toast.makeText(TroughWeightScreen.this,"Incorrect date format",Toast.LENGTH_SHORT).show();
			}
			else if(new TimeReviewer(time).reviseTime())
			{
				Toast.makeText(TroughWeightScreen.this, "Incorrect time format", Toast.LENGTH_SHORT).show();
			}
			else if((Integer.parseInt(cageNumber.getText().toString()))<=0)
			{
				Toast.makeText(TroughWeightScreen.this, "Cage cannot be zero or below", Toast.LENGTH_SHORT).show();
			}
			else
			{
				
			}
			
			break;
			
		case R.id.backButtonTroughWeight:
			next=new Intent(TroughWeightScreen.this,ReportsScreen.class);
			startActivity(next);
			break;
		}
		
	}
}
