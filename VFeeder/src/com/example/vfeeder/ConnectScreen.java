package com.example.vfeeder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author einsteinboricua
 *This will implement the logic behind the connection.
 *The app, by default will not connect to internet unless
 *done by this screen.
 */
public class ConnectScreen extends Activity implements OnClickListener{
	
	//Variables
	private Button connect;
	private Intent next;
	private boolean connected;
	
	//Logic behind screen once launched
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect_screen);
		
		//Initiate button
		connect=(Button)this.findViewById(R.id.connectB);
		
		//Assign a ClickListener
		connect.setOnClickListener(this);
		
		//Boolean for connection
		connected=true;
	}
	
	//Show menu once the screen is created
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connect_screen, menu);
		return true;
	}
	
	//Once clicked, detect which button was clicked and do the appropriate action.
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(connected==true){
			next=new Intent(ConnectScreen.this,WelcomeScreen.class);
			startActivity(next);
		}
	}
}
