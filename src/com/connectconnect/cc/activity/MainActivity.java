package com.connectconnect.cc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.connectconnect.cc.R;
import com.connectconnect.cc.fragment.CCFragment;
import com.connectconnect.cc.fragment.ChatAllHistoryFragment;
import com.connectconnect.cc.fragment.ContactlistFragment;
import com.connectconnect.cc.fragment.SettingsFragment;
import com.connectconnect.cc.service.PushServiceUserPwd;
import com.connectconnect.cc.util.ToolUtils;

public class MainActivity extends BaseActivity {
	private Button[] mTabs;
	private ContactlistFragment contactListFragment;
	private ChatAllHistoryFragment chatHistoryFragment;
	private SettingsFragment settingFragment;
	private CCFragment ccFragment;
	private Fragment[] fragments;
	public static int dip;
	private int index;
	// 当前fragment的index
	private int currentTabIndex;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent Intent = new Intent(MainActivity.this, PushServiceUserPwd.class);
		Log.d("homeActivity", "homeActivity onCreate");
		startService(Intent);
		initView();
		
	}
	
	/**
	 * button点击事件
	 * 
	 * @param view
	 */
	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.btn_conversation:
			index = 0;
			break;
		case R.id.btn_address_list:
			index = 1;
			break;
		case R.id.btn_setting:
			index = 2;
			break;
		case R.id.btn_cc:
			index = 3;
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		mTabs[currentTabIndex].setSelected(false);
		// 把当前tab设为选中状态
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mTabs = new Button[4];
		mTabs[0] = (Button) findViewById(R.id.btn_conversation);
		mTabs[1] = (Button) findViewById(R.id.btn_address_list);
		mTabs[2] = (Button) findViewById(R.id.btn_setting);
		mTabs[3] = (Button) findViewById(R.id.btn_cc);
		// 把第一个tab设为选中状态
		mTabs[0].setSelected(true);
		dip = ToolUtils.dip2px(MainActivity.this, 14);
		chatHistoryFragment = new ChatAllHistoryFragment();
		contactListFragment = new ContactlistFragment();
		settingFragment = new SettingsFragment();
		ccFragment=new CCFragment();
		fragments = new Fragment[] { chatHistoryFragment, contactListFragment, settingFragment ,ccFragment};
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, chatHistoryFragment)
				.add(R.id.fragment_container, contactListFragment).add(R.id.fragment_container, ccFragment).hide(ccFragment).hide(contactListFragment).show(chatHistoryFragment).commit();
	}
	


}
