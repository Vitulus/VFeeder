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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author einsteinboricua
 *A class to display dispense data
 */
public class DispenseDataScreen extends Activity implements OnClickListener{

	//Variables
	private Button compute, back;
	private Intent next;
	private EditText cageNumber, date;//month, day, year;
	private TextView food,water,time;
	private ArrayList<EditText> list;
	private String [] success;

	private HttpPost post;
	private HttpResponse response;
	private HttpClient client;
	private List<NameValuePair> nameValuePair;
	private ProgressDialog dialog=null;
	private Thread thread=new Thread(new Runnable(){
		public void run(){
			dispenseData();
		}});


	protected void onCreate(Bundle savedInstanceState) {
		//Android commands to initiate
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dispense_data_screen);

		//Initialize variables
		compute=(Button)this.findViewById(R.id.computeButtonDispense);
		back=(Button)this.findViewById(R.id.backButtonDispenseReport);


		//		month=(EditText)this.findViewById(R.id.monthActiveCages);
		//		day=(EditText)this.findViewById(R.id.dayActiveCages);
		//		year=(EditText)this.findViewById(R.id.yearActiveCages);

		cageNumber=(EditText)this.findViewById(R.id.cageNumberFieldDispenseReport);
		date=(EditText)this.findViewById(R.id.dateFieldDispense);

		food=(TextView)this.findViewById(R.id.foodAmountReading);
		water=(TextView)this.findViewById(R.id.waterAmountReading);
		time=(TextView)this.findViewById(R.id.dispenseTimeDispenseReport);

		food.setText("");
		water.setText("");
		time.setText("");

		//Add click listeners
		compute.setOnClickListener(this);
		back.setOnClickListener(this);

	}


	//Android method
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dispense_data_screen, menu);
		return true;
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		//Compute button clicked
		case R.id.computeButtonDispense:
			if(!isNetworkAvailable())
			{
				Toast.makeText(DispenseDataScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();
			}
			else
			{
				list=new ArrayList<EditText>();
				//			list.add(month);
				//			list.add(day);
				//			list.add(year);
				list.add(cageNumber);
				list.add(date);

				//Check if any field is empty
				if(new EmptyStringReviewer(list).reviseEmpty())
				{
					Toast.makeText(DispenseDataScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();
				}
				//Check if cage number is zero or below
				else if((Integer.parseInt(cageNumber.getText().toString()))<=0)
				{
					Toast.makeText(DispenseDataScreen.this, "Cage cannot be zero or below", Toast.LENGTH_SHORT).show();
				}
				else
				{
					//Data is good. Begin reading...
					dialog=ProgressDialog.show(DispenseDataScreen.this,"","Reading...",true);
					if(thread.getState()==Thread.State.NEW)
						thread.start();
					else
					{
						thread.interrupt();
						thread=new Thread(new Runnable(){
							public void run(){
								dispenseData();
							}});
						thread.start();
					}
				}
			}
			break;

			//Back button is pressed
		case R.id.backButtonDispenseReport:
			next=new Intent(DispenseDataScreen.this,ReportsScreen.class);
			startActivity(next);
			break;
		}
	}
	//Internal method to dispense data
	public void dispenseData(){
		try
		{
			//Establish connection
			client=new DefaultHttpClient();
			post=new HttpPost("http://www.vitulustech.com/dispenseDataScript.php");

			//Give elements to PHP Script
			nameValuePair=new ArrayList<NameValuePair>(2);
			nameValuePair.add(new BasicNameValuePair("CageNum",cageNumber.getText().toString().trim()));
			nameValuePair.add(new BasicNameValuePair("Date",date.getText().toString().trim()));

			post.setEntity(new UrlEncodedFormEntity(nameValuePair));
			//response=client.execute(post);

			//Listen for response
			ResponseHandler<String> handler=new BasicResponseHandler();
			final String response=client.execute(post, handler);

			try{

				success=response.split("/");
			}
			catch(Exception e)
			{	
				success[0]="No";
				Toast.makeText(DispenseDataScreen.this, "Parse error", Toast.LENGTH_SHORT).show();
			}
			//If everything is successful...
			if(success[0].equalsIgnoreCase("Success"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(DispenseDataScreen.this, "Success", Toast.LENGTH_SHORT).show();
						food.setText(success[1].toString());
						water.setText(success[2].toString());
						time.setText(success[3].toString());
					}
				});
			}
			//If the cage is not on record
			else if(response.equalsIgnoreCase("Not Found"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(DispenseDataScreen.this, "Cage not found",Toast.LENGTH_SHORT).show();
					}
				});
			}
			//If the date does not exist
			else if(response.equalsIgnoreCase("Wrong Date"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(DispenseDataScreen.this, "No date record",Toast.LENGTH_SHORT).show();
					}
				});
			}
			//An error
			else
			{
				Toast.makeText(DispenseDataScreen.this, "Error adding cage", Toast.LENGTH_SHORT).show();
			}

			dialog.dismiss();
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
