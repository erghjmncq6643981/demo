/*
 * CustomRouteLocator.java
 * 2017年7月13日 下午2:57:17
 * Copyright 2017 Fosun Financial. All  Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * Please contact Fosun Corporation or visit
 * www.fosun.com
 * if you need additional information or have any questions.
 * @author kyle
 * @version 1.0
 */

package com.fomoney.horus.zuul.server.route;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;

import com.fomoney.horus.core.commons.util.ZuulGatewayManager;

/**
 * 实现动态从缓存中的自定义路由规则加载到zuul相关类中
 *
 * @version
 * @author kyle 2017年7月13日下午2:57:17
 * @since 1.8
 */
public class CustomRouteLocator extends DiscoveryClientRouteLocator {
	public final static Logger logger = LoggerFactory.getLogger(CustomRouteLocator.class);
	private ZuulGatewayManager zuulGatewayManager;
	private final ZuulProperties properties;
	private DiscoveryClient discovery;

	/**
	 * @param zuulGatewayManager
	 * @Description: 设置路由策略服务
	 * @create date 2017年9月14日上午9:48:44
	 */
	public void setZuulGatewayManager(ZuulGatewayManager zuulGatewayManager) {
		this.zuulGatewayManager = zuulGatewayManager;
	}

	/**
	 * 构造函数
	 *
	 * @param servletPath
	 * @param discovery
	 * @param properties
	 */
	public CustomRouteLocator(String servletPath, DiscoveryClient discovery, ZuulProperties properties) {
		super(servletPath, discovery, properties);
		this.properties = properties;
		logger.info("servletPath:{}", servletPath);
	}

	@Override
	protected LinkedHashMap<String, ZuulRoute> locateRoutes() {
		final LinkedHashMap<String, ZuulRoute> routesMap = new LinkedHashMap<String, ZuulRoute>();
		routesMap.putAll(zuulGatewayManager.getZuulRouteCache());
		for (ZuulRoute route : this.properties.getRoutes().values()) {
			routesMap.put(route.getPath(), route);
		}
		for (final Entry<String, ZuulRoute> entry : routesMap.entrySet()) {
			String path = entry.getKey();
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			routesMap.put(path, entry.getValue());
		}
		return routesMap;
	}

}
