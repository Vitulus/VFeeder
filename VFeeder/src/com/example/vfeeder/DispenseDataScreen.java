package com.example.vfeeder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class DispenseDataScreen extends Activity implements OnClickListener{

	//Variables
	private Button compute, back;
	private Intent next;
	private EditText cageNumber, date;
	private TextView food,water,time;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		//Android commands to initiate
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dispense_data_screen);
		
		compute=(Button)this.findViewById(R.id.computeButtonDispense);
		back=(Button)this.findViewById(R.id.backButtonDispenseReport);
		
		date=(EditText)this.findViewById(R.id.dateFieldDispenseReport);
		
		cageNumber=(EditText)this.findViewById(R.id.cageNumberFieldDispenseReport);
		
		food=(TextView)this.findViewById(R.id.foodAmountReading);
		water=(TextView)this.findViewById(R.id.waterAmountReading);
		time=(TextView)this.findViewById(R.id.dispenseTimeDispenseReport);
		
		food.setText("");
		water.setText("");
		time.setText("");
		
		compute.setOnClickListener(this);
		back.setOnClickListener(this);
		
	}
	
	
	//Android method
			@Override
			public boolean onCreateOptionsMenu(Menu menu) {
				// Inflate the menu; this adds items to the action bar if it is present.
				getMenuInflater().inflate(R.menu.dispense_data_screen, menu);
				return true;
			}
	
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.computeButtonDispense:
			//TODO
			food.setText("1");
			water.setText("1");
			time.setText("12:45");
			break;
			
		case R.id.backButtonDispenseReport:
			next=new Intent(DispenseDataScreen.this,ReportsScreen.class);
			startActivity(next);
			break;
		}
	}

}
