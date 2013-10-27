package com.example.vfeeder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
			new AlertDialog.Builder(this)
			.setTitle("Are you sure?")
			.setMessage("Do you really want to erase the data?")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int whichButton) {
					
					//Implement DB logic here
					
					Toast.makeText(EraseDataScreen.this, "Erased Data", Toast.LENGTH_SHORT).show();
				}})
				.setNegativeButton(android.R.string.no, null).show();

			break;
			
		case R.id.homeButtonED:
			next=new Intent(EraseDataScreen.this,WelcomeScreen.class);
			startActivity(next);
			break;
		}

	}


}
