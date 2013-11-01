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
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author einsteinboricua
 *
 */
public class LoginScreen extends Activity implements OnClickListener{

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
		case R.id.loginButton:
			list=new ArrayList<EditText>();
			list.add(username);
			list.add(password);
			
			if(new EmptyStringReviewer(list).reviseEmpty())
			{
				Toast.makeText(LoginScreen.this, "Fill all fields", Toast.LENGTH_SHORT).show();	
			}
			else{
				dialog=ProgressDialog.show(LoginScreen.this,"","Validating User...",true);
				thread.start();
			}
			break;
		case R.id.registerTxt:
			next=new Intent(LoginScreen.this,RegisterScreen.class);
			startActivity(next);
			break;
		case R.id.forgotPasswordTxt:
			next=new Intent(LoginScreen.this,ForgotPasswordScreen.class);
			startActivity(next);
			break;		
		}
	}

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
				showAlert();
				thread.stop();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			dialog.dismiss();
		}
	}


	public void showAlert()
	{
		LoginScreen.this.runOnUiThread(new Runnable()
		{
			public void run()
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(LoginScreen.this);
				builder.setTitle("Login Error.");
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

