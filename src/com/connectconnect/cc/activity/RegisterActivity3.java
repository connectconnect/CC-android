package com.connectconnect.cc.activity;

import com.connectconnect.cc.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity3 extends BaseActivity{
	private EditText et_username,et_psd1,et_psd2;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.register_3);
		initView();
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		et_username=(EditText) findViewById(R.id.et_username);
		et_psd1=(EditText) findViewById(R.id.et_psd1);
		et_psd2=(EditText) findViewById(R.id.et_psd2);
		
	}
	/**
	 * 注册完成
	 * 
	 * @param view
	 */
	public void complete(View view) {
		String username=et_username.getText().toString().trim();
		String psd1=et_psd1.getText().toString().trim();
		String psd2=et_psd2.getText().toString().trim();
		if (username.equals("")) {
			et_username.setError("真实姓名不能为空");
		}else {
			if (psd1.equals("")&&psd2.equals("")) {
				et_psd1.setError("密码不能为空");
				et_psd2.setError("确认密码不能为空");
			}else {
				if (!psd1.equals("")&&!psd2.equals("")) {
					if (psd1.toCharArray().length<6) {
						et_psd1.setError("密码不能少于6位");
					}else {
						if (psd1.equals(psd2)) {
							submit();
						}else {
							Toast.makeText(getApplicationContext(), "两次密码不一致请核对", Toast.LENGTH_SHORT).show();
						}
					}
					
				}else {
					if (psd1.equals("")==true&&psd2.equals("")==false) {
						
						et_psd1.setError("密码不能为空");
					}
					if (psd2.equals("")==true&&psd1.equals("")==false) {
						et_psd2.setError("确认密码不能为空");
					}
				}
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
	 * 提交
	 * 
	 * @param view
	 */
	public void submit(){
		LoginActivity.loginActivity.finish();
		RegisterActivity1.registerActivity1.finish();
		RegisterActivity2.registerActivity2.finish();
		startActivity(new Intent(RegisterActivity3.this, LoginActivity.class));
		finish();
	}
}
