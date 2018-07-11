/*
 * AccessFilter.java
 * 2017年8月9日 下午4:07:53
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

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fomoney.horus.core.commons.constant.FilterTypeLabel;
import com.fomoney.horus.core.commons.constant.ResponseErrorCode;
import com.fomoney.horus.core.commons.constant.ResponseErrorCode.ErrorCodeEnum;
import com.fomoney.horus.core.commons.entity.ServerIdenInfo;
import com.fomoney.horus.core.commons.util.ServerIdenManager;
import com.fomoney.horus.core.commons.util.ZuulGatewayManager;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * 前置拦截器，判断访问的微服务是否存在与身份表
 *
 * @version
 * @author kyle 2017年8月9日下午4:07:53
 * @since 1.8
 */
@Component
public class AccessFilter extends ZuulFilter {
	private static Logger logger = LoggerFactory.getLogger(AccessFilter.class);
	@Autowired
	private ServerIdenManager serverIdenManager;
	@Autowired
	private ZuulGatewayManager zuulGatewayManager;

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletRequest request = context.getRequest();
		String remoteAddr = request.getRemoteAddr();// 请求发起方的host
		String requestURI = request.getRequestURI();// 请求的uri
		ServerIdenInfo identity = judgeIdentity(requestURI);
		String status = ErrorCodeEnum.SERVICEERROR.getCode();
		if (identity != null) {
			context.set("startTime", System.currentTimeMillis());
			context.set("requestInfo", requestURI);
			context.set("identity", identity);
			return null;
		}
		logger.info("用户ip:" + remoteAddr + "；请求的service不存在，请检查！");
		ResponseErrorCode.setContext(context, status, ErrorCodeEnum.SERVICEERROR.getTypeName(status));
		return null;
	}

	@Override
	public String filterType() {
		return FilterTypeLabel.ROUTEBEFORE;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	private ServerIdenInfo judgeIdentity(String requestURI) {
		String path = "/" + requestURI.split("/")[1] + "/**";
		String serviceId = zuulGatewayManager.getRoutesCache(path).getServiceId();
		ServerIdenInfo identity = serverIdenManager.getServerIdentityCache(serviceId);
		return identity;
	}

}
