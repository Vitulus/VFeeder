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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author einsteinboricua
 *A class to delete data from cages
 */
public class EraseDataScreen extends Activity implements OnClickListener{

	//Variables
	private Button erase, home;
	private Intent next;
	private EditText cageNumber;
	
	private HttpPost post;
	private HttpResponse response;
	private HttpClient client;
	private List<NameValuePair> nameValuePair;
	private ProgressDialog dialog=null;
	private Thread thread=new Thread(new Runnable(){
		public void run(){
			erase();
		}});

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_erase_data_screen);

		//Initialize variables
		erase=(Button)this.findViewById(R.id.eraseButton);
		home=(Button)this.findViewById(R.id.homeButtonED);

		cageNumber=(EditText)this.findViewById(R.id.cageNumberFieldED);

		//Add listeners
		erase.setOnClickListener(this);
		home.setOnClickListener(this);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.erase_data_screen, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		//Erase button was clicked
		case R.id.eraseButton:
			if(!isNetworkAvailable())
			{
				Toast.makeText(EraseDataScreen.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
			}
			else
			{
			//If cage number is 
			if((Integer.parseInt(cageNumber.getText().toString()))<=0)
			{
				Toast.makeText(EraseDataScreen.this, "Cage number cannot be zero or below",
						Toast.LENGTH_SHORT).show();
			}
			//If field is empty
			else if(cageNumber.getText().toString().length()==0)
			{
				Toast.makeText(EraseDataScreen.this, "Fill all fields",
						Toast.LENGTH_SHORT).show();
			}
			
			else
			{
				//Data is good. Process data
				dialog=ProgressDialog.show(EraseDataScreen.this,"","Processing...",true);
				if(thread.getState()==Thread.State.NEW)
					thread.start();
				else
				{
					thread.interrupt();
					thread=new Thread(new Runnable(){
						public void run(){
							erase();
						}});
					thread.start();
				}
			}
			}
			break;
			
			//Go back home
		case R.id.homeButtonED:
			next=new Intent(EraseDataScreen.this,WelcomeScreen.class);
			startActivity(next);
			break;
		}

	}
	
	//Internal method to erase data
	public void erase()
	{
		try
		{
			//Establish connection
			client=new DefaultHttpClient();
			post=new HttpPost("http://www.vitulustech.com/eraseDataScript.php");
			
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
						Toast.makeText(EraseDataScreen.this, "Success", Toast.LENGTH_SHORT).show();							
					}
				});
			}
			//If there is a repeated cage
			else if(response.equalsIgnoreCase("Not found"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(EraseDataScreen.this, "No active cages",Toast.LENGTH_SHORT).show();
					}
				});
			}
			//An error
			else
			{
				Toast.makeText(EraseDataScreen.this, "Error", Toast.LENGTH_SHORT).show();
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
