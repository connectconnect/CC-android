package com.connectconnect.cc.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserinfoState {
	/**
	 * SharedPreferences获取
	 */
	public static Boolean getDownPreference(Context context, String key,
			Boolean defValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"userDown", Context.MODE_PRIVATE);
		Boolean temp = sharedPreferences.getBoolean(key, defValue);
		return temp;
	}
	
	/**
	 * SharedPreferences存入
	 */
	public static void saveDownPreference(Context context, String key,
			Boolean defValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"userDown", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, defValue);
		editor.commit();
	}
	/**
	 * SharedPreferences获取
	 */
	public static String getInfoPreference(Context context, String key,
			String defValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"userinfo", Context.MODE_PRIVATE);
		String temp = sharedPreferences.getString(key, defValue);
		return temp;
	}
	
	/**
	 * SharedPreferences存入
	 */
	public static void saveInfoPreference(Context context, String key,
			String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"userinfo", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * SharedPreferences获取
	 */
	public static String getpasswordPreference(Context context, String key,
			String defValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"remember_pwd", Context.MODE_PRIVATE);
		String temp = sharedPreferences.getString(key, defValue);
		return temp;
	}

	/**
	 * SharedPreferences存入
	 */
	public static void savepasswordPreference(Context context, String key,
			String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"remember_pwd", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	// SharedPreferences清除
	public static void clearLoginInfo(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"userinfo", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}

	// SharedPreferences清除
	public static void clearpasswordInfo(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"remember_pwd", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * SharedPreferences获取
	 */
	public static boolean getPwdPreference(Context context, String key,
			Boolean defValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"pwdmanager", Context.MODE_PRIVATE);
		boolean temp = sharedPreferences.getBoolean(key, defValue);
		return temp;
	}

	/**
	 * SharedPreferences存入
	 */
	public static void savePwdPreference(Context context, String key,
			Boolean value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"pwdmanager", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	// SharedPreferences清除
	public static void clearPwd(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"pwdmanager", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}

}
