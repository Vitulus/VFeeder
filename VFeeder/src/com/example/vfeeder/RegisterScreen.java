package com.example.vfeeder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterScreen extends Activity implements OnClickListener{

	private Button register;
	private EditText username, password, email;
	private Intent next;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_screen);
		
		//register=(Button)this.findViewById(R.id.registerButton);
		
		//username=(EditText)this.findViewById(R.id.usernameFieldRegister);
		//password=(EditText)this.findViewById(R.id.passwordFieldRegister);
		//email=(EditText)this.findViewById(R.id.emailFieldRegister);
		
		register.setOnClickListener(this);
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
//		switch(v.getId())
//		{
//		case R.id.registerButton:
//			next=new Intent(RegisterScreen.this,WelcomeScreen.class);
//			startActivity(next);
//			break;
//		}
	}
	
	

}
