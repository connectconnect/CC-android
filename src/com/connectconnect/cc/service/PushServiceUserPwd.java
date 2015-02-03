package com.connectconnect.cc.service;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.connectconnect.cc.R;
import com.connectconnect.cc.activity.ChatActivity;
import com.connectconnect.cc.activity.MyApplication;


public class PushServiceUserPwd extends Service  {

	private String MSG_State    = "2";//打开进入的
	
	// constants used to notify the Activity UI of received messages
    public static final String MQTT_MSG_RECEIVED_INTENT = "org.mosquitto.android.powerviewer.MSGRECVD";
    public static final String MQTT_MSG_RECEIVED_TOPIC  = "org.mosquitto.android.powerviewer.MSGRECVD_TOPIC";
    public static final String MQTT_MSG_RECEIVED_MSG    = "org.mosquitto.android.powerviewer.MSGRECVD_MSGBODY";
    public static final String MQTT_MSG_RECEIVED_GTOPIC    = "org.mosquitto.android.powerviewer.MQTT_MSG_RECEIVED_GTOPIC";
    public static final String MQTT_MSG_Count   =  "org.mosquitto.android.powerviewer.MQTT_MSG_Count";
    
    private TopicReceiver topicReceiver;
    private InChatReceiver inChatReceiver;
    public static final String MQTT_MSG_RECEIVED_INChat  = "org.mosquitto.android.powerviewer.inChatReceiver";
    public static final String MQTT_MSG_RECEIVED_INChat_Type  = "org.mosquitto.android.powerviewer.inChatReceiverType";
    public static final String MQTT_MSG_RECEIVED_INChat_Userid  = "org.mosquitto.android.powerviewer.inChatReceiverUserid";
    
    // constants used to tell the Activity UI the connection status
    public static final String MQTT_STATUS_INTENT = "org.mosquitto.android.powerviewer.STATUS";
    public static final String MQTT_STATUS_MSG    = "org.mosquitto.android.powerviewer.STATUS_MSG";

 // constant used internally to schedule the next ping event
    public static final String MQTT_PING_ACTION = "org.mosquitto.android.powerviewer.PING";

    // constants used by status bar notifications
    public static final int MQTT_NOTIFICATION_ONGOING = 1;
    public static final int MQTT_NOTIFICATION_UPDATE  = 2;

    // constants used to define MQTT connection status
    public enum MQTTConnectionStatus
    {
        INITIAL,                            // initial status
        CONNECTING,                         // attempting to connect
        CONNECTED,                          // connected
        NOTCONNECTED_WAITINGFORINTERNET,    // can't connect because the phone
                                            //     does not have Internet access
        NOTCONNECTED_USERDISCONNECT,        // user has explicitly requested
                                            //     disconnection
        NOTCONNECTED_DATADISABLED,          // can't connect because the user
                                            //     has disabled data access
        NOTCONNECTED_UNKNOWNREASON          // failed to connect for some reason
    }

    // MQTT constants
    public static final int MAX_MQTT_CLIENTID_LENGTH = 22;

    /************************************************************************/
    /*    VARIABLES used to maintain state                                  */
    /************************************************************************/

    // status of MQTT client connection
    private MQTTConnectionStatus connectionStatus = MQTTConnectionStatus.INITIAL;

    /************************************************************************/
    /*    VARIABLES used to configure MQTT connection                       */
    /************************************************************************/

    // taken from preferences
    //    host name of the server we're receiving push notifications from
    private String          brokerHostName       = "tcp://182.92.189.224:1883";
    // taken from preferences
    //    topic we want to receive messages about
    //    can include wildcards - e.g.  '#' matches anything  
    private String[] topicName = { "/huiyibang/p/uid","/huiyibang/u/uid", "/huiyibang/g/uid"};
    // defaults - this sample uses very basic defaults for it's interactions
    //   with message brokers
    private int             brokerPortNumber     = 1883;
    private boolean         cleanStart           = false;
    private int[]           qualitiesOfService   = { 1,2,1} ;

