package com.connectconnect.cc.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {
	static final String ACTION = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent Intent = new Intent(context, PushServiceUserPwd.class);
		System.out.println("zoulemeeeeee---------"
				);
		context.startService(Intent);
	}
}