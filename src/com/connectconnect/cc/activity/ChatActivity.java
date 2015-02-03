/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.connectconnect.cc.activity;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.connectconnect.cc.R;
import com.connectconnect.cc.adapter.ChatAdapter;
import com.connectconnect.cc.adapter.ExpressionAdapter;
import com.connectconnect.cc.adapter.ExpressionPagerAdapter;
import com.connectconnect.cc.classes.MessageJson;
import com.connectconnect.cc.service.PushServiceUserPwd;
import com.connectconnect.cc.view.AlertDialog;
import com.connectconnect.cc.view.ExpandGridView;
import com.connectconnect.cc.view.MediaPlayerutil;
import com.connectconnect.cc.view.PasteEditText;
import com.connectconnect.cc.view.SmileUtils;

/**
 * 聊天页面
 * 
 */
public class ChatActivity extends BaseActivity implements OnClickListener {

	private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
	public static final int REQUEST_CODE_CONTEXT_MENU = 3;
	private static final int REQUEST_CODE_MAP = 4;
	public static final int REQUEST_CODE_TEXT = 5;
	public static final int REQUEST_CODE_VOICE = 6;
	public static final int REQUEST_CODE_PICTURE = 7;
	public static final int REQUEST_CODE_LOCATION = 8;
	public static final int REQUEST_CODE_NET_DISK = 9;
	public static final int REQUEST_CODE_FILE = 10;
	public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
	public static final int REQUEST_CODE_PICK_VIDEO = 12;
	public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
	public static final int REQUEST_CODE_VIDEO = 14;
	public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
	public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
	public static final int REQUEST_CODE_SEND_USER_CARD = 17;
	public static final int REQUEST_CODE_CAMERA = 18;
	public static final int REQUEST_CODE_LOCAL = 19;
	public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
	public static final int REQUEST_CODE_GROUP_DETAIL = 21;
	public static final int REQUEST_CODE_SELECT_VIDEO = 23;
	public static final int REQUEST_CODE_SELECT_FILE = 24;
	public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;

	public static final int RESULT_CODE_COPY = 1;
	public static final int RESULT_CODE_DELETE = 2;
	public static final int RESULT_CODE_FORWARD = 3;
	public static final int RESULT_CODE_OPEN = 4;
	public static final int RESULT_CODE_DWONLOAD = 5;
	public static final int RESULT_CODE_TO_CLOUD = 6;
	public static final int RESULT_CODE_EXIT_GROUP = 7;

	public static final int CHATTYPE_SINGLE = 1;
	public static final int CHATTYPE_GROUP = 2;

	public static final String COPY_IMAGE = "EASEMOBIMG";
	private View recordingContainer;
	private ImageView micImage;
	private TextView recordingHint;
	private ListView listView;
	private InputMethodManager manager;
	private PasteEditText mEditTextContent;
	private View buttonSetModeKeyboard;
	private View buttonSetModeVoice;
	private View buttonSend;
	private View buttonPressToSpeak;
	// private ViewPager expressionViewpager;
	private LinearLayout emojiIconContainer;
	private LinearLayout btnContainer;
	private ImageView locationImgview;
	private View more;
	private int position;
	private ViewPager expressionViewpager;
	private List<String> reslist;
	public static ChatActivity activityInstance = null;
	// 给谁发送消息
	private String toChatUsername;
	private File cameraFile;
	static int resendPos;
	private ClipboardManager clipboard;

