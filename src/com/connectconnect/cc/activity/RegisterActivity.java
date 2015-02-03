package com.connectconnect.cc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.connectconnect.cc.R;


/**
 * 注册页
 * 
 */
public class RegisterActivity extends BaseActivity {
	private EditText userNameEditText;
	private EditText passwordEditText;
	private EditText confirmPwdEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initView();
		
	}

	/**
	 * 注册
	 * 
	 * @param view
	 */
	public void register(View view) {
		final String username = userNameEditText.getText().toString().trim();
		final String pwd = passwordEditText.getText().toString().trim();
		String confirm_pwd = confirmPwdEditText.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
			userNameEditText.requestFocus();
			return;
		} else if (TextUtils.isEmpty(pwd)) {
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			passwordEditText.requestFocus();
			return;
		} else if (TextUtils.isEmpty(confirm_pwd)) {
			Toast.makeText(this, "确认密码不能为空！", Toast.LENGTH_SHORT).show();
			confirmPwdEditText.requestFocus();
			return;
		} else if (!pwd.equals(confirm_pwd)) {
			Toast.makeText(this, "两次输入的密码不一致，请重新输入！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
			
				startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
				finish();
			};
	}

	public void back(View view) {
		finish();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		userNameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		confirmPwdEditText = (EditText) findViewById(R.id.confirm_password);
	}

}
