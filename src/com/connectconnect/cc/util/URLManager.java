package com.connectconnect.cc.util;

public class URLManager {
	public static final String URL = "http://cc.qiuhubang.com/";
	//邮箱获取验证码接口
	public static final String CAPTCHA=URL+"user/captcha.json";
	//提交注册接口
	public static final String REGISTER=URL+"user/register.json";
	//登陆接口
	public static final String LOGIN=URL+"user/login.json";
	//登出接口
	public static final String LOGOUT=URL+"user/logout.json";
	//关联状态接口
	public static final String OPENLOGIN=URL+"user/openlogin.json";
	//补全信息接口
	public static final String OPENREGISTER=URL+"user/openregister.json";
	//补充个人信息接口
	public static final String PERSIONALINFO=URL+"user/persionalinfo.json";
	//设置获取信息接口
	public static final String MYINFO=URL+"setinfo/myinfo.json";
	//设置修改信息接口
	public static final String SETMYINFO=URL+"setinfo/setmyinfo.json";
	//设置所有消息开关接口
	public static final String ANS=URL+"setinfo/allnoticeswitch.json";
	//设置群组消息开关接口
	public static final String GNS=URL+"setinfo/groupnoticeswitch.json";
	//设置陌生人发起临时聊天开关接口
	public static final String IMS=URL+"setinfo/interimchatswitch.json";
	//设置版本升级接口
	public static final String CHECKVERSION=URL+"setinfo/checkversion.json";
	//设置关于cc
	public static final String ABOUTS=URL+"setinfo/aboutus.html";
	//设置服务条款
	public static final String TSS=URL+"setinfo/termsofservice.html";
	
	
}
