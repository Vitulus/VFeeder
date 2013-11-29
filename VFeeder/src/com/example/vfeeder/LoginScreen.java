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
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author einsteinboricua
 * A class to handle the Login
 */
public class LoginScreen extends Activity implements OnClickListener{

	//Variables
	private Button login;
	private Intent next;
	private EditText username, password;
	private TextView register, forgotPassword;
	private HttpPost post;
	private HttpResponse response;
	private HttpClient client;
	private List<NameValuePair> nameValuePair;
	private ArrayList<EditText> list;
	private ProgressDialog dialog=null;
	private Thread thread=new Thread(new Runnable(){
		public void run(){
			login();
		}});


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);

		//Initialize variables
		login=(Button)this.findViewById(R.id.loginButton);


		username=(EditText)this.findViewById(R.id.usernameField);
		password=(EditText)this.findViewById(R.id.passwordField);

		register=(TextView)this.findViewById(R.id.registerTxt);
		forgotPassword=(TextView)this.findViewById(R.id.forgotPasswordTxt);

		//Add OnClickListeners
		login.setOnClickListener(this);

		register.setOnClickListener(this);
		forgotPassword.setOnClickListener(this);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_screen, menu);
		return true;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		//Login button pressed
		case R.id.loginButton:
			//Check to see if there's internet connection.
			if(!isNetworkAvailable())
			{
				//If not available, display message.
				Toast.makeText(LoginScreen.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
			}
			else{
				//Add elements to list
				list=new ArrayList<EditText>();
				list.add(username);
				list.add(password);
				//If any field is empty
				if(new EmptyStringReviewer(list).reviseEmpty())
				{
					Toast.makeText(LoginScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();	
				}
				else{
					//Data is good. Begin login.
					dialog=ProgressDialog.show(LoginScreen.this,"","Validating User...",true);
					if(thread.getState()==Thread.State.NEW)
						thread.start();
					else
					{
						thread.interrupt();
						thread=new Thread(new Runnable(){
							public void run(){
								login();
							}});
						thread.start();
					}
				}
			}
			break;
			//Register was clicked
		case R.id.registerTxt:
			next=new Intent(LoginScreen.this,RegisterScreen.class);
			startActivity(next);
			break;
			//ForgotPassword was clicked
		case R.id.forgotPasswordTxt:
			next=new Intent(LoginScreen.this,ForgotPasswordScreen.class);
			startActivity(next);
			break;		
		}
	}

	//Internal method for login
	public void login(){

		try{
			client=new DefaultHttpClient();
			post=new HttpPost("http://www.vitulustech.com/loginScript.php");
			nameValuePair=new ArrayList<NameValuePair>(2);
			nameValuePair.add(new BasicNameValuePair("Username",username.getText().toString().trim()));
			nameValuePair.add(new BasicNameValuePair("Password",password.getText().toString().trim()));

			post.setEntity(new UrlEncodedFormEntity(nameValuePair));
			//response=client.execute(post);

			ResponseHandler<String> handler=new BasicResponseHandler();
			final String response=client.execute(post, handler);

			//If user is found
			if(response.equalsIgnoreCase("User Found")){
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(LoginScreen.this, "Login Success", Toast.LENGTH_SHORT).show();						
					}
				});
				next=new Intent(LoginScreen.this,WelcomeScreen.class);
				startActivity(next);
				finish();
			}
			else
			{
				Toast.makeText(LoginScreen.this, "User not found", Toast.LENGTH_SHORT).show();

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

	//Method to detect Internet Connection
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}


}

