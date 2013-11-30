package com.example.vfeeder;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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
import android.widget.ToggleButton;

/**
 * @author einsteinboricua
 *The class that will send the instruction to dispense.
 */
public class DispenseScreen extends Activity implements OnClickListener{

	private Button home, dispense;
	private Intent next;
	private ToggleButton allCages;
	private EditText cageNumber;

	private HttpPost post;
	private HttpClient client;
	private List<NameValuePair> nameValuePair;
	private ProgressDialog dialog=null;

	private Thread dispense1=new Thread(new Runnable(){
		public void run(){
			dispenseOneCage();
		}});

	private Thread dispense2=new Thread(new Runnable(){
		public void run(){
			dispenseAll();
		}});

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dispense_screen);

		//Initialize variables
		home=(Button)this.findViewById(R.id.homeButtonD);
		dispense=(Button)this.findViewById(R.id.dispenseButton);

		allCages=(ToggleButton)this.findViewById(R.id.allCages);

		cageNumber=(EditText)this.findViewById(R.id.selectCageField);



		//Assign a ClickListener
		home.setOnClickListener(this);
		dispense.setOnClickListener(this);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dispense_screen, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch(v.getId()){

		//Dispense 
		case R.id.dispenseButton:

			if(!allCages.isChecked())
			{

				if(cageNumber.getText().toString().length()==0)
				{
					Toast.makeText(DispenseScreen.this, "Must fill all fields", Toast.LENGTH_SHORT).show();
				}
				else if(Integer.parseInt(cageNumber.getText().toString())<=0)
				{
					Toast.makeText(DispenseScreen.this, "Cage number cannot be zero or below", Toast.LENGTH_SHORT).show();
				}
				else
				{
					if(dispense1.getState()==Thread.State.NEW)
						dispense1.start();
					else
					{
						dispense1.interrupt();
						dispense1=new Thread(new Runnable(){
							public void run(){
								dispenseOneCage();
							}});
						dispense1.start();
					}	

				}
			}

			else
			{				
				if(dispense2.getState()==Thread.State.NEW)
					dispense2.start();
				else
				{
					dispense2.interrupt();
					dispense2=new Thread(new Runnable(){
						public void run(){
							dispenseAll();
						}});
					dispense2.start();
				}	
			}

			//TODO
			//Toast.makeText(DispenseScreen.this, "Dispensed", Toast.LENGTH_SHORT).show();

			break;
			//Go back home	
		case R.id.homeButtonD:
			next=new Intent(DispenseScreen.this,WelcomeScreen.class);
			startActivity(next);
			break;
		}
	}

	//Instructions to dispense a single cage
	public void dispenseOneCage()
	{
		try
		{
			//Establish connection
			client=new DefaultHttpClient();
			post=new HttpPost("http://www.vitulustech.com/dispenseOneCageScript.php");

			//Give elements to PHP Script
			nameValuePair=new ArrayList<NameValuePair>(1);
			nameValuePair.add(new BasicNameValuePair("CageNum",cageNumber.getText().toString().trim()));

			post.setEntity(new UrlEncodedFormEntity(nameValuePair));

			ResponseHandler<String> handler=new BasicResponseHandler();
			String response=client.execute(post, handler);

			//If successful
			if(response.equalsIgnoreCase("Success"))
			{
				runOnUiThread(new Runnable(){
					public void run(){			
						Toast.makeText(DispenseScreen.this, "Dispensed cage #"+cageNumber.getText().toString()
								, Toast.LENGTH_SHORT).show();
					}
				});

			}
			else if((response.trim()).equalsIgnoreCase("Not Found"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(DispenseScreen.this, "Cage not found",Toast.LENGTH_SHORT).show();
					}
				});
			}
			else if((response.trim()).equalsIgnoreCase("Inactive"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(DispenseScreen.this, "Cage is not active",Toast.LENGTH_SHORT).show();
					}
				});
			}
			else
			{
				DispenseScreen.this.runOnUiThread(new Runnable()
				{
					public void run(){
						Toast.makeText(DispenseScreen.this, "Error", Toast.LENGTH_SHORT).show();
					}
				});

			}
		}
		catch(Exception e)
		{

		}
		finally
		{
			dispense1.interrupt();
		}
	}


	//Instructions to dispense all cages.
	public void dispenseAll()
	{

		try
		{
			//Establish connection
			client=new DefaultHttpClient();
			post=new HttpPost("http://www.vitulustech.com/dispenseAllCagesScript.php");

			//				//Give elements to PHP Script
			//				nameValuePair=new ArrayList<NameValuePair>(1);
			//				nameValuePair.add(new BasicNameValuePair("CageNum",cageNumber.getText().toString().trim()));
			//
			//				post.setEntity(new UrlEncodedFormEntity(nameValuePair));

			ResponseHandler<String> handler=new BasicResponseHandler();
			String response=client.execute(post, handler);

			//If successful
			if(response.equalsIgnoreCase("Success"))
			{
				runOnUiThread(new Runnable(){
					public void run(){			
						Toast.makeText(DispenseScreen.this, "Dispensed all cages", Toast.LENGTH_SHORT).show();
					}
				});

			}
			else if((response.trim()).equalsIgnoreCase("Not Found"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(DispenseScreen.this, "Cages not found",Toast.LENGTH_SHORT).show();
					}
				});
			}

			else
			{
				DispenseScreen.this.runOnUiThread(new Runnable()
				{
					public void run(){
						Toast.makeText(DispenseScreen.this, "Error", Toast.LENGTH_SHORT).show();
					}
				});

			}
		}
		catch(Exception e)
		{

		}
		finally
		{
			dispense2.interrupt();
		}
	}

}
