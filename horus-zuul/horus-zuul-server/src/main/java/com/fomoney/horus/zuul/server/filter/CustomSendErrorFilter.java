/*
 * CustomSendErrorFilter.java
 * 2018年1月9日 下午3:32:21
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

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.fomoney.horus.core.commons.constant.FilterTypeLabel;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * 自定义异常处理类
 * 
 * @version
 * @author kyle 2018年1月9日下午3:32:21
 * @since 1.8
 */
@Component
public class CustomSendErrorFilter extends ZuulFilter {
	private static Logger logger = LoggerFactory.getLogger(CustomSendErrorFilter.class);
	/**
	 * Comment for <code>SEND_ERROR_FILTER_RAN</code>
	 */
	protected static final String SEND_ERROR_FILTER_RAN = "sendErrorFilter.ran";

	@Value("${error.path:/error}")
	private String errorPath;

	@Override
	public String filterType() {
		return FilterTypeLabel.ROUTEBEFORE;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		// only forward to errorPath if it hasn't been forwarded to already
		return ctx.containsKey("error.status_code") && !ctx.getBoolean(SEND_ERROR_FILTER_RAN, false);
	}

	@Override
	public Object run() {
		try {
			RequestContext ctx = RequestContext.getCurrentContext();
			HttpServletRequest request = ctx.getRequest();
			int statusCode = (Integer) ctx.get("error.status_code");
			request.setAttribute("javax.servlet.error.status_code", statusCode);
			if (ctx.containsKey("error.exception")) {
				Object e = ctx.get("error.exception");
				logger.warn("Error during filtering", Throwable.class.cast(e));
				if (e instanceof ZuulException) {
					ZuulException zuulException = (ZuulException) e;
					String message = zuulException.getCause().getMessage();
					request.setAttribute("javax.servlet.error.message", message);
				}
			}

			RequestDispatcher dispatcher = request.getRequestDispatcher(this.errorPath);
			if (dispatcher != null) {
				ctx.set(SEND_ERROR_FILTER_RAN, true);
				if (!ctx.getResponse().isCommitted()) {
					dispatcher.forward(request, ctx.getResponse());
				}
			}
		} catch (Exception ex) {
			ReflectionUtils.rethrowRuntimeException(ex);
		}
		return null;
	}

	public void setErrorPath(String errorPath) {
		this.errorPath = errorPath;
	}

}
