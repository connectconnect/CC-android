package com.connectconnect.cc.adapter;

import com.connectconnect.cc.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ChatAllHistoryAdapter extends BaseAdapter{
	private Context context;
	public ChatAllHistoryAdapter(Context context){
		this.context=context;
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = View.inflate(context, R.layout.chatlist_item, null);
		}
		return convertView;
	}

}
