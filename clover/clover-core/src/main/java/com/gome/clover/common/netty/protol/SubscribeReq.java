package com.gome.clover.common.netty.protol;

import java.io.Serializable;

/**
 *
 * 功能描述：请求对象
 * @author linyoufa
 *	2014-12-23
 */
public class SubscribeReq implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String subReqId;
	private String userName;
	private String porductName;
	private String phoneNumber;
	private String address;

	public SubscribeReq(String subReqId){
		this.subReqId=subReqId;
		this.phoneNumber="15210130485";
		this.porductName="Nett权威指南";
		this.userName="linyoufa";
		this.address="北京市朝阳区晓云路26号鹏润大厦21层";

	}

	@Override
	public String toString() {
		return "SubscribeReq [phoneNumber=" + phoneNumber + ", porductName="
				+ porductName + ", subReqId=" + subReqId + ", userName="
				+ userName + "]";
	}
	public String getSubReqId() {
		return subReqId;
	}
	public void setSubReqId(String subReqId) {
		this.subReqId = subReqId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPorductName() {
		return porductName;
	}
	public void setPorductName(String porductName) {
		this.porductName = porductName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}






}
