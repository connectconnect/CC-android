package com.connectconnect.cc.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.connectconnect.cc.json.JSONValue;

public class ToolUtils {
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 常规请求数据Map
	 */
	public static Map<String, String> getMap(Context context,
			String auth_session) {
		String mac = UserinfoState.getInfoPreference(context, "mac", "");
		long time = System.currentTimeMillis() / 1000;
		Map<String, String> params = new HashMap<String, String>();
		params.put("time", String.valueOf(time));
		params.put("version", ToolUtils.getAppVesion(context));
		params.put("os", "Android");
		params.put("udid", "11");
		params.put("token", "");
		params.put("sign", "");
		params.put("auth_session", auth_session);
		return params;
	}
	/**
	 * 获取应用版本号
	 */
	public static String getAppVesion(Context context) {
		String verCode = "";
		try {
			verCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
			Log.i("mainAct", verCode);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verCode;
	}
	/**
	 * 将map转换成json字符串
	 */
	public static String toJSONString(Map map) {
		if (map == null)
			return "null";

		StringBuffer sb = new StringBuffer();
		boolean first = true;
		Iterator iter = map.entrySet().iterator();

		sb.append('{');
		while (iter.hasNext()) {
			if (first)
				first = false;
			else
				sb.append(',');

			Map.Entry entry = (Map.Entry) iter.next();
			toJSONString(String.valueOf(entry.getKey()), entry.getValue(), sb);
		}
		sb.append('}');
		return sb.toString();
	}
	private static String toJSONString(String key, Object value, StringBuffer sb) {
		sb.append('\"');
		if (key == null)
			sb.append("null");
		else
			JSONValue.escape(key, sb);
		sb.append('\"').append(':');

		sb.append(JSONValue.toJSONString(value));

		return sb.toString();
	}
	/**
	 * 判断email格式是否正确
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}
}
