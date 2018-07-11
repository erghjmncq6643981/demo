/*
 * WhiteHostFilter.java
 * 2018年1月12日 下午5:27:52
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fomoney.horus.core.commons.constant.FilterTypeLabel;
import com.fomoney.horus.core.commons.constant.ResponseErrorCode;
import com.fomoney.horus.core.commons.constant.ResponseErrorCode.ErrorCodeEnum;
import com.fomoney.horus.core.commons.entity.ServerIdenInfo;
import com.fomoney.horus.core.commons.util.ZuulWhiteSecurityManager;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * 白名单鉴权
 * 
 * @version
 * @author kyle 2018年1月12日下午5:27:52
 * @since 1.8
 */
@Component
public class WhiteHostFilter extends ZuulFilter {
	private static Logger logger = LoggerFactory.getLogger(WhiteHostFilter.class);
	@Autowired
	private ZuulWhiteSecurityManager zuulWhiteSecurityManager;

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		return ctx.containsKey("authenticate") && ctx.getBoolean("ipLabel");
	}

	@Override
	public Object run() {
		RequestContext context = RequestContext.getCurrentContext();
		final ServerIdenInfo identity = (ServerIdenInfo) context.get("identity");
		String remoteAddr = context.getRequest().getRemoteAddr();
		String status = ErrorCodeEnum.WHITEERROR.getCode();
		if (zuulWhiteSecurityManager.getHostCahce(remoteAddr + identity.getServiceId()) == null) {
			ResponseErrorCode.setContext(context, status, ErrorCodeEnum.WHITEERROR.getTypeName(status));
			logger.info("用户ip:" + remoteAddr + "不在白名单中");
			return null;
		}
		logger.info("白名单用户,ip：" + remoteAddr);
		return null;
	}

	@Override
	public String filterType() {
		return FilterTypeLabel.ROUTEBEFORE;
	}

	@Override
	public int filterOrder() {
		return 3;
	}

}
