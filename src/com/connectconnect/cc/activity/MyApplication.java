package com.connectconnect.cc.activity;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.connectconnect.cc.service.PushServiceUserPwd;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;


/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class MyApplication extends Application {
	public static String sUdid;
	  @Override
	    public void onCreate() {
	        super.onCreate();
	        
	        init();
	    }


	    private void init() {
	    	initImageLoader(getApplicationContext());
	        MyVolley.init(this);
	        UuidUtil util=new UuidUtil(getApplicationContext());
	        sUdid=util.getUDID();
	    }
		public static void initImageLoader(Context context) {
			// This configuration tuning is custom. You can tune every option, you may tune some of them,
			// or you can create default configuration by
			//  ImageLoaderConfiguration.createDefault(this);
			// method.
		
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
					.threadPriority(Thread.NORM_PRIORITY - 2)
					.denyCacheImageMultipleSizesInMemory()
					.discCacheFileNameGenerator(new Md5FileNameGenerator())
					.tasksProcessingOrder(QueueProcessingType.LIFO)
					.writeDebugLogs() // Remove for release app
					.build();
			// Initialize ImageLoader with configuration.
			ImageLoader.getInstance().init(config);
		}
		public   static  class UuidUtil{
		 	   protected static final String PREFS_FILE = "gank_device_id.xml";    
				    protected static final String PREFS_DEVICE_ID = "gank_device_id";  
				    protected static String uuid;  
				    private static Context contexts;
				    public UuidUtil(Context context){
				    	UuidUtil.contexts=context;
				    }
				    static public String getUDID()  
				    {  
				        if( uuid ==null ) {  
				            synchronized (MyApplication.class) {    
				                if( uuid == null) {    
				                    final SharedPreferences prefs = contexts.getSharedPreferences( PREFS_FILE, 0);    
				                    final String id = prefs.getString(PREFS_DEVICE_ID, null );    
				    
				                    if (id != null) {    
				                        // Use the ids previously computed and stored in the prefs file    
				                        uuid = id;    
				                    } else {    
				    
				                        final String androidId = Secure.getString(contexts.getContentResolver(), Secure.ANDROID_ID);    
				    
				                        // Use the Android ID unless it's broken, in which case fallback on deviceId,    
				                        // unless it's not available, then fallback on a random number which we store    
				                        // to a prefs file    
				                        try {    
				                            if (!"9774d56d682e549c".equals(androidId)) {    
				                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();    
				                            } else {    
				                                final String deviceId = ((TelephonyManager) contexts.getSystemService( Context.TELEPHONY_SERVICE )).getDeviceId();    
				                                uuid = deviceId!=null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString() : UUID.randomUUID().toString();    
				                            }    
				                        } catch (UnsupportedEncodingException e) {    
				                            throw new RuntimeException(e);    
				                        }    
				    
				                        // Write the value out to the prefs file    
				                        prefs.edit().putString(PREFS_DEVICE_ID, uuid).commit();    
				                    }  
				                }    
				            }    
				        }  
				        return uuid;  
				    }  
		 }
}