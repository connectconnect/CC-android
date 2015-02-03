package com.connectconnect.cc.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.connectconnect.cc.R;
import com.connectconnect.cc.model.MembersModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 
 * @author Mr.Z
 */
public class SortAdapter extends BaseAdapter implements SectionIndexer {
	private ArrayList<MembersModel> list = null;
	private Context mContext;

	public SortAdapter(Context mContext) {
		this.mContext = mContext;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(ArrayList<MembersModel> plist) {
		if (plist != null) {
			list = (ArrayList<MembersModel>) plist.clone();
			notifyDataSetChanged();
		}
	}

	// 释放数据
	public void clearDataList() {
		if (list != null) {
			list.clear();
		}
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final MembersModel mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.membersmodel_items, null);
			viewHolder.tvHead = (ImageView) view.findViewById(R.id.head_img);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.post = (TextView) view.findViewById(R.id.post);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		// 根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getIndex());
		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}


		viewHolder.tvTitle.setText(this.list.get(position).getName());
		 viewHolder.tvTitle.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

		return view;

	}

	final static class ViewHolder {
		ImageView tvHead;
		TextView tvLetter;
		TextView tvTitle;
		TextView post;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getIndex().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getIndex();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}