    //  how often should the app ping the server to keep the connection alive?
    //
    //   too frequently - and you waste battery life
    //   too infrequently - and you wont notice if you lose your connection
    //                       until the next unsuccessfull attempt to ping
    //
    //   it's a trade-off between how time-sensitive the data is that your
    //      app is handling, vs the acceptable impact on battery life
    //
    //   it is perhaps also worth bearing in mind the network's support for
    //     long running, idle connections. Ideally, to keep a connection open
    //     you want to use a keep alive value that is less than the period of
    //     time after which a network operator will kill an idle connection
    private short           keepAliveSeconds     = 20 * 60; 

    // This is how the Android client app will identify itself to the
    //  message broker.
    // It has to be unique to the broker - two clients are not permitted to
    //  connect to the same broker using the same client ID.
    private String          mqttClientId = null; 

    // receiver that notifies the Service when the phone gets data connection
    private NetworkConnectionIntentReceiver netConnReceiver;

    // receiver that wakes the Service up when it's time to ping the server
    private PingSender pingSender;

	public String msgOpen;
	
	private String userName = "cc2";
	private String passWord = "123456";
	private MqttClient client;
	private MqttConnectOptions options;
	
	private void connect() {
		System.out.println("111-----------------------");
		Log.d("mq6tt", "connect connect connect");
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					if(!client.isConnected()) {
						System.out.println("222-----------------------");
						client.connect(options);
						System.out.println("3333-----------------------");
						String[] topics = topicName;
						int[] qualitiesOfService   = { 0,1,2 } ;
						try {
							System.out.println("5555-----------------------");
							client.subscribe(topics, qualitiesOfService);
							System.out.println("6666-----------------------");
							Log.d("mqtt", "------------------client.subscribe="+topics);
						} catch (MqttException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					connect();
				}
			}
		}).start();
	}
	private String generateClientId()
    {
        // generate a unique client id if we haven't done so before, otherwise
        //   re-use the one we already have

        if (mqttClientId == null)
        {
            // generate a unique client ID - I'm basing this on a combination of
            //  the phone device id and the current timestamp
//            String timestamp = "" + (new Date()).getTime();
//            String android_id = Settings.System.getString(getContentResolver(),
//                                                          Secure.ANDROID_ID);
//            mqttClientId = timestamp + android_id;
//
//            // truncate - MQTT spec doesn't allow client ids longer than 23 chars
//            if (mqttClientId.length() > MAX_MQTT_CLIENTID_LENGTH) {
//                mqttClientId = mqttClientId.substring(0, MAX_MQTT_CLIENTID_LENGTH);
//            }
        	mqttClientId=MyApplication.sUdid;
        	System.out.println("mqttcliedtid---------------->"+mqttClientId);
        }

        return mqttClientId;
    }
	private void init() {
		try {
                       //host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
//			client = new MqttClient(brokerHostName, generateClientId(),
//					new MemoryPersistence());
                       //MQTT的连接设置
			client = new MqttClient(brokerHostName, generateClientId(),
					new MemoryPersistence());
			System.out.println("client---------"+
					client.toString());
			options = new MqttConnectOptions();
                       //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
			options.setCleanSession(true);
                       //设置连接的用户名
			options.setUserName(userName);
                       //设置连接的密码
			options.setPassword(passWord.toCharArray());
			// 设置超时时间 单位为秒
			options.setConnectionTimeout(10);
			// 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
			options.setKeepAliveInterval(20);
                        //设置回调
			
			client.setCallback(new MqttCallback() {
				
				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
                                        //publish后会执行到这里
					System.out.println("deliveryComplete---------"
							+ token.isComplete());
				}

				@Override
				public void messageArrived(String topicName, MqttMessage message)
						throws Exception {
		                     //subscribe后得到的消息会执行到这里面
							Log.d("mqtt", "messageArrived----------"+topicName + "---------"+ message);
							received(topicName, message.toString());
				}

				@Override
				public void connectionLost(Throwable arg0) {
					// TODO Auto-generated method stub
					PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
			        WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MQTT");
			        wl.acquire();
			        if (isOnline() == false)
			        {
			            connectionStatus = MQTTConnectionStatus.NOTCONNECTED_WAITINGFORINTERNET;
			        }
			        else
			        {
			        	Log.d("mqt2t", "connectionLost----------connectionLost");
			            if(!client.isConnected()) {
							connect();
							Log.d("mqt5t", "connectionLost----------connectionLost----------connectionLost");
						}else {
							Log.d("mqt6t", "connectionLost----------成功");

						}
			        }
			        wl.release();
				}
			});
			if(!client.isConnected()) {
				Log.d("mqt3t", "connectionLost---------isConnectedt");
				connect();
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
    @Override
    public void onCreate()
    {
        super.onCreate();
        msgOpen = "3";
        // reset status variable to initial state
        connectionStatus = MQTTConnectionStatus.INITIAL;

        // create a binder that will let the Activity UI send
        //   commands to the Service
        if (mBinder == null)
        {
        	mBinder = new LocalBinder<PushServiceUserPwd>(this);
        }

        Log.d("mqtt", "onCreate onCreate onCreate");
        
//        topicName[0] = "/huiyibang/p/" + userinfoState.getInfoPreference(PushServiceUserPwd.this, "uid", "1");
//        topicName[1] = "/huiyibang/u/" + userinfoState.getInfoPreference(PushServiceUserPwd.this, "uid", "1");
//        topicName[2] = "/huiyibang/g/" + "gid";
        
        init();
        
        if (topicReceiver == null)
        {
        	topicReceiver = new TopicReceiver();
    		IntentFilter intentSFilter = new IntentFilter(PushServiceUserPwd.MQTT_MSG_RECEIVED_TOPIC);
    		registerReceiver(topicReceiver, intentSFilter);
        }
        if (inChatReceiver == null)
        {
        	inChatReceiver = new InChatReceiver();
    		IntentFilter intentSFilter = new IntentFilter(PushServiceUserPwd.MQTT_MSG_RECEIVED_INChat);
    		registerReceiver(inChatReceiver, intentSFilter);
        }
        
    }
    public class TopicReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			Bundle notificationData = intent.getExtras();
			String gidTopic = notificationData.getString(PushServiceUserPwd.MQTT_MSG_RECEIVED_GTOPIC);
			topicName[2] = gidTopic;
			Log.d("PushServiceUserPwd", "group topic receive ==="+gidTopic);
	    	subscribeToTopic(topicName);
		}
	}
    public class InChatReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			Bundle notificationData = intent.getExtras();
			msgOpen = notificationData.getString(PushServiceUserPwd.MQTT_MSG_RECEIVED_INChat);
			Log.d("PushServiceUserPwd", "InChatReceiver==="+msgOpen);
			
		}
	}
    
    synchronized void handleStart(Intent intent, int startId)
    {
    	Log.d("PushserviceUserPwd", "handleStart");
    	ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        if (cm.getBackgroundDataSetting() == false) // respect the user's request not to use data!
        {
            // user has disabled background data
            connectionStatus = MQTTConnectionStatus.NOTCONNECTED_DATADISABLED;

            // update the app to show that the connection has been disabled
            broadcastServiceStatus("Not connected - background data disabled");
            Log.d("PushserviceUserPwd", "Not connected - background data disabled");
            // we have a listener running that will notify us when this
            //   preference changes, and will call handleStart again when it
            //   is - letting us pick up where we leave off now
            return;
        }
        
        // if the Service was already running and we're already connected - we
        //   don't need to do anything
        if (!client.isConnected())
        {
            // set the status to show we're trying to connect
            connectionStatus = MQTTConnectionStatus.CONNECTING;

            // before we attempt to connect - we check if the phone has a
            //  working data connection
            if (isOnline())
            {
            	connect();
            }
            else
            {
                // we can't do anything now because we don't have a working
                //  data connection
                connectionStatus = MQTTConnectionStatus.NOTCONNECTED_WAITINGFORINTERNET;

                // inform the app that we are not connected
                broadcastServiceStatus("Waiting for network connection");
            }
        }

        // changes to the phone's network - such as bouncing between WiFi
        //  and mobile data networks - can break the MQTT connection
        // the MQTT connectionLost can be a bit slow to notice, so we use
        //  Android's inbuilt notification system to be informed of
        //  network changes - so we can reconnect immediately, without
        //  haing to wait for the MQTT timeout
        if (netConnReceiver == null)
        {
            netConnReceiver = new NetworkConnectionIntentReceiver();
            registerReceiver(netConnReceiver,
                             new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        }

        // creates the intents that are used to wake up the phone when it is
        //  time to ping the server
        if (pingSender == null)
        {
            pingSender = new PingSender();
            registerReceiver(pingSender, new IntentFilter(MQTT_PING_ACTION));
        }
    }
    /*
     * Used to implement a keep-alive protocol at this Service level - it sends
     *  a PING message to the server, then schedules another ping after an
     *  interval defined by keepAliveSeconds
     */
    public class PingSender extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // Note that we don't need a wake lock for this method (even though
            //  it's important that the phone doesn't switch off while we're
            //  doing this).
            // According to the docs, "Alarm Manager holds a CPU wake lock as
            //  long as the alarm receiver's onReceive() method is executing.
            //  This guarantees that the phone will not sleep until you have
            //  finished handling the broadcast."
            // This is good enough for our needs.
        	if(!client.isConnected()) {
				connect();
			}
        	
            // start the next keep alive period
            scheduleNextPing();
        }
    }
    private void broadcastServiceStatus(String statusDescription)
    {
        // inform the app (for times when the Activity UI is running /
        //   active) of the current MQTT connection status so that it
        //   can update the UI accordingly
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MQTT_STATUS_INTENT);
        broadcastIntent.putExtra(MQTT_STATUS_MSG, statusDescription);
        sendBroadcast(broadcastIntent);
    }
    private Hashtable<String, String> dataCache = new Hashtable<String, String>(); 

    private boolean addReceivedMessageToStore(String key, String value)
    {
        String previousValue = null;

        if (value.length() == 0)
        {
            previousValue = dataCache.remove(key);
        }
        else
        {
            previousValue = dataCache.put(key, value);
        }

        // is this a new value? or am I receiving something I already knew?
        //  we return true if this is something new
        return ((previousValue == null) ||
                (previousValue.equals(value) == false));
    }

    private void broadcastReceivedMessage(String topic, String message)
    {
        // pass a message received from the MQTT server on to the Activity UI
        //   (for times when it is running / active) so that it can be displayed
        //   in the app GUI
    	
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MQTT_MSG_RECEIVED_INTENT);
        broadcastIntent.putExtra(MQTT_MSG_RECEIVED_TOPIC, topic);
        broadcastIntent.putExtra(MQTT_MSG_RECEIVED_MSG,   message);
        sendBroadcast(broadcastIntent);
    }
    @Override
    public void onStart(final Intent intent, final int startId)
    {
        // This is the old onStart method that will be called on the pre-2.0
        // platform.  On 2.0 or later we override onStartCommand() so this
        // method will not be called.        
    	Log.d("mqtt", "onStart onStart onStart");
        new Thread(new Runnable() {
            @Override
            public void run() {
                handleStart(intent, startId);
            }
        }, "PushServiceUserPwd").start();
    }
    @Override
    public int onStartCommand(final Intent intent, int flags, final int startId)
    {
    	Log.d("mqtt", "onStartCommand onStartCommand onStartCommand");
		// reset status variable to initial state
        connectionStatus = MQTTConnectionStatus.INITIAL;

        // create a binder that will let the Activity UI send
        //   commands to the Service
        if (mBinder == null)
        {
        	mBinder = new LocalBinder<PushServiceUserPwd>(this);
        }
//        
//        topicName[0] = "/huiyibang/p/" + userinfoState.getInfoPreference(PushServiceUserPwd.this, "uid", "1");
//        topicName[1] = "/huiyibang/u/" + userinfoState.getInfoPreference(PushServiceUserPwd.this, "uid", "1");
//        topicName[2] = "/huiyibang/g/" + "gid";
        
        if (topicReceiver == null)
        {
	        topicReceiver = new TopicReceiver();
			IntentFilter intentSFilter = new IntentFilter(PushServiceUserPwd.MQTT_MSG_RECEIVED_TOPIC);
			registerReceiver(topicReceiver, intentSFilter);
        }
        if (inChatReceiver == null)
        {
        	inChatReceiver = new InChatReceiver();
    		IntentFilter intentSFilter = new IntentFilter(PushServiceUserPwd.MQTT_MSG_RECEIVED_INChat);
    		registerReceiver(inChatReceiver, intentSFilter);
        }
        // define the connection to the broker
        init();
        if (client.isConnected())
        {
	        new Thread(new Runnable() {
	            @Override
	            public void run() {
	                handleStart(intent, startId);
	            }
	        }, "PushServiceUserPwd").start();
        }
        // return START_NOT_STICKY - we want this Service to be left running
        //  unless explicitly stopped, and it's process is killed, we want it to
        //  be restarted
        return START_STICKY;
    }
    
    private LocalBinder<PushServiceUserPwd> mBinder;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	public class LocalBinder<S> extends Binder
    {
        private WeakReference<S> mService;

        public LocalBinder(S service)
        {
            mService = new WeakReference<S>(service);
        }
        public S getService()
        {
            return mService.get();
        }
        public void close()
        {
            mService = null;
        }
    }

	private ScreenBroadcastReceiver mScreenReceiver;
	private class ScreenBroadcastReceiver extends BroadcastReceiver {
	    private String action = null;
	 
	 
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        action = intent.getAction();
	        if (Intent.ACTION_SCREEN_ON.equals(action)) {
	        	Log.d("PushserviceUserPwd", "// 开屏");
	        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { 
	        	Log.d("PushserviceUserPwd", "// 锁屏");// 锁屏
	        } else if (Intent.ACTION_USER_PRESENT.equals(action)) { 
	        	Log.d("PushserviceUserPwd", "// 解锁");// 解锁
	        }
	    }
	}
	@SuppressWarnings("unused")
	private void startScreenBroadcastReceiver() {
	    IntentFilter filter = new IntentFilter();
	    filter.addAction(Intent.ACTION_SCREEN_ON);
	    filter.addAction(Intent.ACTION_SCREEN_OFF);
	    filter.addAction(Intent.ACTION_USER_PRESENT);
	    registerReceiver(mScreenReceiver, filter);
	}

	private void received(String topic, String messageBody) throws Exception {
		// TODO Auto-generated method stub
		// we protect against the phone switching off while we're doing this
        //  by requesting a wake lock - we request the minimum possible wake
        //  lock - just enough to keep the CPU running until we've finished
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MQTT");
        wl.acquire();

        //  for times when the app's Activity UI is not running, the Service
        //   will need to safely store the data that it receives
        if (addReceivedMessageToStore(topic, messageBody))
        {
            // this is a new message - a value we haven't seen before
        	//
            // inform the app (for times when the Activity UI is running) of the
            //   received message so the app UI can be updated with the new data
        	Log.d("PushserviceUserPwd", "msgbody="+messageBody+" now topicU="+topic);
        	if(isBackground(PushServiceUserPwd.this) || !pm.isScreenOn())
        	{
        		notifyUser("收到新消息", "会议邦", messageBody);
	      		return;
        	}

        	Log.d("mqtt", "broadcast ReceivedMessage=bbbbbbbbb=bbbbbbbbb="+msgOpen);
        	if (msgOpen.equals(MSG_State))
        	{
        		broadcastReceivedMessage(topic, messageBody);
        	}
        	else if(msgOpen.equals("3"))
        	{
        		notifyUser("收到新消息", "会议邦", messageBody);
        	}
        	else
        	{
      		  	Intent broadcastIntent = new Intent();
    	        broadcastIntent.setAction(PushServiceUserPwd.MQTT_MSG_Count);
    	        sendBroadcast(broadcastIntent);
    	        Vibrator vib = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
      		  	vib.vibrate(2000);
      		  	TipHelper.PlaySound(getApplicationContext());
        	}
        }

        // receiving this message will have kept the connection alive for us, so
        //  we take advantage of this to postpone the next scheduled ping
        scheduleNextPing();

        // we're finished - if the phone is switched off, it's okay for the CPU
        //  to sleep now
        wl.release();
	}
	
	private boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null &&
           cm.getActiveNetworkInfo().isAvailable() &&
           cm.getActiveNetworkInfo().isConnected())
        {
            return true;
        }

        return false;
    }

    /*
     * Schedule the next time that you want the phone to wake up and ping the
     *  message broker server
     */
    private void scheduleNextPing()
    {
        // When the phone is off, the CPU may be stopped. This means that our
        //   code may stop running.
        // When connecting to the message broker, we specify a 'keep alive'
        //   period - a period after which, if the client has not contacted
        //   the server, even if just with a ping, the connection is considered
        //   broken.
        // To make sure the CPU is woken at least once during each keep alive
        //   period, we schedule a wake up to manually ping the server
        //   thereby keeping the long-running connection open
        // Normally when using this Java MQTT client library, this ping would be
        //   handled for us.
        // Note that this may be called multiple times before the next scheduled
        //   ping has fired. This is good - the previously scheduled one will be
        //   cancelled in favour of this one.
        // This means if something else happens during the keep alive period,
        //   (e.g. we receive an MQTT message), then we start a new keep alive
        //   period, postponing the next ping.

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                                                                 new Intent(MQTT_PING_ACTION),
                                                                 PendingIntent.FLAG_UPDATE_CURRENT);

        // in case it takes us a little while to do this, we try and do it
        //  shortly before the keep alive period expires
        // it means we're pinging slightly more frequently than necessary
        Calendar wakeUpTime = Calendar.getInstance();
        wakeUpTime.add(Calendar.SECOND, keepAliveSeconds);

        AlarmManager aMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        aMgr.set(AlarmManager.RTC_WAKEUP,
                 wakeUpTime.getTimeInMillis(),
                 pendingIntent);
    }

    /*
     * Send a request to the message broker to be sent messages published with
     *  the specified topic name. Wildcards are allowed.
     */
    private void subscribeToTopic(String[] topicName)
    {
        boolean subscribed = false;

        if (!client.isConnected())
        {
            // quick sanity check - don't try and subscribe if we
            //  don't have a connection
        	Log.e("mqtt", "connect Unable to subscribe as we are not connected");
        	connect();
        	subscribed = true;
            
        }
        else
        {
        	try {
				client.subscribe(topicName, qualitiesOfService);
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	subscribed = true;
        }
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        if (subscribed == false)
        {
        	connect();
        	try {
				client.subscribe(topicName, qualitiesOfService);
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        scheduleNextPing();
    }
    private void notifyUser(String alert, String title, String body)
    {    	
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.ic_launcher, alert,
                                                     System.currentTimeMillis());
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.ledARGB = Color.MAGENTA;

		JSONObject root = null;
		try {
			root = new JSONObject(body);
			String msgContent = root.getString("msg");

			Intent notificationIntent = new Intent(this, ChatActivity.class);
			notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			notificationIntent.addFlags(Intent.FILL_IN_DATA);
			notificationIntent.putExtra("type", root.getString("chat_type"));
			notificationIntent.putExtra("user_id", root.getString("chat_id"));
			notificationIntent.putExtra("shield_sw", root.getString("shield_sw"));
			notificationIntent.putExtra("state", root.getString("is_friend"));
			notificationIntent.putExtra("title", root.getString("chat_name"));
	        
	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
	                                                                notificationIntent,
	                                                                PendingIntent.FLAG_UPDATE_CURRENT);
	        notification.setLatestEventInfo(PushServiceUserPwd.this, title, msgContent, contentIntent);
	
	        nm.notify(MQTT_NOTIFICATION_UPDATE, notification);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	/*
     * Called in response to a change in network connection - after losing a
     *  connection to the server, this allows us to wait until we have a usable
     *  data connection again
     */
    private class NetworkConnectionIntentReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context ctx, Intent intent)
        {
            // we protect against the phone switching off while we're doing this
            //  by requesting a wake lock - we request the minimum possible wake
            //  lock - just enough to keep the CPU running until we've finished
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MQTT");
            wl.acquire();

            if (isOnline())
            {
                // we have an internet connection - have another try at connecting
                if (!client.isConnected())
                {
                    // we subscribe to a topic - registering to receive push
                    //  notifications with a particular key
                    connect();
                }
            }

            // we're finished - if the phone is switched off, it's okay for the CPU
            //  to sleep now
            wl.release();
        }
    }
    public static boolean isBackground(Context context) {


    	ActivityManager activityManager = (ActivityManager) context
    	.getSystemService(Context.ACTIVITY_SERVICE);
    	List<RunningAppProcessInfo> appProcesses = activityManager
    	.getRunningAppProcesses();
    	for (RunningAppProcessInfo appProcess : appProcesses) {
    	if (appProcess.processName.equals(context.getPackageName())) {
    	if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
    	Log.d("PushserviceUserPwd","Background App:"+appProcess.processName);
    	return true;
    	}else{
    		Log.d("PushserviceUserPwd","Foreground App:"+appProcess.processName);
    	return false;
    	}
    	}
    	}
    	return false;
    	}
}
