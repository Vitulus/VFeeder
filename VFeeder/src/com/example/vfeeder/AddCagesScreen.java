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
 * This is the logic behind the screen that will
 * allow the user to add or activate cages.
 */
public class AddCagesScreen extends Activity implements OnClickListener{

	//Variables
	private Button add, home;
	private Intent next;
	private EditText cageNum, cageID, foodLevel, waterLevel, time;
	private ArrayList<EditText> list;
	
	private HttpPost post;
	private HttpResponse response;
	private HttpClient client;
	private List<NameValuePair> nameValuePair;
	private ProgressDialog dialog=null;
	private Thread thread=new Thread(new Runnable(){
		public void run(){
			addCages();
		}});
	
	
	//Logic behind screen once launched
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_cage_screen);
		
		//Initiate button
		add=(Button)this.findViewById(R.id.addButton);
		home=(Button)this.findViewById(R.id.homeButtonAC);
		
		//Initiate EditText fields
		cageNum=(EditText)this.findViewById(R.id.cageNumberField);
		cageID=(EditText)this.findViewById(R.id.cageIDField);
		foodLevel=(EditText)this.findViewById(R.id.foodLevelsField);
		waterLevel=(EditText)this.findViewById(R.id.waterLevelsField);
		time=(EditText)this.findViewById(R.id.setTimeField);
		
		//Add listener to buttons
		add.setOnClickListener(this);
		home.setOnClickListener(this);
	}
	
	//Show menu once the screen is created
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_cage_screen, menu);
		return true;
	}
	
	//Once clicked, detect which button was clicked and do the appropriate action.
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.addButton: //If add button is pressed
			//TODO
			
			//Add elements to ArrayList
			list=new ArrayList<EditText>();
			list.add(cageNum);
			list.add(cageID);
			list.add(foodLevel);
			list.add(waterLevel);
			list.add(time);
			
			//Use Reviewers and display appropriate message if condition is met.
			if(new EmptyStringReviewer(list).reviseEmpty())
			{
				Toast.makeText(AddCagesScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();
			}
			else if(new TimeReviewer(time).reviseTime())
			{
				Toast.makeText(AddCagesScreen.this, "Incorrect time format", Toast.LENGTH_SHORT).show();
			}
			else if((Integer.parseInt(cageNum.getText().toString()))<=0)
			{
				Toast.makeText(AddCagesScreen.this, "Cage number cannot be zero or below", Toast.LENGTH_SHORT).show();
			}
			else
			{
				//Data is good. Begin process to add cage.
				dialog=ProgressDialog.show(AddCagesScreen.this,"","Adding cage...",true);
				if(thread.getState()==Thread.State.NEW)
					thread.start();
					else
					{
						thread.interrupt();
						thread=new Thread(new Runnable(){
							public void run(){
								addCages();
							}});
						thread.start();
					}
			}			
			break;
			
		case R.id.homeButtonAC: //If home button is pressed
			next=new Intent(AddCagesScreen.this,WelcomeScreen.class);//Go to Welcome Screen
			startActivity(next);
			break;
		}
	}

	//Internal Method to add cages
	public void addCages()
	{
		try
		{
			//Establish connection
			client=new DefaultHttpClient();
			post=new HttpPost("http://www.vitulustech.com/addCagesScript.php");
			
			//Give elements to PHP Script
			nameValuePair=new ArrayList<NameValuePair>(5);
			nameValuePair.add(new BasicNameValuePair("CageID",cageID.getText().toString().trim()));
			nameValuePair.add(new BasicNameValuePair("CageNum",cageNum.getText().toString().trim()));
			nameValuePair.add(new BasicNameValuePair("FoodAmount",foodLevel.getText().toString().trim()));
			nameValuePair.add(new BasicNameValuePair("WaterAmount",waterLevel.getText().toString().trim()));
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
						Toast.makeText(AddCagesScreen.this, "Success", Toast.LENGTH_SHORT).show();	
						
					}
				});
			}
			//If there is a repeated cage
			else if(response.equalsIgnoreCase("Repeated"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(AddCagesScreen.this, "Cage already exists",Toast.LENGTH_SHORT).show();
					}
				});
			}
			//An error
			else
			{
				Toast.makeText(AddCagesScreen.this, "Error adding cage", Toast.LENGTH_SHORT).show();
			}
			
		}
		catch(Exception e)
		{
			dialog.dismiss();
			
		}
		finally
		{
			thread.interrupt();
		}
		
	}

}
