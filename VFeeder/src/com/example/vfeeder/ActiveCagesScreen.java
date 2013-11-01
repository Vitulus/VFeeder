package com.example.vfeeder;

import java.util.ArrayList;

import com.example.helperMethods.DateReviewer;
import com.example.helperMethods.EmptyStringReviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActiveCagesScreen extends Activity implements OnClickListener{
	
	private Button compute,back;
	private EditText day,month,year;
	private Intent next;
	private ArrayList<EditText> list;
	
	protected void onCreate(Bundle savedInstanceState) {
		//Android commands to initiate
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_active_cages_screen);
		
		compute=(Button)this.findViewById(R.id.computeActiveCages);
		back=(Button)this.findViewById(R.id.backButtonActiveCages);
		
		day=(EditText)this.findViewById(R.id.dayActiveCages);
		month=(EditText)this.findViewById(R.id.monthActiveCages);
		year=(EditText)this.findViewById(R.id.yearActiveCages);
		
		compute.setOnClickListener(this);
		back.setOnClickListener(this);
		
	}
	
	//Android method
			@Override
			public boolean onCreateOptionsMenu(Menu menu) {
				// Inflate the menu; this adds items to the action bar if it is present.
				getMenuInflater().inflate(R.menu.active_cages_screen, menu);
				return true;
			}
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.backButtonActiveCages:
			next=new Intent(ActiveCagesScreen.this,ReportsScreen.class);
			startActivity(next);
			break;
		case R.id.computeActiveCages:
			//TODO
			list=new ArrayList<EditText>();
			list.add(month);
			list.add(day);
			list.add(year);
			
			if(new EmptyStringReviewer(list).reviseEmpty())
			{
				Toast.makeText(ActiveCagesScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();	
			}
			else if(new DateReviewer(list).reviseDate())
			{
				Toast.makeText(ActiveCagesScreen.this, "Incorrect date format", Toast.LENGTH_SHORT).show();	
			}
			else
			{
				
			}
			
			break;
		}
		
	}
	
	

}
