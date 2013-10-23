package com.example.vfeeder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author einsteinboricua
 *
 */
public class LoginScreen extends Activity implements OnClickListener{

	private Button login;
	private Intent next;
	private EditText username, password;
	private TextView register, forgotPassword;
	
	
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
			//TODO
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
}
