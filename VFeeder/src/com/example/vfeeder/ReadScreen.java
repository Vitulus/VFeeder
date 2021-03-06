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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author einsteinboricua
 * Read screen.
 */
public class ReadScreen extends Activity implements OnClickListener{
	//Variable
	private Button home, read;
	private Intent next;
	private EditText cageNumber;
	private TextView silo, temperature;//food,temperature;

	private String [] success;
	private HttpPost post;
	private HttpResponse response;
	private HttpClient client;
	private List<NameValuePair> nameValuePair;
	private ProgressDialog dialog=null;
	private Thread thread=new Thread(new Runnable(){
		public void run(){
			readCages();
		}});
	private int weight, level;
	private double temp, tempWeight;
	private String tempStr, weightStr, siloLevelStr;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_data_screen);

		//Identify the button pressed
		home=(Button)this.findViewById(R.id.homeButton);
		read=(Button)this.findViewById(R.id.readButton);
		cageNumber=(EditText)this.findViewById(R.id.cageNumField);
		silo=(TextView)this.findViewById(R.id.siloLevelReading);
		//food=(TextView)this.findViewById(R.id.foodLevelsRead);
		temperature=(TextView)this.findViewById(R.id.temperatureReadingRead);


		//Assign a ClickListener
		home.setOnClickListener(this);
		read.setOnClickListener(this);

		//Set the TextViews to blank
		silo.setText("");
		//food.setText("");
		temperature.setText("");
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
			//If field is empty
			if(!isNetworkAvailable())
			{
				Toast.makeText(ReadScreen.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
			}
			else{
				if(cageNumber.getText().toString().length()==0)
				{
					Toast.makeText(ReadScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();
				}
				//If cage number is equal or below zero
				else if((Integer.parseInt(cageNumber.getText().toString()))<=0)
				{
					Toast.makeText(ReadScreen.this, "Cage number cannot be zero or below",
							Toast.LENGTH_SHORT).show();
				}
				else
				{
					//Data is good. Begin process to read cage.
					dialog=ProgressDialog.show(ReadScreen.this,"","Reading...",true);
					if(thread.getState()==Thread.State.NEW)
						thread.start();
					else
					{
						thread.interrupt();
						thread=new Thread(new Runnable(){
							public void run(){
								readCages();
							}});
						thread.start();
					}
				}
				break;
			}
		}

	}

	public void readCages()
	{
		try
		{
			//Establish connection
			client=new DefaultHttpClient();
			post=new HttpPost("http://www.vitulustech.com/readRecentCageScript.php");

			//Give elements to PHP Script
			nameValuePair=new ArrayList<NameValuePair>(1);
			nameValuePair.add(new BasicNameValuePair("CageNum",cageNumber.getText().toString().trim()));

			post.setEntity(new UrlEncodedFormEntity(nameValuePair));
			//response=client.execute(post);
			Thread.sleep(1000);
			//Listen for response
			ResponseHandler<String> handler=new BasicResponseHandler();
			final String response=client.execute(post, handler);

			try{

				success=response.split("/");
			}
			catch(Exception e)
			{	
				success[0]="No";
				Toast.makeText(ReadScreen.this, "Parse error", Toast.LENGTH_SHORT).show();
			}
			//If everything is successful...
			if(success[0].equalsIgnoreCase("Success"))
			{

				temp=Double.parseDouble(success[1]);
				temp=temp/100;
				tempStr=String.valueOf(temp);

				
				
				weight=Integer.parseInt(success[2],16);
				tempWeight=weight/100.0;
				weightStr=String.valueOf(tempWeight);
				
				level=Integer.parseInt(success[3]);
				
				
				//temperature.setText("Error");


				runOnUiThread(new Runnable(){
					public void run(){
						temperature.setText(tempStr+" Celcius");
						if(temp>37.5)
						{
							temperature.setTextColor(Color.rgb(255, 0, 0));
						}
						
						//food.setText(weightStr+" pounds");
						
						if(level==1){
							siloLevelStr="Acceptable";
							silo.setText("Acceptable");
							silo.setTextColor(Color.rgb(0, 255, 0));
						}
						else if(level==0)
						{
							siloLevelStr="Running low";
							silo.setText(siloLevelStr);
							silo.setTextColor(Color.rgb(255, 0, 0));
						}
							
						//food.setText(success[2].toString());
						//silo.setText(success[3].toString());
						Toast.makeText(ReadScreen.this, "Success", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						thread.interrupt();
					}
				});


			}
			//Cage not found
			else if(response.equalsIgnoreCase("Not found"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(ReadScreen.this, "Cage does not exist",Toast.LENGTH_SHORT).show();
					}
				});
			}
			//An error
			else
			{
				Toast.makeText(ReadScreen.this, "Error", Toast.LENGTH_SHORT).show();
			}

		}
		catch(Exception e)
		{

		}
		finally
		{
			dialog.dismiss();
			thread.interrupt();
		}
	}

	//Method to detect Internet Connection
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
