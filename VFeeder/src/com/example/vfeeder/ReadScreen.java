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
import android.widget.Toast;

public class ReadScreen extends Activity implements OnClickListener{
	private Button home, read;
	private Intent next;
	private EditText cageNumber;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_data_screen);

		//Identify the button pressed
		home=(Button)this.findViewById(R.id.homeButton);
		read=(Button)this.findViewById(R.id.readButton);
		cageNumber=(EditText)this.findViewById(R.id.cageNumField);

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
			if((Integer.parseInt(cageNumber.getText().toString()))<=0)
			{
				Toast.makeText(ReadScreen.this, "Cage number cannot be zero or below",
						Toast.LENGTH_SHORT).show();
			}
			else
			{

			}
			break;
		}

	}
}
