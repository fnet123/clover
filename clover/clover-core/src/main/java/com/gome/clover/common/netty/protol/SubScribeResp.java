package com.gome.clover.common.netty.protol;

import java.io.Serializable;

/**
 *
 * 功能描述：应答对象 
 * @author linyoufa
 *	2014-12-23
 */
public class SubScribeResp implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String subReqId;
	private String respCode;
	private String desc;

	public SubScribeResp(String subRespId,String userName,String address){
		this.subReqId=subRespId;
		this.respCode="0";
		this.desc="Netty book order succesd  name:"+userName +" address"+address;
	}

	public String getSubReqId() {
		return subReqId;
	}

	public void setSubReqId(String subReqId) {
		this.subReqId = subReqId;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "SubScribeResp [desc=" + desc + ", respCode=" + respCode
				+ ", subReqId=" + subReqId + "]";
	}

}
