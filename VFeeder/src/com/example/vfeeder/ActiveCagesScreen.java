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
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActiveCagesScreen extends Activity implements OnClickListener{
	
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
		
		compute=(Button)this.findViewById(R.id.computeActiveCages);
		back=(Button)this.findViewById(R.id.backButtonActiveCages);
		
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
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.backButtonActiveCages:
			next=new Intent(ActiveCagesScreen.this,ReportsScreen.class);
			startActivity(next);
			break;
		case R.id.computeActiveCages:
			//TODO
			
			if(date.getText().toString().length()==0)
			{
				Toast.makeText(ActiveCagesScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();	
			}
			else if(new DateReviewer(date).reviseDate())
			{
				Toast.makeText(ActiveCagesScreen.this, "Incorrect date format", Toast.LENGTH_SHORT).show();	
			}
			else
			{
				dialog=ProgressDialog.show(ActiveCagesScreen.this,"","Verifying...",true);
				thread.start();				
			}
			
			break;
		}
		
	}
	
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
			dialog.dismiss();
		}
		finally
		{
			thread.interrupt();
		}
	}

}