	private ImageView iv_emoticons_normal;
	private ImageView iv_emoticons_checked;
	private RelativeLayout edittext_layout;
	private ProgressBar loadmorePB;
	private Button btnMore;
	public String playMsgId;
	private TextView tNameTextView;
	private ChatAdapter chatAdapter;
	private ArrayList<MessageJson> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		initView();
	}

	/**
	 * initView
	 */
	public void initView() {
		Intent Intent = new Intent(ChatActivity.this, PushServiceUserPwd.class);
		Log.d("homeActivity", "homeActivity onCreate");
		startService(Intent);
		list=new ArrayList<MessageJson>();
		chatAdapter=new ChatAdapter(getApplicationContext());
		clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		rcChat_popup = this.findViewById(R.id.rcChat_popup);
		volume = (ImageView) this.findViewById(R.id.volume);
		mSensor = new com.connectconnect.cc.view.SoundMeter();
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		recordingContainer = findViewById(R.id.recording_container);
		micImage = (ImageView) findViewById(R.id.mic_image);
		recordingHint = (TextView) findViewById(R.id.recording_hint);
		listView = (ListView) findViewById(R.id.list);
		mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);
		buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
		edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
		buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
		buttonSend = findViewById(R.id.btn_send);
		buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
		expressionViewpager = (ViewPager) findViewById(R.id.vPager);
		emojiIconContainer = (LinearLayout) findViewById(R.id.ll_face_container);
		btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
		locationImgview = (ImageView) findViewById(R.id.btn_location);
		iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
		iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
		loadmorePB = (ProgressBar) findViewById(R.id.pb_load_more);
		btnMore = (Button) findViewById(R.id.btn_more);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		iv_emoticons_checked.setOnClickListener(this);
		iv_emoticons_normal.setOnClickListener(this);
		more = findViewById(R.id.more);
		edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
		tNameTextView = (TextView) findViewById(R.id.name);

		// 表情list
		reslist = getExpressionRes(35);
		// 初始化表情viewpager
		List<View> views = new ArrayList<View>();
		View gv1 = getGridChildView(1);
		View gv2 = getGridChildView(2);
		views.add(gv1);
		views.add(gv2);
		expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
		edittext_layout.requestFocus();
		// buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
		mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					edittext_layout
							.setBackgroundResource(R.drawable.input_bar_bg_active);
				} else {
					edittext_layout
							.setBackgroundResource(R.drawable.input_bar_bg_normal);
				}

			}
		});
		mEditTextContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edittext_layout
						.setBackgroundResource(R.drawable.input_bar_bg_active);
				more.setVisibility(View.GONE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
				emojiIconContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.GONE);
			}
		});
		// 监听文字框
		mEditTextContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!TextUtils.isEmpty(s)) {
					btnMore.setVisibility(View.GONE);
					buttonSend.setVisibility(View.VISIBLE);
				} else {
					btnMore.setVisibility(View.VISIBLE);
					buttonSend.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		buttonPressToSpeak.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				// 获取在当前窗口内的绝对坐标
				if (flag == 3) {
					flag = 1;
				}
				if (flag == 1) {
					if (!ExistSDCard()) {
						Toast.makeText(ChatActivity.this, "No SDCard",
								Toast.LENGTH_LONG).show();
						return false;
					}
					// rcChat_popup.setVisibility(View.VISIBLE);
					mHandler.postDelayed(new Runnable() {
						public void run() {
						}
					}, 300);
					mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
					mFileName += "/"+System.currentTimeMillis()+".3gp";
					System.out.println("mfile---------------------->"+mFileName);
					start(mFileName);
					flag = 2;

				}

				return false;
			}

		});
		buttonPressToSpeak.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (flag == 1) {
						if (!ExistSDCard()) {
							Toast.makeText(ChatActivity.this, "No SDCard",
									Toast.LENGTH_LONG).show();
							return false;
						}
						rcChat_popup.setVisibility(View.VISIBLE);
						flag = 3;
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if (flag == 2) {
						rcChat_popup.setVisibility(View.GONE);
						stop();
						flag = 1;
						MediaPlayer mp = MediaPlayer.create(ChatActivity.this,
								Uri.parse(mFileName));
						final int duration = mp.getDuration() / 1000;// ��Ϊʱ��
																		// ��ms
						if (duration < 1) {
							Toast.makeText(getApplicationContext(), "时间太短",
									Toast.LENGTH_SHORT).show();
						} else {
							MessageJson messageJson=new MessageJson();
							messageJson.setType("1");
							messageJson.setMessage(mFileName);
							list.add(messageJson);
							chatAdapter.setDataList(list);
							listView.setAdapter(chatAdapter);
							tNameTextView.setText(duration + "S");
							tNameTextView
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											// TODO Auto-generated method stub;
											tNameTextView.setEnabled(false);
											MediaPlayerutil mediaPlayerutil = new MediaPlayerutil(
													tNameTextView, duration,
													ChatActivity.this);
											mediaPlayerutil.playUrl(mFileName);
										}
									});

						}

					} else {
						rcChat_popup.setVisibility(View.GONE);
						flag = 1;

					}

				}
				return false;
			}
		});

	}

	/**
	 * 消息图标点击事件
	 * 
	 * @param view
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_send:
			String s = mEditTextContent.getText().toString();
			Toast.makeText(getApplicationContext(), s, 1).show();
			Spannable span = SmileUtils.getSmiledText(getApplicationContext(),
					s);
			// 设置内容
			tNameTextView.setText(span, BufferType.SPANNABLE);
			MessageJson messageJson=new MessageJson();
			messageJson.setType("0");
			messageJson.setMessage(s);
			list.add(messageJson);
			chatAdapter.setDataList(list);
			listView.setAdapter(chatAdapter);
			break;
		case R.id.btn_picture:
			selectPicFromLocal();
			break;
		case R.id.iv_emoticons_normal:
			more.setVisibility(View.VISIBLE);
			iv_emoticons_normal.setVisibility(View.INVISIBLE);
			iv_emoticons_checked.setVisibility(View.VISIBLE);
			btnContainer.setVisibility(View.GONE);
			emojiIconContainer.setVisibility(View.VISIBLE);
			hideKeyboard();
			break;
		case R.id.iv_emoticons_checked:
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
			btnContainer.setVisibility(View.VISIBLE);
			emojiIconContainer.setVisibility(View.GONE);
			more.setVisibility(View.GONE);
			break;
		case R.id.btn_file:
			selectFileFromLocal();
			break;
		default:
			break;
		}
		// int id = view.getId();
		// if (id == R.id.btn_send) {// 点击发送按钮(发文字和表情)
		// String s = mEditTextContent.getText().toString();
		// Toast.makeText(getApplicationContext(), s, 1).show();
		// Spannable span = SmileUtils.getSmiledText(getApplicationContext(),
		// s);
		// // 设置内容
		// tNameTextView.setText(span, BufferType.SPANNABLE);
		// } else if (id == R.id.btn_take_picture) {
		// } else if (id == R.id.btn_picture) {
		// selectPicFromLocal(); // 点击图片图标
		// } else if (id == R.id.btn_location) { // 位置
		// } else if (id == R.id.iv_emoticons_normal) { // 点击显示表情框
		// more.setVisibility(View.VISIBLE);
		// iv_emoticons_normal.setVisibility(View.INVISIBLE);
		// iv_emoticons_checked.setVisibility(View.VISIBLE);
		// btnContainer.setVisibility(View.GONE);
		// emojiIconContainer.setVisibility(View.VISIBLE);
		// hideKeyboard();
		// } else if (id == R.id.iv_emoticons_checked) { // 点击隐藏表情框
		// iv_emoticons_normal.setVisibility(View.VISIBLE);
		// iv_emoticons_checked.setVisibility(View.INVISIBLE);
		// btnContainer.setVisibility(View.VISIBLE);
		// emojiIconContainer.setVisibility(View.GONE);
		// more.setVisibility(View.GONE);
		//
		// } else if (id == R.id.btn_video) {
		// // 点击摄像图标
		// } else if (id == R.id.btn_file) { // 点击文件图标
		// selectFileFromLocal();
		// } else if (id == R.id.btn_voice_call) { // 点击语音电话图标
		// }
	}

	/**
	 * 选择文件
	 */
	private void selectFileFromLocal() {
		Intent intent = null;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("*/*");
			intent.addCategory(Intent.CATEGORY_OPENABLE);

		} else {
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
	}

	/**
	 * 从图库获取图片
	 */
	public void selectPicFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_LOCAL);
	}

	/**
	 * 根据图库图片uri发送图片
	 * 
	 * @param selectedImage
	 */
	private void sendPicByUri(Uri selectedImage) {
		// String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(selectedImage, null, null,
				null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex("_data");
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			cursor = null;

			if (picturePath == null || picturePath.equals("null")) {
				Toast toast = Toast.makeText(this, "找不到图片", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
		} else {
			File file = new File(selectedImage.getPath());
			if (!file.exists()) {
				Toast toast = Toast.makeText(this, "找不到图片", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;

			}
		}

	}

	/**
	 * 发送文件
	 * 
	 * @param uri
	 */
	private void sendFile(Uri uri) {
		String filePath = null;
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = getContentResolver().query(uri, projection, null,
						null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					filePath = cursor.getString(column_index);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			filePath = uri.getPath();
		}
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			Toast.makeText(getApplicationContext(), "文件不存在", 0).show();
			return;
		}
		if (file.length() > 10 * 1024 * 1024) {
			Toast.makeText(getApplicationContext(), "文件不能大于10M", 0).show();
			return;
		}

	}

	/**
	 * 显示语音图标按钮
	 * 
	 * @param view
	 */
	public void setModeVoice(View view) {
		hideKeyboard();
		edittext_layout.setVisibility(View.GONE);
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeKeyboard.setVisibility(View.VISIBLE);
		buttonSend.setVisibility(View.GONE);
		btnMore.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.VISIBLE);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		btnContainer.setVisibility(View.VISIBLE);
		emojiIconContainer.setVisibility(View.GONE);

	}

	/**
	 * 显示键盘图标
	 * 
	 * @param view
	 */
	public void setModeKeyboard(View view) {
		// mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener()
		// {
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		// if(hasFocus){
		// getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		// }
		// }
		// });
		edittext_layout.setVisibility(View.VISIBLE);
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeVoice.setVisibility(View.VISIBLE);
		// mEditTextContent.setVisibility(View.VISIBLE);
		mEditTextContent.requestFocus();
		// buttonSend.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.GONE);
		if (TextUtils.isEmpty(mEditTextContent.getText())) {
			btnMore.setVisibility(View.VISIBLE);
			buttonSend.setVisibility(View.GONE);
		} else {
			btnMore.setVisibility(View.GONE);
			buttonSend.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 点击清空聊天记录
	 * 
	 * @param view
	 */
	public void emptyHistory(View view) {
		startActivityForResult(
				new Intent(this, AlertDialog.class)
						.putExtra("titleIsCancel", true)
						.putExtra("msg", "是否清空所有聊天记录").putExtra("cancel", true),
				REQUEST_CODE_EMPTY_HISTORY);
	}

	/**
	 * 显示或隐藏图标按钮页
	 * 
	 * @param view
	 */
	public void more(View view) {
		if (more.getVisibility() == View.GONE) {
			System.out.println("more gone");
			hideKeyboard();
			more.setVisibility(View.VISIBLE);
			btnContainer.setVisibility(View.VISIBLE);
			emojiIconContainer.setVisibility(View.GONE);
		} else {
			if (emojiIconContainer.getVisibility() == View.VISIBLE) {
				emojiIconContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.VISIBLE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
			} else {
				more.setVisibility(View.GONE);
			}

		}

	}

	/**
	 * 点击文字输入框
	 * 
	 * @param v
	 */
	public void editClick(View v) {
		listView.setSelection(listView.getCount() - 1);
		if (more.getVisibility() == View.VISIBLE) {
			more.setVisibility(View.GONE);
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
		}

	}

	/**
	 * 按住说话listener
	 * 
	 */
	// class PressToSpeakListen implements View.OnTouchListener {
	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// switch (event.getAction()) {
	// case MotionEvent.ACTION_DOWN:
	// if (!CommonUtils.isExitsSdcard()) {
	// Toast.makeText(ChatActivity.this, "发送语音需要sdcard支持！",
	// Toast.LENGTH_SHORT).show();
	// return false;
	// }
	// try {
	// v.setPressed(true);
	// wakeLock.acquire();
	// if (VoicePlayClickListener.isPlaying)
	// VoicePlayClickListener.currentPlayListener.stopPlayVoice();
	// recordingContainer.setVisibility(View.VISIBLE);
	// recordingHint.setText(getString(R.string.move_up_to_cancel));
	// recordingHint.setBackgroundColor(Color.TRANSPARENT);
	// voiceRecorder.startRecording(null, toChatUsername,
	// getApplicationContext());
	// } catch (Exception e) {
	// e.printStackTrace();
	// v.setPressed(false);
	// if (wakeLock.isHeld())
	// wakeLock.release();
	// if (voiceRecorder != null)
	// voiceRecorder.discardRecording();
	// recordingContainer.setVisibility(View.INVISIBLE);
	// Toast.makeText(ChatActivity.this, R.string.recoding_fail,
	// Toast.LENGTH_SHORT).show();
	// return false;
	// }
	//
	// return true;
	// case MotionEvent.ACTION_MOVE: {
	// if (event.getY() < 0) {
	// recordingHint.setText(getString(R.string.release_to_cancel));
	// recordingHint.setBackgroundResource(R.drawable.recording_text_hint_bg);
	// } else {
	// recordingHint.setText(getString(R.string.move_up_to_cancel));
	// recordingHint.setBackgroundColor(Color.TRANSPARENT);
	// }
	// return true;
	// }
	// case MotionEvent.ACTION_UP:
	// v.setPressed(false);
	// recordingContainer.setVisibility(View.INVISIBLE);
	// if (wakeLock.isHeld())
	// wakeLock.release();
	// if (event.getY() < 0) {
	// // discard the recorded audio.
	// voiceRecorder.discardRecording();
	//
	// } else {
	// // stop recording and send voice file
	// try {
	// int length = voiceRecorder.stopRecoding();
	// if (length > 0) {
	// sendVoice(voiceRecorder.getVoiceFilePath(),
	// voiceRecorder.getVoiceFileName(toChatUsername),
	// Integer.toString(length), false);
	// } else if (length == EMError.INVALID_FILE) {
	// Toast.makeText(getApplicationContext(), "无录音权限",
	// Toast.LENGTH_SHORT).show();
	// } else {
	// Toast.makeText(getApplicationContext(), "录音时间太短",
	// Toast.LENGTH_SHORT).show();
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// Toast.makeText(ChatActivity.this, "发送失败，请检测服务器是否连接",
	// Toast.LENGTH_SHORT).show();
	// }
	//
	// }
	// return true;
	// default:
	// recordingContainer.setVisibility(View.INVISIBLE);
	// if (voiceRecorder != null)
	// voiceRecorder.discardRecording();
	// return false;
	// }
	// }
	// }

	/**
	 * 获取表情的gridview的子view
	 * 
	 * @param i
	 * @return
	 */
	private View getGridChildView(int i) {
		View view = View.inflate(this, R.layout.expression_gridview, null);
		ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
		List<String> list = new ArrayList<String>();
		if (i == 1) {
			List<String> list1 = reslist.subList(0, 20);
			list.addAll(list1);
		} else if (i == 2) {
			list.addAll(reslist.subList(20, reslist.size()));
		}
		list.add("delete_expression");
		final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this,
				1, list);
		gv.setAdapter(expressionAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String filename = expressionAdapter.getItem(position);
				try {
					// 文字输入框可见时，才可输入表情
					// 按住说话可见，不让输入表情
					if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

						if (filename != "delete_expression") { // 不是删除键，显示表情
							// 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
							Class clz = Class
									.forName("com.connectconnect.cc.view.SmileUtils");
							Field field = clz.getField(filename);
							mEditTextContent.append(SmileUtils.getSmiledText(
									ChatActivity.this, (String) field.get(null)));
						} else { // 删除文字或者表情
							if (!TextUtils.isEmpty(mEditTextContent.getText())) {

								int selectionStart = mEditTextContent
										.getSelectionStart();// 获取光标的位置
								if (selectionStart > 0) {
									String body = mEditTextContent.getText()
											.toString();
									String tempStr = body.substring(0,
											selectionStart);
									int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
									if (i != -1) {
										CharSequence cs = tempStr.substring(i,
												selectionStart);
										if (SmileUtils.containsKey(cs
												.toString()))
											mEditTextContent.getEditableText()
													.delete(i, selectionStart);
										else
											mEditTextContent.getEditableText()
													.delete(selectionStart - 1,
															selectionStart);
									} else {
										mEditTextContent.getEditableText()
												.delete(selectionStart - 1,
														selectionStart);
									}
								}
							}

						}
					}
				} catch (Exception e) {
				}

			}
		});
		return view;
	}

	public List<String> getExpressionRes(int getSum) {
		List<String> reslist = new ArrayList<String>();
		for (int x = 1; x <= getSum; x++) {
			String filename = "ee_" + x;

			reslist.add(filename);

		}
		return reslist;

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 隐藏软键盘
	 */
	private void hideKeyboard() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back(View view) {
		finish();
	}

	/**
	 * 覆盖手机返回键
	 */
	@Override
	public void onBackPressed() {
		if (more.getVisibility() == View.VISIBLE) {
			more.setVisibility(View.GONE);
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// 点击notification bar进入聊天页面，保证只有一个聊天页面
		String username = intent.getStringExtra("userId");
		if (toChatUsername.equals(username))
			super.onNewIntent(intent);
		else {
			finish();
			startActivity(intent);
		}

	}

	private int flag = 1;
	private View rcChat_popup;
	private Handler mHandler = new Handler();
	private com.connectconnect.cc.view.SoundMeter mSensor;
	private static final int POLL_INTERVAL = 300;
	private ImageView volume;
	public static boolean isUsed = false;// 是否使用自定义声音文件
	// 保存使用状态的文件
	private static String mFileName = null;

	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = mSensor.getAmplitude();
			updateDisplay(amp);
			mHandler.postDelayed(mPollTask, POLL_INTERVAL);
		}
	};

	private void start(String name) {
		mSensor.start(name);
		mHandler.postDelayed(mPollTask, POLL_INTERVAL);
	}

	private void stop() {
		mHandler.removeCallbacks(mPollTask);
		mSensor.stop();
		volume.setImageResource(R.drawable.amp1);
	}

	private void updateDisplay(double signalEMA) {
		switch ((int) signalEMA) {

		case 0:
		case 1:
			volume.setImageResource(R.drawable.amp1);
			break;
		case 2:
		case 3:
			volume.setImageResource(R.drawable.amp2);
			break;
		case 4:
		case 5:
			volume.setImageResource(R.drawable.amp3);
			break;
		case 6:
		case 7:
			volume.setImageResource(R.drawable.amp4);
			break;
		case 8:
		case 9:
			volume.setImageResource(R.drawable.amp5);
			break;
		case 10:
		case 11:
			volume.setImageResource(R.drawable.amp6);
			break;
		default:
			volume.setImageResource(R.drawable.amp7);
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		mSensor.pause();

	}

	/**
	 * 判断sdk是否存在
	 * 
	 * @return
	 */
	private boolean ExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(

		android.os.Environment.MEDIA_MOUNTED)) {

			return true;

		} else

			return false;

	}

	public ChatActivity() {
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
	
	}

}
