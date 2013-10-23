package com.example.vfeeder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddCagesScreen extends Activity implements OnClickListener{

	private Button add, home;
	private Intent next;
	private EditText cageNum, cageID, foodLevel, waterLevel;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_cage_screen);
		
		add=(Button)this.findViewById(R.id.addButton);
		home=(Button)this.findViewById(R.id.homeButtonAC);
		
		cageNum=(EditText)this.findViewById(R.id.cageNumberField);
		cageID=(EditText)this.findViewById(R.id.cageIDField);
		foodLevel=(EditText)this.findViewById(R.id.foodLevelsField);
		waterLevel=(EditText)this.findViewById(R.id.waterLevelsField);
		
		add.setOnClickListener(this);
		home.setOnClickListener(this);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_cage_screen, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.addButton:
			//TODO
			break;
			
		case R.id.homeButtonAC:
			next=new Intent(AddCagesScreen.this,WelcomeScreen.class);
			startActivity(next);
			break;
		}
	}

}
