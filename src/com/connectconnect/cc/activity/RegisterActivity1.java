package com.connectconnect.cc.activity;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request.Method;
import com.connectconnect.cc.R;
import com.connectconnect.cc.util.HttpconnectionUtil;
import com.connectconnect.cc.util.URLManager;
import com.connectconnect.cc.util.HttpconnectionUtil.ReturnResult;
import com.connectconnect.cc.util.ToolUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity1 extends BaseActivity{
	private EditText input_text_input;
	public static RegisterActivity1 registerActivity1;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.register_1);
		initView();
		
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		registerActivity1=this;
		input_text_input=(EditText) findViewById(R.id.input_text_input);
	}
	/**
	 * 下一步
	 * 
	 * @param view
	 */
	public void next(View view) {
		String email=input_text_input.getText().toString().trim();
		if (email.equals("")) {
			input_text_input.setError("邮箱不能为空");
		} else {
			if (!ToolUtils.isEmail(email)) {
				input_text_input.setError("邮箱格式不正确");
			}else {
				getVfCode(email);
			}
		}
		

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
	/**
	 * 获取验证码
	 * 
	 * @param view
	 */
	public void getVfCode(String email){
		Map<String, String> params2 = new HashMap<String, String>();
		params2.put("email", email);
		String json = ToolUtils.toJSONString(params2);
		Map<String, String> params = ToolUtils.getMap(RegisterActivity1.this,
				"");
		params.put("params", json);
		System.out.println("params--------------------->"+params.toString());
		HttpconnectionUtil.uploadFile(RegisterActivity1.this, new ReturnResult() {
			
			@Override
			public void getResult(String result) {
				// TODO Auto-generated method stub
				System.out.println("result---------------->"+result);
				Toast.makeText(RegisterActivity1.this, "验证码已发到你的邮箱请注意查收", Toast.LENGTH_SHORT).show();
				startActivityForResult(new Intent(RegisterActivity1.this, RegisterActivity2.class), 0);
			}
		}, Method.POST, URLManager.CAPTCHA, params);
	}

}
