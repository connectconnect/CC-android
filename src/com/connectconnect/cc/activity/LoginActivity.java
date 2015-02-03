package com.connectconnect.cc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.connectconnect.cc.R;


/**
 * 登陆页面
 * 
 */
public class LoginActivity extends BaseActivity {
	public static final int REQUEST_CODE_SETNICK = 1;
	private EditText usernameEditText;
	private EditText passwordEditText;
	public static LoginActivity loginActivity;
	private boolean progressShow;
	private boolean autoLogin = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		initView();

	}

	/**
	 * 登陆
	 * 
	 * @param view
	 */
	public void login(View view) {
		startActivity(new Intent(LoginActivity.this, MainActivity.class));
//		startActivity(new Intent(LoginActivity.this, ChatActivity.class));
		finish();
	

	}

	
	/**
	 * 注册
	 * 
	 * @param view
	 */
	public void register(View view) {
		startActivityForResult(new Intent(this, RegisterActivity1.class), 0);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		loginActivity=this;
		usernameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);

		// 如果用户名改变，清空密码
		usernameEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				passwordEditText.setText(null);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	
}
