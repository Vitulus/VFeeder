package com.example.vfeeder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ReadScreen extends Activity implements OnClickListener{
	private Button home, read;
	private Intent next;
	private EditText box;
	private String number;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_data_screen);
		
		//Identify the button pressed
		home=(Button)this.findViewById(R.id.homeButton);
		read=(Button)this.findViewById(R.id.readButton);
		box=(EditText)this.findViewById(R.id.cageNumField);
				
		//Assign a ClickListener
		home.setOnClickListener(this);
		read.setOnClickListener(this);
	}
	
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.read_data_screen, menu);
		return true;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//Get ID of the button and enter switch cases
				switch(v.getId()){
				
				//If Home button is clicked
				case R.id.homeButton:
					next=new Intent(ReadScreen.this,WelcomeScreen.class);
					startActivity(next);
					break;
				
				//If Read button is clicked
				case R.id.readButton:
					try{
					Integer.parseInt(box.getText().toString());
					//Log.d("MyApp","I am here");
					}
					catch(Exception e){
						System.out.println("Error. Not a number");
					}
					break;
				}
		
	}
}
