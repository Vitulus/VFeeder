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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterScreen extends Activity implements OnClickListener{

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

		register=(Button)this.findViewById(R.id.registerButtonRegister);
		home=(Button)this.findViewById(R.id.homeButtonRegister);

		username=(EditText)this.findViewById(R.id.usernameFieldRegister);
		password=(EditText)this.findViewById(R.id.passwordFieldRegister);
		email=(EditText)this.findViewById(R.id.emailFieldRegister);

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
		case R.id.registerButtonRegister:
			//TODO
			list=new ArrayList<EditText>();
			list.add(username);
			list.add(password);
			list.add(email);			
			if(new EmptyStringReviewer(list).reviseEmpty())
			{
				Toast.makeText(RegisterScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();
			}
			else
			{
				dialog=ProgressDialog.show(RegisterScreen.this,"","Registering...",true);
				thread.start();
			}
			break;
		case R.id.homeButtonRegister:
			next=new Intent(RegisterScreen.this,LoginScreen.class);
			startActivity(next);
			finish();
			break;
		}
	} 

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
				thread.stop();
			}
		}


		catch(Exception e)
		{
			e.printStackTrace();
			dialog.dismiss();
		}
	}

}
