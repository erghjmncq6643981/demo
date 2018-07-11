/*
 * ServerWhiteInfo.java
 * 2017年9月28日 下午2:31:49
 * Copyright 2017 Fosun Financial. All  Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * Please contact Fosun Corporation or visit         
 * www.fosun.com 
 * if you need additional information or have any questions.
 * @author kyle
 * @version 1.0
 */

package com.fomoney.horus.core.commons.entity;

/**
 * 白名单信息对象
 * 
 * @version
 * @author kyle 2017年9月28日下午2:31:49
 * @since 1.8
 */
public class ServerWhiteInfo {
	private long whiteId;
	private String whiteHost;
	private String serviceId;
	private String addLabel;
	private long updateLabel;
	private String status;
	private String mayFlag;

	public long getWhiteId() {
		return whiteId;
	}

	public void setWhiteId(long whiteId) {
		this.whiteId = whiteId;
	}

	public String getWhiteHost() {
		return whiteHost;
	}

	public void setWhiteHost(String whiteHost) {
		this.whiteHost = whiteHost;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getAddLabel() {
		return addLabel;
	}

	public void setAddLabel(String addLabel) {
		this.addLabel = addLabel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getUpdateLabel() {
		return updateLabel;
	}

	public void setUpdateLabel(long updateLabel) {
		this.updateLabel = updateLabel;
	}

	public String getMayFlag() {
		return mayFlag;
	}

	public void setMayFlag(String mayFlag) {
		this.mayFlag = mayFlag;
	}

	/**
	 * @param serverWhiteInfo
	 * @return Boolean
	 * @Description:
	 * @create date 2018年5月29日下午2:45:03
	 */
	public Boolean equals(ServerWhiteInfo serverWhiteInfo) {
		if (null == serverWhiteInfo || null == this) {
			return false;
		}
		if (whiteId != serverWhiteInfo.getWhiteId()) {
			return false;
		}
		if (null == status || !status.equals(serverWhiteInfo.getStatus())) {
			return false;
		}
		if (null == mayFlag || !mayFlag.equals(serverWhiteInfo.getMayFlag())) {
			return false;
		}
		if (null == whiteHost || !whiteHost.equals(serverWhiteInfo.getWhiteHost())) {
			return false;
		}
		if (!addLabel.equals(serverWhiteInfo.getAddLabel())) {
			return false;
		}
		return true;
	}

}
