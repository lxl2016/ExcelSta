package com.niit.org.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.niit.org.util.Dic;

public class User implements Comparable<User>{
	private int id;
	private String no;
	private String stuno;
	private String name;
	private String en_name;
	private int batch;
	private Map<String,List<Record>> records = new HashMap();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEn_name() {
		return en_name;
	}
	public void setEn_name(String en_name) {
		this.en_name = en_name;
	}
	public int getBatch() {
		return batch;
	}
	public void setBatch(int batch) {
		this.batch = batch;
	}
	public Map<String, List<Record>> getRecords() {
		return records;
	}
	public void setRecords(Map<String, List<Record>> records) {
		this.records = records;
	}
	
	public String getStuno() {
		return stuno;
	}
	public void setStuno(String stuno) {
		this.stuno = stuno;
	}
	@Override
	public int compareTo(User arg0) {
		// TODO Auto-generated method stub
		int i = Dic.batchesById.get(this.getBatch()).compareTo(Dic.batchesById.get(arg0.getBatch()));
		if(i == 0){
			int j = this.getStuno().compareTo(arg0.getStuno());
			return j;
		}
		return i;
	}
	
	
}
