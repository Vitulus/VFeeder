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
 *A class to retrieve password.
 */
public class ForgotPasswordScreen extends Activity implements OnClickListener{

	//Variables
	private Button sendPassword, back;
	private Intent next;
	private EditText email, username;
	private HttpPost post;
	private HttpResponse response;
	private HttpClient client;
	private List<NameValuePair> nameValuePair;
	private ProgressDialog dialog=null;
	private ArrayList<EditText> list;
	private EmptyStringReviewer reviewer;
	private Thread thread=new Thread(new Runnable(){
		public void run(){
			forgotPassword();
		}});

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password_screen);

		//Initialize variable
		sendPassword=(Button)this.findViewById(R.id.forgotPasswordButton);
		back=(Button)this.findViewById(R.id.backPasswordButton);

		email=(EditText)this.findViewById(R.id.enterEmailFieldFP);
		username=(EditText)this.findViewById(R.id.usernameFieldFP);

		//Add listener
		sendPassword.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forgot_password_screen, menu);
		return true;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		//Forgot password button
		case R.id.forgotPasswordButton:
			if(!isNetworkAvailable())
			{
				Toast.makeText(ForgotPasswordScreen.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
			}
			else{
				//Add elements to list for verification
				list=new ArrayList<EditText>();
				list.add(username);
				list.add(email);

				//Check if fields are empty
				if(new EmptyStringReviewer(list).reviseEmpty())
				{
					Toast.makeText(ForgotPasswordScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();	
				}
				else if(!(email.getText().toString()).contains("@"))
				{
					Toast.makeText(ForgotPasswordScreen.this, "Incorrect email format", Toast.LENGTH_SHORT).show();
				}
				else
				{
					//Data is good. Begin.
					dialog=ProgressDialog.show(ForgotPasswordScreen.this,"","Searching...",true);
					if(thread.getState()==Thread.State.NEW)
						thread.start();
					else
					{
						thread.interrupt();
						thread=new Thread(new Runnable(){
							public void run(){
								forgotPassword();
							}});
						thread.start();
					}
				}
			}
			break;
			//Back button pressed
		case R.id.backPasswordButton:
			next=new Intent(ForgotPasswordScreen.this,LoginScreen.class);
			startActivity(next);
			finish();

			break;
		}
		//startActivity(next);
	}

	//Method due to forgotten password.
	public void forgotPassword(){

		try{
			client=new DefaultHttpClient();
			post=new HttpPost("http://www.vitulustech.com/forgotPasswordScript.php");
			nameValuePair=new ArrayList<NameValuePair>(2);
			nameValuePair.add(new BasicNameValuePair("Username",username.getText().toString().trim()));
			nameValuePair.add(new BasicNameValuePair("Email",email.getText().toString().trim()));

			post.setEntity(new UrlEncodedFormEntity(nameValuePair));
			//response=client.execute(post);

			ResponseHandler<String> handler=new BasicResponseHandler();
			final String response=client.execute(post, handler);

			//User exists.
			if(response.equalsIgnoreCase("User exists"))
			{
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(ForgotPasswordScreen.this, "Success", Toast.LENGTH_SHORT).show();						
					}
				});
				next=new Intent(ForgotPasswordScreen.this,LoginScreen.class);
				startActivity(next);
			}
			else
			{
				showAlert();

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

	//User not found
	public void showAlert()
	{
		ForgotPasswordScreen.this.runOnUiThread(new Runnable()
		{
			public void run()
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordScreen.this);
				builder.setTitle("Error.");
				builder.setMessage("User not Found.")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
				AlertDialog alert=builder.create();
				alert.show();
			}
		});
	}

	//Method to detect Internet Connection
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}