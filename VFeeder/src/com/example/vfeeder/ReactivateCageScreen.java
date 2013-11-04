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
 * A class to reactivate a cage
 */
public class ReactivateCageScreen extends Activity implements OnClickListener{

	//Variables
	private Button reactivate, home;
	private Intent next;
	private EditText cageNumber;

	private HttpPost post;
	private HttpResponse response;
	private HttpClient client;
	private List<NameValuePair> nameValuePair;
	private ProgressDialog dialog=null;
	private Thread thread=new Thread(new Runnable(){
		public void run(){
			reactivateCage();
		}});

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reactivate_cage_screen);

		//Initialize
		reactivate=(Button)this.findViewById(R.id.reactivateButton);
		home=(Button)this.findViewById(R.id.homeReactivateCage);

		cageNumber=(EditText)this.findViewById(R.id.cageNumberFieldReactivateCage);

		//Add listeners
		reactivate.setOnClickListener(this);
		home.setOnClickListener(this);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reactivate_cage_screen, menu);
		return true;
	}

	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
		{
		//Home button pressed
		case R.id.homeReactivateCage:
			next=new Intent(ReactivateCageScreen.this,WelcomeScreen.class);//Go to Welcome Screen
			startActivity(next);
			break;

			//Reactivate was pressed
		case R.id.reactivateButton:
			//Check if field is empty
			if(cageNumber.getText().toString().length()==0)
			{
				Toast.makeText(ReactivateCageScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();
			}
			//Check if cage number is equal or below zero
			else if((Integer.parseInt(cageNumber.getText().toString()))<=0)
			{
				Toast.makeText(ReactivateCageScreen.this, "Cage number cannot be zero or below",
						Toast.LENGTH_SHORT).show();
			}
			else
			{
				//TODO 
				//Data is good. Begin process.
				dialog=ProgressDialog.show(ReactivateCageScreen.this,"","Processing...",true);
				if(thread.getState()==Thread.State.NEW)
					thread.start();
				else
				{
					thread.interrupt();
					thread=new Thread(new Runnable(){
						public void run(){
							reactivateCage();
						}});
					thread.start();
				}
			}

		}

	}

	//Internal method to reactivate cages.
	public void reactivateCage()
	{
		try
		{
			//Establish connection
			client=new DefaultHttpClient();
			post=new HttpPost("http://www.vitulustech.com/reactivateCageScript.php");

			//Give elements to PHP Script
			nameValuePair=new ArrayList<NameValuePair>(1);
			nameValuePair.add(new BasicNameValuePair("CageNum",cageNumber.getText().toString().trim()));

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
						Toast.makeText(ReactivateCageScreen.this, "Success", Toast.LENGTH_SHORT).show();	

					}
				});
			}
			//If cage is not found
			else if(response.equalsIgnoreCase("Not Found"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(ReactivateCageScreen.this, "Cage does not exist",Toast.LENGTH_SHORT).show();
					}
				});
			}
			//If cage is already active
			else if(response.equalsIgnoreCase("Active"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(ReactivateCageScreen.this, "Cage is already active",Toast.LENGTH_SHORT).show();
					}
				});
			}
			//An error
			else
			{
				Toast.makeText(ReactivateCageScreen.this, "Error", Toast.LENGTH_SHORT).show();
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
