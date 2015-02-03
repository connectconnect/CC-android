package com.connectconnect.cc.view;

import java.util.Comparator;

import com.connectconnect.cc.model.MembersModel;


/**
 * 
 * @author Mr.Z
 */
public class PinyinComparator implements Comparator<MembersModel> {

	public int compare(MembersModel o1, MembersModel o2) {
		if(o1.getIndex().equals("@") || o2.getIndex().equals("#")) {
			return -1;
		} else if(o1.getIndex().equals("#") || o2.getIndex().equals("@")) {
			return 1;
		} else {
			return o1.getIndex().compareTo(o2.getIndex());
		}
	}

}
