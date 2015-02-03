package com.connectconnect.cc.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.connectconnect.cc.activity.MyVolley;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/*
 * 封装网络的操作工具类，实现可以从网络获取或者上传数据
 * 
 */
public class HttpconnectionUtil {
	

	public interface ReturnResult {
		public void getResult(String result);
	}


	
	  public static  void  uploadFile(final Context context,final ReturnResult callback,final int flag,final String url ,final Map<String, String> params)
    {
//		  RequestQueue  queue=Volley.newRequestQueue(context);
		  if (flag==Method.GET) {
			  StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						callback.getResult(response);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
			        	System.out.println("-------------->"+error.networkResponse.statusCode);

						Log.e("LOGIN-ERROR", error.getMessage(), error);
			        	byte[] htmlBodyBytes = error.networkResponse.data;
			        	Log.e("LOGIN-ERROR", new String(htmlBodyBytes), error);
			        	callback.getResult(new String(htmlBodyBytes));
					}
				});
			    stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

			  MyVolley.getRequestQueue().add(stringRequest);	
		}else {
			 StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
			        @Override
			        public void onResponse(String response) {
			        	System.out.println("response-------------->"+response);
			        	callback.getResult(response);
			        }
			    }, new Response.ErrorListener() {
			        @Override
			        public void onErrorResponse(VolleyError error) {
			        	System.out.println("-------------->"+error.networkResponse.statusCode);
			        	Log.e("LOGIN-ERROR", error.getMessage(), error);
			        	byte[] htmlBodyBytes = error.networkResponse.data;
			        	Log.e("LOGIN-ERROR", new String(htmlBodyBytes), error);
			        }
			    }){
			        @Override
			        protected Map<String,String> getParams(){
//			            Map<String,String> params = new HashMap<String, String>();
//			            params.put("User_Name", "1");
//			            params.put("Mobile", "1");
//			            params.put("MessageContent", "1");
			            return params;
			        }
			  
			       
			    };	
			    stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			    MyVolley.getRequestQueue().add(stringRequest);	
		}
		  
		
			
			   
			 
				
			
	}
	  


}
