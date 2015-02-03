package com.connectconnect.cc.model;

public class MembersModel {
	private String name;
	private String index;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	@Override
	public String toString() {
		return "MembersModel [name=" + name + ", index=" + index + "]";
	}

}
