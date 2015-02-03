package com.connectconnect.cc.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.connectconnect.cc.R;
import com.connectconnect.cc.util.ToolUtils;
import com.connectconnect.cc.util.UserinfoState;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;



/**
 * 开屏页
 *
 */
public class SplashActivity extends BaseActivity {
	private LinearLayout rootLayout;
	private TextView versionText;
	private String mac, auth_session;
	private static final int sleepTime = 2500;

	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.activity_splash);
		super.onCreate(arg0);
		initView();
		
	}
	/**
	 * 获取WiFi下的ip
	 *
	 */
	
	public String getLocalMacAddress() {
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		System.out.println("info.getMacAddress()---------------->"+info.getMacAddress());
		return info.getMacAddress();
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		rootLayout = (LinearLayout) findViewById(R.id.splash_root);
		versionText = (TextView) findViewById(R.id.tv_version);
		mac = getLocalMacAddress();
		auth_session = UserinfoState.getInfoPreference(SplashActivity.this,
				"auth_session", "");
		versionText.setText(ToolUtils.getAppVesion(getApplicationContext()));
		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(2000);
		rootLayout.startAnimation(animation);
		final Intent in = new Intent(SplashActivity.this, LoginActivity.class);
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				startActivity(in);
				
				SplashActivity.this.finish();
			
			}
		};
		timer.schedule(task, sleepTime);
	}
	
}
