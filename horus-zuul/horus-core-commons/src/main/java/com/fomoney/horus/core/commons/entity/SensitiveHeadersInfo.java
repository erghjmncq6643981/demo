/*
 * SensitiveHeadersInfo.java
 * 2017年12月28日 下午5:27:23
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
 * @version   
 * @author kyle 2017年12月28日下午5:27:23
 * @since 1.8
 */
public class SensitiveHeadersInfo {
	private long id;
	private String headName;
	private boolean validStatus;
	private long key;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getHeadName() {
		return headName;
	}
	public void setHeadName(String headName) {
		this.headName = headName;
	}
	public boolean isValidStatus() {
		return validStatus;
	}
	public void setValidStatus(boolean validStatus) {
		this.validStatus = validStatus;
	}
	public long getKey() {
		return key;
	}
	public void setKey(long key) {
		this.key = key;
	}
}

