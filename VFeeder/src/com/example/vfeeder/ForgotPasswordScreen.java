package com.example.vfeeder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ForgotPasswordScreen extends Activity implements OnClickListener{

	private Button sendPassword, back;
	private Intent next;
	private EditText email;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password_screen);

		sendPassword=(Button)this.findViewById(R.id.forgotPasswordButton);
		back=(Button)this.findViewById(R.id.backPasswordButton);

		email=(EditText)this.findViewById(R.id.enterEmailFieldFP);

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
			break;
		case R.id.backPasswordButton:
			next=new Intent(ForgotPasswordScreen.this,LoginScreen.class);
			break;
		}
		startActivity(next);

	}
}