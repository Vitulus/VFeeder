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
import android.content.DialogInterface;
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
 *Deactivate a cage from the system
 */
public class DeactivateCagesScreen extends Activity implements OnClickListener{

	//Variables
	private Button delete, home;
	private Intent next;
	private EditText cageNumber;
	
	private HttpPost post;
	private HttpResponse response;
	private HttpClient client;
	private List<NameValuePair> nameValuePair;
	private ProgressDialog dialog=null;
	private Thread thread=new Thread(new Runnable(){
		public void run(){
			deactivateCage();
		}});

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deactivate_cage_screen);

		//Initialize variables
		delete=(Button)this.findViewById(R.id.eraseBDC);
		home=(Button)this.findViewById(R.id.homeBDC);

		cageNumber=(EditText)this.findViewById(R.id.cageNumFieldDC);

		//Assign listeners
		delete.setOnClickListener(this);
		home.setOnClickListener(this);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.deactivate_cage_screen, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		//Deactivate button clicked
		case R.id.eraseBDC:
			//TODO
			//Check if empty
			if(cageNumber.getText().toString().length()==0)
			{
				Toast.makeText(DeactivateCagesScreen.this, "Fill all fields",
						Toast.LENGTH_SHORT).show();
			}
			//Check if cage number is zero 
			else if((Integer.parseInt(cageNumber.getText().toString()))<=0)
			{
				Toast.makeText(DeactivateCagesScreen.this, "Cage number cannot be zero or below",
						Toast.LENGTH_SHORT).show();
			}
			else
			{
				//Data is good. Begin deactivating.
				dialog=ProgressDialog.show(DeactivateCagesScreen.this,"","Deactivating...",true);
				
				if(thread.getState()==Thread.State.NEW)
					thread.start();
					else
					{
						thread.interrupt();
						thread=new Thread(new Runnable(){
							public void run(){
								deactivateCage();
							}});
						thread.start();
					}	
			}
			break;

			//Go back to home screen
		case R.id.homeBDC:
			next=new Intent(DeactivateCagesScreen.this,WelcomeScreen.class);
			startActivity(next);
			break;
		}
	}
	
	//Internal method to deactivate cage.
	public void deactivateCage()
	{
		try
		{
			//Establish connection
			client=new DefaultHttpClient();
			post=new HttpPost("http://www.vitulustech.com/deactivateCageScript.php");
			
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
						Toast.makeText(DeactivateCagesScreen.this, "Success", Toast.LENGTH_SHORT).show();	
						
					}
				});
			}
			//If cage is not found
			else if(response.equalsIgnoreCase("Not Found"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(DeactivateCagesScreen.this, "Cage does not exist",Toast.LENGTH_SHORT).show();
					}
				});
			}
			//If cage is already inactive
			else if(response.equalsIgnoreCase("Inactive"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(DeactivateCagesScreen.this, "Cage is already inactive",Toast.LENGTH_SHORT).show();
					}
				});
			}
			//An error
			else
			{
				Toast.makeText(DeactivateCagesScreen.this, "Error", Toast.LENGTH_SHORT).show();
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
