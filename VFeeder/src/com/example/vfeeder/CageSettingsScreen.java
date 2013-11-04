package com.example.vfeeder;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.example.helperMethods.EmptyStringReviewer;
import com.example.helperMethods.TimeReviewer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author einsteinboricua
 *Assign settings to a cage.
 */
public class CageSettingsScreen extends Activity implements OnClickListener{

	//Variables
	private Button set, home;
	private Intent next;
	private EditText cageNumber, foodLevels, waterLevels, time;
	private ArrayList<EditText> list;

	private HttpPost post;
	private HttpResponse response;
	private HttpClient client;
	private List<NameValuePair> nameValuePair;
	private ProgressDialog dialog=null;
	private Thread thread=new Thread(new Runnable(){
		public void run(){
			updateCage();
		}});

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cage_settings_screen);

		//Initialize variables
		set=(Button)this.findViewById(R.id.setButton);
		home=(Button)this.findViewById(R.id.homeButtonSet);

		cageNumber=(EditText)this.findViewById(R.id.cageNumField1);
		foodLevels=(EditText)this.findViewById(R.id.foodLevelsEdit);
		waterLevels=(EditText)this.findViewById(R.id.waterLevelsEdit);
		time=(EditText)this.findViewById(R.id.timeEdit);
		
		//Set listeners
		set.setOnClickListener(this);
		home.setOnClickListener(this);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cage_settings_screen, menu);
		return true;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.setButton:
			//TODO
			//Add fields to ArrayList for verification
			list=new ArrayList<EditText>();
			list.add(cageNumber);
			list.add(foodLevels);
			list.add(waterLevels);
			list.add(time);

			//Check if any field is empty
			if(new EmptyStringReviewer(list).reviseEmpty())
			{
				Toast.makeText(CageSettingsScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();
			}
		//Check time format entered
			else if(new TimeReviewer(time).reviseTime())
			{
				Toast.makeText(CageSettingsScreen.this, "Incorrect time format", Toast.LENGTH_SHORT).show();
			}
			//Check if cage number is zero
			else if((Integer.parseInt(cageNumber.getText().toString()))<=0)
			{
				Toast.makeText(CageSettingsScreen.this, "Cage number cannot be zero or below", Toast.LENGTH_SHORT).show();
			}
			else
			{
				//TODO
				//Data is good. Begin process to update cage.
				dialog=ProgressDialog.show(CageSettingsScreen.this,"","Updating settings...",true);
				if(thread.getState()==Thread.State.NEW)
					thread.start();
					else
					{
						thread.interrupt();
						thread=new Thread(new Runnable(){
							public void run(){
								updateCage();
							}});
						thread.start();
					}
			}
			break;
			//Go back home
		case R.id.homeButtonSet:
			next=new Intent(CageSettingsScreen.this, WelcomeScreen.class);
			startActivity(next);
			break;

		}
	}

	//Internal method for thread
	public void updateCage()
	{
		try{
			//Establish connection
			client=new DefaultHttpClient();
			post=new HttpPost("http://www.vitulustech.com/updateCageScript.php");

			//Give elements to PHP Script
			nameValuePair=new ArrayList<NameValuePair>(4);
			nameValuePair.add(new BasicNameValuePair("CageNum",cageNumber.getText().toString().trim()));
			nameValuePair.add(new BasicNameValuePair("FoodAmount",foodLevels.getText().toString().trim()));
			nameValuePair.add(new BasicNameValuePair("WaterAmount",waterLevels.getText().toString().trim()));
			nameValuePair.add(new BasicNameValuePair("Time",time.getText().toString().trim()));

			post.setEntity(new UrlEncodedFormEntity(nameValuePair));
			//response=client.execute(post);

			//Listen for response
			ResponseHandler<String> handler=new BasicResponseHandler();
			final String response=client.execute(post, handler);

			//If everything is successful...
			if(response.equalsIgnoreCase("Success"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(CageSettingsScreen.this, "Success", Toast.LENGTH_SHORT).show();	
					}
				});
			}
			//If there is a repeated cage
			else if(response.equalsIgnoreCase("Not Found"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(CageSettingsScreen.this, "Cage does not exist",Toast.LENGTH_SHORT).show();
					}
				});
			}
			//An error
			else
			{
				Toast.makeText(CageSettingsScreen.this, "Error updating cage", Toast.LENGTH_SHORT).show();
			}

		}
		catch(Exception e)
		{

		}
		finally
		{
			thread.interrupt();
			dialog.dismiss();
		}

	}
}
