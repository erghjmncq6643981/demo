/*
 * ServerIdenInfo.java
 * 2017年12月28日 下午4:31:04
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
 * 类功能描述
 * 
 * @version
 * @author kyle 2017年12月28日下午4:31:04
 * @since 1.8
 */
public class ServerIdenInfo {
	private Long idenId;
	private String projectName;
	private String serviceId;
	private String appId;
	private boolean judgeSecurity;
	private boolean ipLabel;
	private boolean tokenLabel;

	public Long getIdenId() {
		return idenId;
	}

	public void setIdenId(Long idenId) {
		this.idenId = idenId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public boolean isJudgeSecurity() {
		return judgeSecurity;
	}

	public void setJudgeSecurity(boolean judgeSecurity) {
		this.judgeSecurity = judgeSecurity;
	}

	public boolean isIpLabel() {
		return ipLabel;
	}

	public void setIpLabel(boolean ipLabel) {
		this.ipLabel = ipLabel;
	}

	public boolean isTokenLabel() {
		return tokenLabel;
	}

	public void setTokenLabel(boolean tokenLabel) {
		this.tokenLabel = tokenLabel;
	}

	/**
	 * @param serverIdenInfo
	 * @return boolean
	 * @Description:
	 * @create date 2018年5月18日下午4:57:18
	 */
	public Boolean equals(ServerIdenInfo serverIdenInfo) {
		if (null == serverIdenInfo || null == this) {
			return false;
		}
		int num1 = 0;
		int num2 = 0;
		if (null == idenId || !idenId.equals(serverIdenInfo.getIdenId())) {
			return false;
		}
		if (null == serviceId || !serviceId.equals(serverIdenInfo.getServiceId())) {
			return false;
		}
		if (null == appId || !appId.equals(serverIdenInfo.getAppId())) {
			return false;
		}
		if (judgeSecurity) {
			num1 += 1;
		}
		if (serverIdenInfo.isJudgeSecurity()) {
			num2 += 1;
		}
		if (num1 != num2) {
			return false;
		}
		if (ipLabel) {
			num1 += 1;
		}
		if (serverIdenInfo.isIpLabel()) {
			num2 += 1;
		}
		if (num1 != num2) {
			return false;
		}
		if (tokenLabel) {
			num1 += 1;
		}
		if (serverIdenInfo.isTokenLabel()) {
			num2 += 1;
		}
		if (num1 != num2) {
			return false;
		}
		return true;
	}
}
