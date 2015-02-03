package com.connectconnect.cc.adapter;

import java.util.ArrayList;

import com.connectconnect.cc.R;
import com.connectconnect.cc.activity.ChatActivity;
import com.connectconnect.cc.adapter.SortAdapter.ViewHolder;
import com.connectconnect.cc.classes.MessageJson;
import com.connectconnect.cc.view.MediaPlayerutil;
import com.connectconnect.cc.view.SmileUtils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class ChatAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<MessageJson> list;
	
	public ChatAdapter(Context context){
		this.context=context;
	}
	// 更新数据
	public void setList(ArrayList<MessageJson> plist) {
		if (plist != null) {
			list = (ArrayList<MessageJson>) plist.clone();
		}

	}
	// 更新数据
		public void setDataList(ArrayList<MessageJson> plist) {
			if (plist != null) {
				list = (ArrayList<MessageJson>) plist.clone();
				notifyDataSetChanged();
			}

		}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
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
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder=new  ViewHolder();
			convertView = View.inflate(context, R.layout.chatlist_item, null);
			viewHolder.text_context=(TextView) convertView.findViewById(R.id.text_context);
			convertView.setTag(viewHolder);
		} else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		if (list.get(position).getType().equals("0")) {
			Spannable span = SmileUtils.getSmiledText(context,
					list.get(position).getMessage());
			// 设置内容
			viewHolder.text_context.setText(span, BufferType.SPANNABLE);
		}else {
			MediaPlayer mp = MediaPlayer.create(context,
					Uri.parse(list.get(position).getMessage()));
			final int duration = mp.getDuration() / 1000;
			viewHolder.text_context.setText(duration + "S");
			viewHolder.text_context.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub;
							((TextView)arg0).setEnabled(false);
							MediaPlayerutil mediaPlayerutil = new MediaPlayerutil(
									(TextView)arg0, duration,
									context);
							mediaPlayerutil.playUrl(list.get(position).getMessage());
						}
					});
		}
		
		return convertView;
	}
	 class ViewHolder{
		TextView text_context;
	}
}
