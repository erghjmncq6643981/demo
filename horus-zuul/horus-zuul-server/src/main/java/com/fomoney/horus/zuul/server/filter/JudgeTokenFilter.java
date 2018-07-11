/*
 * JudgeTokenFilter.java
 * 2018年1月12日 下午5:38:57
 * Copyright 2017 Fosun Financial. All  Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * Please contact Fosun Corporation or visit         
 * www.fosun.com 
 * if you need additional information or have any questions.
 * @author kyle
 * @version 1.0
 */

package com.fomoney.horus.zuul.server.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fomoney.horus.core.commons.constant.FilterTypeLabel;
import com.fomoney.horus.core.commons.constant.ResponseErrorCode;
import com.fomoney.horus.core.commons.constant.ResponseErrorCode.ErrorCodeEnum;
import com.fomoney.horus.core.commons.entity.ServerIdenInfo;
import com.fomoney.jjwt.util.JwtUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * token鉴权
 * 
 * @version
 * @author kyle 2018年1月12日下午5:38:57
 * @since 1.8
 */
@Component
public class JudgeTokenFilter extends ZuulFilter {
	private static Logger logger = LoggerFactory.getLogger(JudgeTokenFilter.class);
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		return ctx.containsKey("authenticate") && ctx.getBoolean("tokenLabel");
	}

	@Override
	public Object run() {
		RequestContext context = RequestContext.getCurrentContext();
		final ServerIdenInfo identity = (ServerIdenInfo) context.get("identity");
		parseToken(context, identity.getAppId());
		clearAuthenticateInformation(context);
		return null;
	}

	@Override
	public String filterType() {
		return FilterTypeLabel.ROUTEBEFORE;
	}

	@Override
	public int filterOrder() {
		return 4;
	}

	private void parseToken(RequestContext context, String key) {
		HttpServletRequest request = context.getRequest();
		String authToken = parseToken(request);
		String status = ErrorCodeEnum.TOKENERROR.getCode();
		if (null != authToken) {
			if (jwtUtil.parseJWT(authToken, key) != null) {
				logger.info("有效token：" + authToken);
			} else {
				ResponseErrorCode.setContext(context, status, ErrorCodeEnum.TOKENERROR.getTypeName(status));
				logger.info("无效token：" + authToken);
			}
		} else {
			ResponseErrorCode.setContext(context, status, ErrorCodeEnum.TOKENERROR.getTypeName(status));
			logger.info("token is empty");
		}
	}

	private String parseToken(HttpServletRequest request) {
		String authToken = null;
		final String method = request.getMethod();// 请求的类型，post get ..
		String contentType = request.getHeader("Content-Type");
		boolean multipart = true;
		if (null != contentType && "".equals(contentType)) {
			multipart = (contentType.indexOf("multipart/form-data") == -1);
		}
		if ("POST".equals(method) && multipart) {
			authToken = parseRequestBody(request);// 请求的body中token的值
		} else {
			authToken = request.getHeader("AuthToken");
		}
		return authToken;
	}

	private String parseRequestBody(HttpServletRequest request) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] contentData = null;
		String token = null;
		try {
			IOUtils.copy(request.getInputStream(), baos);
			contentData = baos.toByteArray();
			if (null != contentData && contentData.length != 0) {
				String charEncoding = request.getCharacterEncoding();
				if (charEncoding == null) {
					charEncoding = "UTF-8";
				}
				JSONObject jsonObject = new JSONObject(new String(contentData, charEncoding));
				token = jsonObject.getString("AuthToken");
			} else {
				token = request.getHeader("AuthToken");
			}
			return token;
		} catch (final IOException e) {
			return token;
		} catch (JSONException e) {
			return token;
		}
	}

	private void clearAuthenticateInformation(RequestContext context) {
		context.remove("authenticate");
		context.remove("ipLabel");
		context.remove("tokenLabel");
	}
}
