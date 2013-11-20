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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
 *Temperature reading screen
 */
public class TemperatureScreen extends Activity implements OnClickListener{

	//Variables
	private Button compute,back;
	private Intent next;
	private EditText cageNumber,date;
	private ArrayList<EditText> list;
	private TextView reading;
	private String[] success;

	private HttpPost post;
	//private HttpResponse response;
	private HttpClient client;
	private List<NameValuePair> nameValuePair;
	private ProgressDialog dialog=null;
	private Thread thread=new Thread(new Runnable(){
		public void run(){
			readTemperature();
		}});

	protected void onCreate(Bundle savedInstanceState) {
		//Android commands to initiate
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temperature_screen);

		//Initialize variables
		compute=(Button)this.findViewById(R.id.computeTemperature);
		back=(Button)this.findViewById(R.id.backButtonTemperature);

		date=(EditText)this.findViewById(R.id.dateTemperature);	
		cageNumber=(EditText)this.findViewById(R.id.selectCageFieldTemperature);

		reading=(TextView)this.findViewById(R.id.readingTxtTemperature);

		//Add listeners
		compute.setOnClickListener(this);
		back.setOnClickListener(this);

		reading.setText("");
	}


	//Android method
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.temperature_screen, menu);
		return true;
	}


	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		//back button clicked
		case R.id.backButtonTemperature:
			next=new Intent(TemperatureScreen.this,ReportsScreen.class);
			startActivity(next);
			break;

			//Compute button clicked
		case R.id.computeTemperature:
			//TODO
			list=new ArrayList<EditText>();
			list.add(cageNumber);
			list.add(date);

			//Check if field is empty
			if(new EmptyStringReviewer(list).reviseEmpty())
			{
				Toast.makeText(TemperatureScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();
			}
			//Check if date is correct
			else if(new DateReviewer(date).reviseDate())
			{
				Toast.makeText(TemperatureScreen.this, "Incorrect date format", Toast.LENGTH_SHORT).show();
			}
			//Check if cage number is zero or below
			else if((Integer.parseInt(cageNumber.getText().toString()))<=0)
			{
				Toast.makeText(TemperatureScreen.this, "Cage number cannot be zero or below",
						Toast.LENGTH_SHORT).show();
			}
			else
			{
				//Data is good. Begin reading.
				dialog=ProgressDialog.show(TemperatureScreen.this,"","Reading...",true);
				if(thread.getState()==Thread.State.NEW)
					thread.start();
				else
				{
					thread.interrupt();
					thread=new Thread(new Runnable(){
						public void run(){
							readTemperature();
						}});
					thread.start();
				}
			}
			break;
		}
	}

	//Internal method to read temperature.
	public void readTemperature()
	{
		try{
			//Establish connection
			client=new DefaultHttpClient();
			post=new HttpPost("http://www.vitulustech.com/temperatureReadScript.php");

			//Give elements to PHP Script
			nameValuePair=new ArrayList<NameValuePair>(2);
			nameValuePair.add(new BasicNameValuePair("CageNum",cageNumber.getText().toString().trim()));
			nameValuePair.add(new BasicNameValuePair("Date",date.getText().toString().trim()));

			post.setEntity(new UrlEncodedFormEntity(nameValuePair));
			//response=client.execute(post);

			//Listen for response
			ResponseHandler<String> handler=new BasicResponseHandler();
			final String response=client.execute(post, handler);

			try
			{

				success=response.split("/");
			}
			catch(Exception e)
			{	
				success[0]="No";
				Toast.makeText(TemperatureScreen.this, "Parse error", Toast.LENGTH_SHORT).show();
			}
			//If everything is successful...
			if(success[0].equalsIgnoreCase("Success"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(TemperatureScreen.this, "Success", Toast.LENGTH_SHORT).show();
						reading.setText(success[1].toString()+" Celsius");
					}
				});
			}
			//If the cage is not on record
			else if((response.trim()).equalsIgnoreCase("Not Found"))
			{
				runOnUiThread(new Runnable()
				{
					public void run()
					{
						Toast.makeText(TemperatureScreen.this, "Cage not found",Toast.LENGTH_SHORT).show();

					}
				});
			}

			//If the date does not exist
			else if((response.trim()).equalsIgnoreCase("Wrong Date"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(TemperatureScreen.this, "No date record",Toast.LENGTH_SHORT).show();
					}
				});
			}

			//An error
			else
			{
				Toast.makeText(TemperatureScreen.this, "Error reading cage", Toast.LENGTH_SHORT).show();
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
}
