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

import com.example.helperMethods.DateDiffCalculator;
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

/**
 * @author einsteinboricua
 *Calculate the length of stay of a calf.
 */
public class LengthOfStayScreen extends Activity implements OnClickListener{
	
	//Variables
	private Button compute,back;
	private EditText cageNumber;
	private Intent next;
	private ArrayList<EditText> list;
	private TextView length;
	private String[] success;
	private HttpPost post;
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
		setContentView(R.layout.activity_length_of_stay_screen);
		
		//Initialize variable
		compute=(Button)this.findViewById(R.id.computeLengthOfStay);
		back=(Button)this.findViewById(R.id.backButtonLengthOfStay);
		
		/*
		day=(EditText)this.findViewById(R.id.dayActiveCages);
		month=(EditText)this.findViewById(R.id.monthActiveCages);
		year=(EditText)this.findViewById(R.id.yearActiveCages);
		*/
		cageNumber=(EditText)this.findViewById(R.id.selectCageLengthOfStay);
		length=(TextView)this.findViewById(R.id.lengthOfStayNumber);
		
		//Add listeners
		compute.setOnClickListener(this);
		back.setOnClickListener(this);
		
	}
	
	//Android method
			@Override
			public boolean onCreateOptionsMenu(Menu menu) {
				// Inflate the menu; this adds items to the action bar if it is present.
				getMenuInflater().inflate(R.menu.length_of_stay_screen, menu);
				return true;
			}
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		//Back button pressed
		case R.id.backButtonLengthOfStay:
			next=new Intent(LengthOfStayScreen.this,ReportsScreen.class);
			startActivity(next);
			break;
		//Compute button pressed	
		case R.id.computeLengthOfStay:
			//TODO
			//Check if field is empty
			if(cageNumber.getText().toString().length()==0)
			{
				Toast.makeText(LengthOfStayScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();	
			}
			//Check if cage number is not zero or below
			else if(Integer.parseInt(cageNumber.getText().toString())==0)
			{
				Toast.makeText(LengthOfStayScreen.this, "Cage number cannot be zero or below",
						Toast.LENGTH_SHORT).show();	
			}
			else
			{
				//Data is good. Begin process.
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
	
	//Internal method to calculate length of stay.
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
			String response=client.execute(post, handler);
			
			try
			{
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
						
						length.setText(new DateDiffCalculator(success[1],success[2]).calculate()+" days");
						Toast.makeText(LengthOfStayScreen.this, "Success", Toast.LENGTH_SHORT).show();	
						dialog.dismiss();
						thread.interrupt();
					}
				});
			}
			//If cage not found
			else if((response.trim()).equalsIgnoreCase("Not Found"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(LengthOfStayScreen.this, "Cage not found",Toast.LENGTH_SHORT).show();
					}
				});
			}
			//Cage is inactive
			else if(response.trim().equalsIgnoreCase("Inactive"))
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
