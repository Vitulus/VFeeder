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
 * Register user class
 */
public class RegisterScreen extends Activity implements OnClickListener{

	//Variables
	private Button register, home;
	private EditText username, password, email;
	private Intent next;
	private HttpPost post;
	private HttpResponse response;
	private HttpClient client;
	private List<NameValuePair> nameValuePair;
	private ProgressDialog dialog=null;
	private ArrayList<EditText> list;
	private Thread thread=new Thread(new Runnable(){
		public void run(){
			register();
		}});

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_screen);

		//Initialize variables
		register=(Button)this.findViewById(R.id.registerButtonRegister);
		home=(Button)this.findViewById(R.id.homeButtonRegister);

		username=(EditText)this.findViewById(R.id.usernameFieldRegister);
		password=(EditText)this.findViewById(R.id.passwordFieldRegister);
		email=(EditText)this.findViewById(R.id.emailFieldRegister);

		//Add listener
		register.setOnClickListener(this);
		home.setOnClickListener(this);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_screen, menu);
		return true;
	}

	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		//Register button clicked
		case R.id.registerButtonRegister:
			//Check to see if there's internet connection.
			if(!isNetworkAvailable())
			{
				//If not available, display message.
				Toast.makeText(RegisterScreen.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
			}
			else{
			//Add elements to list for verification
			list=new ArrayList<EditText>();
			list.add(username);
			list.add(password);
			list.add(email);
			//Check is any field is empty
			if(new EmptyStringReviewer(list).reviseEmpty())
			{
				Toast.makeText(RegisterScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();
			}
			else if(!(email.getText().toString()).contains("@"))
			{
				Toast.makeText(RegisterScreen.this, "Incorrect email format", Toast.LENGTH_SHORT).show();
			}
			else
			{
				//Data is good. Beging process.
				dialog=ProgressDialog.show(RegisterScreen.this,"","Registering...",true);
				if(thread.getState()==Thread.State.NEW)
					thread.start();
					else
					{
						thread.interrupt();
						thread=new Thread(new Runnable(){
							public void run(){
								register();
							}});
						thread.start();
					}
			}
			}
			break;
			//Home button was clicked.
		case R.id.homeButtonRegister:
			next=new Intent(RegisterScreen.this,LoginScreen.class);
			startActivity(next);
			finish();
			break;
		}
	} 

	//Internal method to register
	public void register(){
		try{
			client=new DefaultHttpClient();
			post=new HttpPost("http://www.vitulustech.com/dbRegisterScript.php");
			nameValuePair=new ArrayList<NameValuePair>(3);
			nameValuePair.add(new BasicNameValuePair("Username",username.getText().toString().trim()));
			nameValuePair.add(new BasicNameValuePair("Password",password.getText().toString().trim()));
			nameValuePair.add(new BasicNameValuePair("Email",email.getText().toString().trim()));

			post.setEntity(new UrlEncodedFormEntity(nameValuePair));
			//response=client.execute(post);

			ResponseHandler<String> handler=new BasicResponseHandler();
			final String response=client.execute(post, handler);

			//If successful
			if(response.equalsIgnoreCase("Success")){
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(RegisterScreen.this, "Success", Toast.LENGTH_SHORT).show();						
					}
				});
				next=new Intent(RegisterScreen.this,LoginScreen.class);
				startActivity(next);
				finish();
			}
			else
			{
				RegisterScreen.this.runOnUiThread(new Runnable()
				{
					public void run(){
						Toast.makeText(RegisterScreen.this, "Register Error", Toast.LENGTH_SHORT).show();
					}
				});
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
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
