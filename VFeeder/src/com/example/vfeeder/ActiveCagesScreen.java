package com.example.vfeeder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import com.example.helperMethods.DateReviewer;
import com.example.helperMethods.EmptyStringReviewer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author einsteinboricua
 *A class to detect the number of active cages in the system
 */
public class ActiveCagesScreen extends Activity implements OnClickListener{

	//Variables
	private Button compute,back;
	private EditText day,month,year, date;
	private Intent next;
	private ArrayList<EditText> list;
	private TextView numberCages;

	private HttpPost post;
	private HttpResponse response;
	private HttpClient client;
	private List<NameValuePair> nameValuePair;
	private ProgressDialog dialog=null;
	private Thread thread=new Thread(new Runnable(){
		public void run(){
			activeCages();
		}});

	protected void onCreate(Bundle savedInstanceState) {
		//Android commands to initiate
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_active_cages_screen);

		//Initialization of variables
		compute=(Button)this.findViewById(R.id.computeLengthOfStay);
		back=(Button)this.findViewById(R.id.backButtonLengthOfStay);

		date=(EditText)this.findViewById(R.id.dateFieldActiveCages);

		/*
		day=(EditText)this.findViewById(R.id.dayActiveCages);
		month=(EditText)this.findViewById(R.id.monthActiveCages);
		year=(EditText)this.findViewById(R.id.yearActiveCages);
		 */
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
	
	//Once clicked...
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		//Back button is pressed
		case R.id.backButtonLengthOfStay:
			next=new Intent(ActiveCagesScreen.this,ReportsScreen.class);
			startActivity(next);
			break;
			
			//Compute button is pressed
		case R.id.computeLengthOfStay:
			if(!isNetworkAvailable())
			{
				Toast.makeText(ActiveCagesScreen.this, "No internet connection", Toast.LENGTH_SHORT).show();
			}
			else
			{
			//If the field is empty
			if(date.getText().toString().length()==0)
			{
				Toast.makeText(ActiveCagesScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();	
			}
			//Check the format
			else if(new DateReviewer(date).reviseDate())
			{
				Toast.makeText(ActiveCagesScreen.this, "Incorrect date format", Toast.LENGTH_SHORT).show();	
			}
			//Everything's OK. Begin...
			else
			{
				dialog=ProgressDialog.show(ActiveCagesScreen.this,"","Verifying...",true);
				if(thread.getState()==Thread.State.NEW)
					thread.start();
				else
				{
					thread.interrupt();
					thread=new Thread(new Runnable(){
						public void run(){
							activeCages();
						}});
					thread.start();				
				}

				break;
			}
		}
		}

	}

	//Method for thread.
	public void activeCages()
	{
		try
		{
			//Establish connection
			client=new DefaultHttpClient();
			post=new HttpPost("http://www.vitulustech.com/activeCagesScript.php");

			//Give elements to PHP Script
			nameValuePair=new ArrayList<NameValuePair>(1);
			nameValuePair.add(new BasicNameValuePair("Date",date.getText().toString().trim()));

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
						Toast.makeText(ActiveCagesScreen.this, "Success", Toast.LENGTH_SHORT).show();	

					}
				});
			}
			//If there is a repeated cage
			else if(response.equalsIgnoreCase("Not found"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(ActiveCagesScreen.this, "No active cages",Toast.LENGTH_SHORT).show();
					}
				});
			}
			//An error
			else
			{
				Toast.makeText(ActiveCagesScreen.this, "Error", Toast.LENGTH_SHORT).show();
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
