package com.example.vfeeder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ConnectScreen extends Activity implements OnClickListener{
	
	private Button connect;
	private Intent next;
	private boolean connected;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect_screen);
		
		//Identify the button pressed
		connect=(Button)this.findViewById(R.id.connectB);
		
		//Assign a ClickListener
		connect.setOnClickListener(this);
		
		connected=true;
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connect_screen, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(connected==true){
			next=new Intent(ConnectScreen.this,WelcomeScreen.class);
			startActivity(next);
		}
	}
}
