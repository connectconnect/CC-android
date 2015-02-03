package com.connectconnect.cc.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.connectconnect.cc.R;
import com.connectconnect.cc.activity.ChatActivity;
import com.connectconnect.cc.adapter.ChatAllHistoryAdapter;


/**
 * 聊天记录Fragment
 * 
 */
public class ChatAllHistoryFragment extends Fragment {
	private ListView listView;
	private ChatAllHistoryAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_conversation_history, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		listView=(ListView) getView().findViewById(R.id.list);
		adapter=new ChatAllHistoryAdapter(getActivity());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),ChatActivity.class);
				startActivity(intent);
			}
		});
		
		

	}

	
	
	
}
