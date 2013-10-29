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

public class DeactivateCagesScreen extends Activity implements OnClickListener{
	
	private Button delete, home;
	private Intent next;
	private EditText cageNumber;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deactivate_cage_screen);
		
		delete=(Button)this.findViewById(R.id.eraseBDC);
		home=(Button)this.findViewById(R.id.homeBDC);
		
		cageNumber=(EditText)this.findViewById(R.id.cageNumFieldDC);
		
		delete.setOnClickListener(this);
		home.setOnClickListener(this);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.deactivate_cage_screen, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.eraseBDC:
			//TODO
			new AlertDialog.Builder(this)
			.setTitle("Are you sure?")
			.setMessage("Do you really want to delete the cage?")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int whichButton) {
					
					//Implement DB logic here
					
					Toast.makeText(DeactivateCagesScreen.this, "Deleted Cage", Toast.LENGTH_SHORT).show();
				}})
				.setNegativeButton(android.R.string.no, null).show();
			break;
			
		case R.id.homeBDC:
			next=new Intent(DeactivateCagesScreen.this,WelcomeScreen.class);
			startActivity(next);
			break;
		}
	}

}
