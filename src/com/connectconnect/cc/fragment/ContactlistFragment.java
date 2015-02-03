package com.connectconnect.cc.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.connectconnect.cc.R;
import com.connectconnect.cc.activity.ChatActivity;
import com.connectconnect.cc.adapter.SortAdapter;
import com.connectconnect.cc.model.Chang;
import com.connectconnect.cc.model.MembersModel;
import com.connectconnect.cc.view.PinyinComparator;
import com.connectconnect.cc.view.SideBar;
import com.connectconnect.cc.view.SideBar.OnTouchingLetterChangedListener;




/**
 * 联系人列表页
 * 
 */
public class ContactlistFragment extends Fragment {
	private TextView floating_header;
	private ListView sortListView;
	private SideBar sideBar;
	private SortAdapter adapter;
	private AutoCompleteTextView search_input;
	private ImageView input_del_img;
	private TextView input_search_img;
	/**
	 * 汉字转换成拼音的类
	 */
	private ArrayList<MembersModel> SourceDateList = new ArrayList<MembersModel>();
	private ArrayList<MembersModel> SourceDateList2 = new ArrayList<MembersModel>();
	
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_contact_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
		sortListView=(ListView) getView().findViewById(R.id.list);
		search_input = (AutoCompleteTextView) getView().findViewById(R.id.search_input);
		input_del_img = (ImageView)getView(). findViewById(R.id.input_del_img);
		input_search_img = (TextView)getView(). findViewById(R.id.input_search_img);
		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar)getView(). findViewById(R.id.sidebar);
		floating_header = (TextView)getView(). findViewById(R.id.floating_header);
		sideBar.setTextView(floating_header);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});
		sortListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent = new Intent(getActivity(),
						ChatActivity.class);
				
				startActivity(intent);
			}
		});

		MembersModel membersModel1=new MembersModel();
		MembersModel membersModel2=new MembersModel();
		MembersModel membersModel3=new MembersModel();
		MembersModel membersModel4=new MembersModel();
		MembersModel membersModel5=new MembersModel();
		MembersModel membersModel6=new MembersModel();
		MembersModel membersModel7=new MembersModel();
		MembersModel membersModel8=new MembersModel();
		MembersModel membersModel9=new MembersModel();
		MembersModel membersModel82=new MembersModel();
		MembersModel membersModel73=new MembersModel();
		MembersModel membersModel84=new MembersModel();
		membersModel1.setName("倪三");
		membersModel1.setIndex("");
		membersModel2.setName("李四");
		membersModel2.setIndex("");
		membersModel3.setName("王五");
		membersModel3.setIndex("");
		membersModel4.setName("单六");
		membersModel4.setIndex("");
		membersModel5.setName("重三");
		membersModel5.setIndex("");
		membersModel6.setName("李四");
		membersModel6.setIndex("");
		membersModel7.setName(" 王五");
		membersModel7.setIndex("");
		membersModel8.setName("赵六");
		membersModel8.setIndex("");
		membersModel9.setName("aaaa");
		membersModel9.setIndex("");
		membersModel82.setName("@ddd");
		membersModel82.setIndex("");
		membersModel73.setName("$$赵六");
		membersModel73.setIndex("");
		SourceDateList2.add(membersModel1);
		SourceDateList2.add(membersModel2);
		SourceDateList2.add(membersModel3);
		SourceDateList2.add(membersModel4);
		SourceDateList2.add(membersModel5);
		SourceDateList2.add(membersModel6);
		SourceDateList2.add(membersModel7);
		SourceDateList2.add(membersModel8);
		SourceDateList2.add(membersModel9);
		SourceDateList2.add(membersModel82);
		SourceDateList2.add(membersModel73);
		for (int i = 0; i < SourceDateList2.size(); i++) {
			MembersModel membersModel=SourceDateList2.get(i);
			String pinyin;
			try {
				pinyin = Chang.getFirstSpell(membersModel.getName().toString());
				String sortString = pinyin.substring(0, 1)
						.toUpperCase();
				System.out.println(pinyin+"---------------->"+sortString);
				// 正则表达式，判断首字母是否是英文字母
				if (sortString.matches("[A-Z]")) {
					membersModel.setIndex(sortString
							.toUpperCase());
				}else{
					membersModel.setIndex("#");
				}

				SourceDateList.add(membersModel);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		adapter = new SortAdapter(getActivity());
		Collections.sort(SourceDateList, pinyinComparator);
		adapter.updateListView(SourceDateList);
		
		sortListView.setAdapter(adapter);
		search_input.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String value = search_input.getText().toString();
				ArrayList<MembersModel> current_grouplist;
				if (SourceDateList != null) {
					if (value == null || value.length() == 0) {
						adapter.updateListView(SourceDateList);
					} else {
						current_grouplist = getSearchGroupList(SourceDateList,
								value);
						adapter.updateListView(current_grouplist);
					}
				} else {
					Toast.makeText(getActivity(), "加载中，请稍候",
							Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				input_del_img
						.setVisibility(s.toString().trim().length() > 0 ? View.VISIBLE
								: View.GONE);
//				notice.setVisibility(s.toString().trim().length() > 0 ? View.GONE
//						: View.VISIBLE);
				input_search_img
						.setVisibility(s.toString().trim().length() > 0 ? View.GONE
								: View.VISIBLE);
				search_input.setCursorVisible(s.toString().length() > 0 ? true
						: false);

			}
		});
		input_del_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				search_input.setText("");
				return;
			}
		});
		
		
	}
	private ArrayList<MembersModel> getSearchGroupList(
			ArrayList<MembersModel> grouplist, String value) {
		ArrayList<MembersModel> current_grouplist = new ArrayList<MembersModel>();
		for (int i = 0; i < grouplist.size(); i++) {
			MembersModel sortModel = (MembersModel) grouplist.get(i);
			if (sortModel.getName().contains(value)) {
				current_grouplist.add(sortModel);
				continue;
			}

		}
		return current_grouplist;
	}
}
