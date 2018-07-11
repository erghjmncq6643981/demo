/*
 * TokenResponse.java
 * 2017年11月10日 下午5:00:20
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

import com.fomoney.chaos.framework.util.support.util.base.BaseResponse;

/**
 * 类功能描述
 * @version   
 * @author kyle 2017年11月10日下午5:00:20
 * @since 1.8
 */
public class TokenResponse extends BaseResponse{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -8114950164761625323L;
	
	private String jwtToken;
	public TokenResponse(){
	}
	public TokenResponse(String code, String message){
		super(code, message);
	}
	
	public TokenResponse(String code, String message,String jwtToken){
		super.setCode(code);
		super.setMessage(message);
		this.jwtToken = jwtToken;
	}
	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
	public String getJwtToken() {
		return jwtToken;
	}
	

}

