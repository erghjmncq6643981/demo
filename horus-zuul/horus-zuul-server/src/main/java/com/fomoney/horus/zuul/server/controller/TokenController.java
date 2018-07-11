/*
 * DemoController.java
 * 2017年7月13日 下午3:53:39
 * Copyright 2017 Fosun Financial. All  Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * Please contact Fosun Corporation or visit
 * www.fosun.com
 * if you need additional information or have any questions.
 * @author kyle
 * @version 1.0
 */

package com.fomoney.horus.zuul.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fomoney.chaos.framework.util.support.util.base.BaseResponse;
import com.fomoney.jjwt.entity.TokenResponse;
import com.fomoney.jjwt.util.JwtUtil;

/**
 * 获取token令牌的接口
 *
 * @version
 * @author kyle 2017年7月13日下午3:53:39
 * @since 1.8
 */
@RestController
public class TokenController {
	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * @param appId
	 * @return token
	 * @Description: 获得token
	 * @create date 2017年9月12日下午2:53:02
	 */
	@RequestMapping(value = "/getToken", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public BaseResponse getToken(@RequestBody String appId) {
		if("".equals(appId)){
			return null;
		}
		appId = appId.trim();
		if ("=".equals(appId.substring(appId.length() - 1, appId.length()))) {
			appId = appId.substring(0, appId.length() - 1);
		}
		if (appId.indexOf("\"") != -1) {
			appId = appId.replaceAll("\"", "");
		}
		java.lang.String[] result = jwtUtil.applyToken(appId);
		TokenResponse response = new TokenResponse(BaseResponse.SUCCESS, BaseResponse.SUCCESS_MESSAGE, result[1],
				result[0]);
		return response;
	}

}
