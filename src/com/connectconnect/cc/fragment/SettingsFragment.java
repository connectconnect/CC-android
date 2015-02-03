package com.connectconnect.cc.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.connectconnect.cc.R;


/**
 * 设置界面
 * 
 * @author Administrator
 * 
 */
public class SettingsFragment extends Fragment implements OnClickListener {

	/**
	 * 设置新消息通知布局
	 */
	private RelativeLayout rl_switch_notification;
	/**
	 * 设置声音布局
	 */
	private RelativeLayout rl_switch_sound;
	/**
	 * 设置震动布局
	 */
	private RelativeLayout rl_switch_vibrate;
	/**
	 * 设置扬声器布局
	 */
	private RelativeLayout rl_switch_speaker;

	/**
	 * 打开新消息通知imageView
	 */
	private ImageView iv_switch_open_notification;
	/**
	 * 关闭新消息通知imageview
	 */
	private ImageView iv_switch_close_notification;
	/**
	 * 打开声音提示imageview
	 */
	private ImageView iv_switch_open_sound;
	/**
	 * 关闭声音提示imageview
	 */
	private ImageView iv_switch_close_sound;
	/**
	 * 打开消息震动提示
	 */
	private ImageView iv_switch_open_vibrate;
	/**
	 * 关闭消息震动提示
	 */
	private ImageView iv_switch_close_vibrate;
	/**
	 * 打开扬声器播放语音
	 */
	private ImageView iv_switch_open_speaker;
	/**
	 * 关闭扬声器播放语音
	 */
	private ImageView iv_switch_close_speaker;

	/**
	 * 声音和震动中间的那条线
	 */
	private TextView textview1, textview2;

	private LinearLayout blacklistContainer;
	
	/**
	 * 退出按钮
	 */
	private Button logoutBtn;

 
	/**
	 * 诊断
	 */
	private LinearLayout llDiagnose;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_conversation_settings, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
		

	
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	

}
