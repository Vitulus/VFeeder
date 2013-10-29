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

public class ForgotPasswordScreen extends Activity implements OnClickListener{

	private Button sendPassword, back;
	private Intent next;
	private EditText email, username;
	private HttpPost post;
	private HttpResponse response;
	private HttpClient client;
	private List<NameValuePair> nameValuePair;
	private ProgressDialog dialog=null;
	private Thread thread=new Thread(new Runnable(){
		public void run(){
			forgotPassword();
		}});

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password_screen);

		sendPassword=(Button)this.findViewById(R.id.forgotPasswordButton);
		back=(Button)this.findViewById(R.id.backPasswordButton);

		email=(EditText)this.findViewById(R.id.enterEmailFieldFP);
		username=(EditText)this.findViewById(R.id.usernameFieldFP);

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
		case R.id.forgotPasswordButton:
			//TODO
			dialog=ProgressDialog.show(ForgotPasswordScreen.this,"","Searching...",true);
			thread.start();
			break;
		case R.id.backPasswordButton:
			next=new Intent(ForgotPasswordScreen.this,LoginScreen.class);
			startActivity(next);
			finish();
			
			break;
		}
		//startActivity(next);
	}

	public void forgotPassword(){
		if(username.getText().equals(null)||email.getText().equals(null))
		{
			Toast.makeText(ForgotPasswordScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();	
		}
		else{
			try{
				client=new DefaultHttpClient();
				post=new HttpPost("http://www.vitulustech.com/forgotPasswordScript.php");
				nameValuePair=new ArrayList<NameValuePair>(2);
				nameValuePair.add(new BasicNameValuePair("Username",username.getText().toString().trim()));
				nameValuePair.add(new BasicNameValuePair("Email",email.getText().toString().trim()));

				post.setEntity(new UrlEncodedFormEntity(nameValuePair));
				response=client.execute(post);

				ResponseHandler<String> handler=new BasicResponseHandler();
				final String response=client.execute(post, handler);

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
					thread.stop();
				}
				//FIX PASSWORD SENT BLANK!!!

			}
			catch(Exception e)
			{
				dialog.dismiss();
			}
		}
	}
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
}