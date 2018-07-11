/*
 * JudgeSecurityFilter.java
 * 2018年1月12日 下午5:14:16
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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fomoney.horus.core.commons.constant.FilterTypeLabel;
import com.fomoney.horus.core.commons.entity.ServerIdenInfo;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * 安全控制逻辑判断
 * 
 * @version
 * @author kyle 2018年1月12日下午5:14:16
 * @since 1.8
 */
@Component
@ConfigurationProperties(prefix = "zuulUtil.monitor")
public class JudgeSecurityFilter extends ZuulFilter {
	private String zuulSecurityLabel = "false";

	/**
	 * @param zuulSecurityLabel
	 * @Description:
	 * @create date 2017年9月25日下午6:11:12
	 */
	public void setZuulSecurityLabel(String zuulSecurityLabel) {
		this.zuulSecurityLabel = zuulSecurityLabel;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext context = RequestContext.getCurrentContext();
		final ServerIdenInfo identity = (ServerIdenInfo) context.get("identity");
		if ("true".equals(zuulSecurityLabel)) {
			if (identity.isJudgeSecurity()) {
				context.set("authenticate");
				context.set("ipLabel", identity.isIpLabel());
				context.set("tokenLabel", identity.isTokenLabel());
			}
		}
		return null;
	}

	@Override
	public String filterType() {
		return FilterTypeLabel.ROUTEBEFORE;
	}

	@Override
	public int filterOrder() {
		return 2;
	}

}
