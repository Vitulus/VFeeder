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

public class LengthOfStayScreen extends Activity implements OnClickListener{
	
	private Button compute,back;
	private EditText cageNumber;
	private Intent next;
	private ArrayList<EditText> list;
	private TextView length;
	private String[] success;
	private HttpPost post;
	private HttpResponse response;
	private HttpClient client;
	private List<NameValuePair> nameValuePair;
	private ProgressDialog dialog=null;
	private Thread thread=new Thread(new Runnable(){
		public void run(){
			lengthOfStay();
		}});
	
	protected void onCreate(Bundle savedInstanceState) {
		//Android commands to initiate
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_active_cages_screen);
		
		compute=(Button)this.findViewById(R.id.computeLengthOfStay);
		back=(Button)this.findViewById(R.id.backButtonLengthOfStay);
		
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
		case R.id.backButtonLengthOfStay:
			next=new Intent(LengthOfStayScreen.this,ReportsScreen.class);
			startActivity(next);
			break;
			
		case R.id.computeLengthOfStay:
			//TODO
			
			if(cageNumber.getText().toString().length()==0)
			{
				Toast.makeText(LengthOfStayScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();	
			}
			else if(Integer.parseInt(cageNumber.getText().toString())==0)
			{
				Toast.makeText(LengthOfStayScreen.this, "Cage number cannot be zero or below",
						Toast.LENGTH_SHORT).show();	
			}
			else
			{
				dialog=ProgressDialog.show(LengthOfStayScreen.this,"","Verifying...",true);
				if(thread.getState()==Thread.State.NEW)
					thread.start();
					else
					{
						thread.interrupt();
						thread=new Thread(new Runnable(){
							public void run(){
								lengthOfStay();
							}});
						thread.start();
					}		
			}
			
			break;
		}
		
	}
	
	public void lengthOfStay()
	{
		try
		{
			//Establish connection
			client=new DefaultHttpClient();
			post=new HttpPost("http://www.vitulustech.com/lengthOfStayScript.php");
			
			//Give elements to PHP Script
			nameValuePair=new ArrayList<NameValuePair>(1);
			nameValuePair.add(new BasicNameValuePair("CageNum",cageNumber.getText().toString().trim()));
			
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
				Toast.makeText(LengthOfStayScreen.this, "Parse error", Toast.LENGTH_SHORT).show();
			}
			//If everything is successful...
			if(success[0].equalsIgnoreCase("Success"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						length.setText(success[1].toString());
						Toast.makeText(LengthOfStayScreen.this, "Success", Toast.LENGTH_SHORT).show();	
						dialog.dismiss();
						thread.interrupt();
					}
				});
			}
			//If there is a repeated cage
			else if(response.equalsIgnoreCase("Not found"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(LengthOfStayScreen.this, "Cage not found",Toast.LENGTH_SHORT).show();
					}
				});
			}
			else if(response.equalsIgnoreCase("Inactive"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(LengthOfStayScreen.this, "Cage is inactive",Toast.LENGTH_SHORT).show();
					}
				});	
			}
			//An error
			else
			{
				Toast.makeText(LengthOfStayScreen.this, "Error", Toast.LENGTH_SHORT).show();
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
