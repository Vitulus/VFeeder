package com.example.vfeeder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

public class DispenseScreen extends Activity implements OnClickListener{
	
	private Button home, dispense;
	private Intent next;
	private ToggleButton allCages;
	private EditText cageNumber;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dispense_screen);
		
		//Initialize variables
		home=(Button)this.findViewById(R.id.homeButtonD);
		dispense=(Button)this.findViewById(R.id.dispenseButton);
		
		allCages=(ToggleButton)this.findViewById(R.id.allCages);
		
		cageNumber=(EditText)this.findViewById(R.id.selectCageField);
		
		
		
		//Assign a ClickListener
		home.setOnClickListener(this);
		dispense.setOnClickListener(this);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dispense_screen, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		case R.id.dispenseButton:
			if((Integer.parseInt(cageNumber.getText().toString()))<=0)
			{
				Toast.makeText(DispenseScreen.this, "Cage number cannot be zero or below", Toast.LENGTH_SHORT).show();
			}
			else
			{
				if(allCages.isChecked())
				{
					
				}
				else
				{
					
				}
				//TODO
			}
			break;
			
		case R.id.homeButtonD:
			next=new Intent(DispenseScreen.this,WelcomeScreen.class);
			startActivity(next);
			break;
		}
	}

}
