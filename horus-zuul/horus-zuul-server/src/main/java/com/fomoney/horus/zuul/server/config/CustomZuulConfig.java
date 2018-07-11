/*
 * CustomZuulConfig.java
 * 2017年7月13日 下午3:33:03
 * Copyright 2017 Fosun Financial. All  Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * Please contact Fosun Corporation or visit
 * www.fosun.com
 * if you need additional information or have any questions.
 * @author kyle
 * @version 1.0
 */

package com.fomoney.horus.zuul.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fomoney.horus.core.commons.service.ServerIdenInfoService;
import com.fomoney.horus.core.commons.service.ServerWhiteInfoService;
import com.fomoney.horus.core.commons.service.ZuulRouteVOService;
import com.fomoney.horus.core.commons.util.ServerIdenManager;
import com.fomoney.horus.core.commons.util.XMLUtil;
import com.fomoney.horus.core.commons.util.ZuulGatewayManager;
import com.fomoney.horus.core.commons.util.ZuulWhiteSecurityManager;
import com.fomoney.horus.zuul.server.route.CustomRouteLocator;

/**
 * 自定义zuul配置类
 *
 * @version
 * @author kyle 2017年7月13日下午3:33:03
 * @since 1.8
 */
@Configuration
public class CustomZuulConfig {
	@Autowired
	private ZuulProperties zuulProperties;
	@Autowired
	private ServerProperties server;
	@Autowired
	private DiscoveryClient discovery;
	@Autowired
	private ServerIdenInfoService serverIdenInfoService;
	@Autowired
	private ZuulRouteVOService zuulRouteService;
	@Autowired
	private ServerWhiteInfoService serverWhiteInfoService;

	/**
	 * @return xmlUtil
	 * @Description:
	 * @create date 2018年4月11日下午1:18:19
	 */
	@Bean("xmlUtil")
	public XMLUtil getXMLUtil() {
		return new XMLUtil();
	}

	/**
	 * @param xmlUtil
	 * @return ZuulWhiteSecurityManager
	 * @Description: 白名单加载管理类，启动2个定时线程
	 * @create date 2017年10月20日下午3:48:13
	 */
	@Bean("zuulWhiteSecurityManager")
	public ZuulWhiteSecurityManager getZuulWhiteSecurityManager(XMLUtil xmlUtil) {
		ZuulWhiteSecurityManager zuulWhiteSecurityManager = new ZuulWhiteSecurityManager(serverWhiteInfoService,
				xmlUtil);
		zuulWhiteSecurityManager.runWhiteListTimeThread(1, 5);
		zuulWhiteSecurityManager.runReadXmlToCacheTimeThread(1, 10);
		return zuulWhiteSecurityManager;
	}

	/**
	 * @param xmlUtil
	 * @return ZuulGatewayManager
	 * @Description: 路由规则加载管理类
	 * @create date 2017年10月20日下午3:48:36
	 */
	@Bean("zuulGatewayManager")
	public ZuulGatewayManager getZuulGatewayManager(XMLUtil xmlUtil) {
		ZuulGatewayManager zuulGatewayManager = new ZuulGatewayManager(zuulRouteService, xmlUtil);
		zuulGatewayManager.runWriteZuulRouteTimeThread(1, 5);
		zuulGatewayManager.runLoadZuulRouteTimeThread(1, 10);
		return zuulGatewayManager;
	}

	/**
	 * @param xmlUtil
	 * @return serverIdenManager
	 * @Description:
	 * @create date 2017年12月29日下午5:54:50
	 */
	@Bean("serverIdenManager")
	public ServerIdenManager getServerIdenManager(XMLUtil xmlUtil) {
		ServerIdenManager serverIdenManager = new ServerIdenManager(serverIdenInfoService, xmlUtil);
		serverIdenManager.runServerIdenListTimeThread(1, 5);
		serverIdenManager.runLoadServerIdenTimeThread(1, 10);
		return serverIdenManager;
	}

	/**
	 * @param zuulGatewayManager
	 * @return CustomRouteLocator
	 * @Description: zuul定时刷新路由规则
	 * @create date 2017年10月20日下午3:47:35
	 */
	@Bean("routeLocator")
	public CustomRouteLocator routeLocator(ZuulGatewayManager zuulGatewayManager) {
		final CustomRouteLocator routeLocator = new CustomRouteLocator(this.server.getServletPrefix(), this.discovery,
				this.zuulProperties);
		routeLocator.setZuulGatewayManager(zuulGatewayManager);
		return routeLocator;
	}
}
