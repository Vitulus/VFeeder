package com.example.vfeeder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DispenseScreen extends Activity implements OnClickListener{
	
	private Button home, dispense;
	private Intent next;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dispense_screen);
		
		//Identify the button pressed
		home=(Button)this.findViewById(R.id.homeButtonD);
		dispense=(Button)this.findViewById(R.id.dispenseButton);
		
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
		case R.id.homeButtonD:
			next=new Intent(DispenseScreen.this,WelcomeScreen.class);
			startActivity(next);
			break;
		}
	}

}
