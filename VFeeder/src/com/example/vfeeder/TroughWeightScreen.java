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

import com.example.helperMethods.DateReviewer;
import com.example.helperMethods.EmptyStringReviewer;
import com.example.helperMethods.TimeReviewer;

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
 * This class is to detect the trough weight from previous readings.
 *
 */
public class TroughWeightScreen extends Activity implements OnClickListener{

	//Variables
	private Button read,back;
	private EditText cageNumber, date; //month,day,year;
	private Intent next;
	private TextView food,water;
	private ArrayList<EditText> list;
	private String[] success;
	private HttpPost post;
	private HttpResponse response;
	private HttpClient client;
	private List<NameValuePair> nameValuePair;
	private ProgressDialog dialog=null;
	private Thread thread=new Thread(new Runnable(){
		public void run(){
			troughWeight();
		}});

	protected void onCreate(Bundle savedInstanceState) {
		//Android commands to initiate
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trough_weight_screen);

		//Initialize variables
		read=(Button)this.findViewById(R.id.readButtonTroughWeight);
		back=(Button)this.findViewById(R.id.backButtonTroughWeight);

		cageNumber=(EditText)this.findViewById(R.id.selectCageFieldTroughWeight);
		//		month=(EditText)this.findViewById(R.id.monthActiveCages);
		//		day=(EditText)this.findViewById(R.id.dayActiveCages);
		//		year=(EditText)this.findViewById(R.id.yearActiveCages);
		//		time=(EditText)this.findViewById(R.id.selectTimeFieldTroughWeight);
		date=(EditText)this.findViewById(R.id.dateTroughWeight);

		food=(TextView)this.findViewById(R.id.foodWeightReading);
		water=(TextView)this.findViewById(R.id.waterLevelsReading);

		//Add listeners
		read.setOnClickListener(this);
		back.setOnClickListener(this);
		
		food.setText("");
		water.setText("");

	}

	//Android method
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trough_weight_screen, menu);
		return true;
	}


	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		//Read button clicked
		case R.id.readButtonTroughWeight:
			
			if(!isNetworkAvailable())
			{
				Toast.makeText(TroughWeightScreen.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
			}
			
			else
			{
			//Add elements to list for verification
			list=new ArrayList<EditText>();

			//			list.add(month);
			//			list.add(day);
			//			list.add(year);
			list.add(cageNumber);
			list.add(date);
			//list.add(time);

			//Check if empty
			if(new EmptyStringReviewer(list).reviseEmpty())
			{
				Toast.makeText(TroughWeightScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();	
			}
			//Check the date format
			else if(new DateReviewer(date).reviseDate())
			{
				Toast.makeText(TroughWeightScreen.this,"Incorrect date format",Toast.LENGTH_SHORT).show();
			}
			//			else if(new TimeReviewer(time).reviseTime())
			//			{
			//				Toast.makeText(TroughWeightScreen.this, "Incorrect time format", Toast.LENGTH_SHORT).show();
			//			}
			//Check if cage number is zero or below
			else if((Integer.parseInt(cageNumber.getText().toString()))<=0)
			{
				Toast.makeText(TroughWeightScreen.this, "Cage cannot be zero or below", Toast.LENGTH_SHORT).show();
			}
			else
			{
				//Data is good. Begin process to add cage.
				dialog=ProgressDialog.show(TroughWeightScreen.this,"","Reading...",true);
				if(thread.getState()==Thread.State.NEW)
					thread.start();
					else
					{
						thread.interrupt();
						thread=new Thread(new Runnable(){
							public void run(){
								troughWeight();
							}});
						thread.start();
					}	
			}
			}
			break;

			//Back button is clicked
		case R.id.backButtonTroughWeight:
			next=new Intent(TroughWeightScreen.this,ReportsScreen.class);
			startActivity(next);
			break;
		}
		
	}

	//Internal method to compute weight
	public void troughWeight()
	{
		try
		{
			//Establish connection
			client=new DefaultHttpClient();
			post=new HttpPost("http://www.vitulustech.com/troughWeightScript.php");

			//Give elements to PHP Script
			nameValuePair=new ArrayList<NameValuePair>(2);
			nameValuePair.add(new BasicNameValuePair("CageNum",cageNumber.getText().toString().trim()));
			nameValuePair.add(new BasicNameValuePair("Date",date.getText().toString().trim()));

			post.setEntity(new UrlEncodedFormEntity(nameValuePair));
			//response=client.execute(post);

			//Listen for response
			ResponseHandler<String> handler=new BasicResponseHandler();
			String response=client.execute(post, handler);
			
			try
			{
				success=response.split("/");
			}
			catch(Exception e)
			{	
				success[0]="No";
				Toast.makeText(TroughWeightScreen.this, "Parse error", Toast.LENGTH_SHORT).show();
			}
			//If everything is successful...
			if(success[0].equalsIgnoreCase("Success"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(TroughWeightScreen.this, "Success", Toast.LENGTH_SHORT).show();	
						food.setText(success[1].toString());
						water.setText(success[2].toString());						
						dialog.dismiss();
						thread.interrupt();
					}
				});
			}
			//If cgae is not found
			else if((response.trim()).equalsIgnoreCase("Not found"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(TroughWeightScreen.this, "Cage not found",Toast.LENGTH_SHORT).show();
					}
				});
			}
			else if((response.trim()).equalsIgnoreCase("Wrong date"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(TroughWeightScreen.this, "Wrong date",Toast.LENGTH_SHORT).show();
					}
				});
			}
			//An error
			else
			{
				Toast.makeText(TroughWeightScreen.this, "Error", Toast.LENGTH_SHORT).show();
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
