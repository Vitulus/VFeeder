package com.example.vfeeder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EraseDataScreen extends Activity implements OnClickListener{

	private Button erase, home;
	private Intent next;
	private EditText cageNumber;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_erase_data_screen);
		
		erase=(Button)this.findViewById(R.id.eraseButton);
		home=(Button)this.findViewById(R.id.homeButtonED);
		
		cageNumber=(EditText)this.findViewById(R.id.cageNumberFieldED);
		
		erase.setOnClickListener(this);
		home.setOnClickListener(this);
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.erase_data_screen, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.eraseButton:
			//TODO
			break;
		case R.id.homeButtonED:
			next=new Intent(EraseDataScreen.this,WelcomeScreen.class);
			startActivity(next);
			break;
		}
		
	}
	

}
