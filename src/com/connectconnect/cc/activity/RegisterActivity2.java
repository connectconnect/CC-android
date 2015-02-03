package com.connectconnect.cc.activity;

import com.connectconnect.cc.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity2 extends BaseActivity{
	private EditText input_text_input;
	public static RegisterActivity2 registerActivity2;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.register_2);
		initView();
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		registerActivity2=this;
		input_text_input=(EditText) findViewById(R.id.input_text_input);
	}
	/**
	 * 登陆
	 * 
	 * @param view
	 */
	public void next(View view) {
		if (input_text_input.getText().toString().trim().equals("")) {
			input_text_input.setError("验证码不能为空");
			
		}else{if (input_text_input.getText().toString().trim().equals("523123")) {
			Toast.makeText(getApplicationContext(), "验证码输入正确", Toast.LENGTH_SHORT).show();
			startActivityForResult(new Intent(RegisterActivity2.this, RegisterActivity3.class), 0);
			}else {
				Toast.makeText(getApplicationContext(), "验证码错误请重新输入", Toast.LENGTH_SHORT).show();

			}
		
		}
	}
	/**
	 * 重新获取验证码
	 * 
	 * @param view
	 */
	public void restart(View view) {
		Toast.makeText(getApplicationContext(), "验证码已发到你的邮箱请注意查收", Toast.LENGTH_SHORT).show();

	}
	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back(View view) {
		
		Intent intent=new Intent();
		setResult(11, intent);
		finish();

	}


}
