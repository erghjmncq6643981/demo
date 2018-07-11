/*
 * LoggerFilter.java
 * 2017年8月9日 下午4:09:38
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
import org.springframework.stereotype.Component;

import com.fomoney.horus.core.commons.constant.FilterTypeLabel;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * 类功能描述:后置拦截器，处理微服务响应
 *
 * @version
 * @author kyle 2017年8月9日下午4:09:38
 * @since 1.8
 */
@Component
public class LoggerFilter extends ZuulFilter {
	private static Logger logger = LoggerFactory.getLogger(LoggerFilter.class);

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		final RequestContext context = RequestContext.getCurrentContext();
		final long statrtTime = (long) context.get("startTime");// 请求的开始时间
		final String requestURI = (String) context.get("requestInfo");
		final int statusCode = context.getResponseStatusCode();// 请求的状态
		final long duration = System.currentTimeMillis() - statrtTime;// 请求耗时
		logger.info("statusCode:" + statusCode);
		logger.info("requestURI:" + requestURI);
		logger.info("duration:" + duration + "ms");
		return null;
	}

	@Override
	public String filterType() {
		return FilterTypeLabel.ROUTEAFTER;
	}

	@Override
	public int filterOrder() {
		return 999;
	}

}
