package com.niit.org.beans;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Record implements Comparable<Record>{
	private int id;
	private int user_id;
	private Timestamp time;
	private int record_type;
	private String comment;
	private int record_user;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public int getRecord_type() {
		return record_type;
	}
	public void setRecord_type(int record_type) {
		this.record_type = record_type;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getRecord_user() {
		return record_user;
	}
	public void setRecord_user(int record_user) {
		this.record_user = record_user;
	}
	@Override
	public int compareTo(Record o) {
		// TODO Auto-generated method stub
		return this.getTime().compareTo(o.getTime());
	}
	
	
}
