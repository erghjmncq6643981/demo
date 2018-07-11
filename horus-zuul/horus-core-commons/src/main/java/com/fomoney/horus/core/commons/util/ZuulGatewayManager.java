/*
 * ZuulGatewayManager.java
 * 2017年10月24日 下午12:19:32
 * Copyright 2017 Fosun Financial. All  Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * Please contact Fosun Corporation or visit         
 * www.fosun.com 
 * if you need additional information or have any questions.
 * @author kyle
 * @version 1.0
 */

package com.fomoney.horus.core.commons.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;

import com.fomoney.horus.core.commons.entity.ZuulRouteVO;
import com.fomoney.horus.core.commons.service.ZuulRouteVOService;

/**
 * 网关路由之动态路由管理类 从数据库读取路由规则
 * 
 * @version
 * @author kyle 2017年10月24日下午12:19:32
 * @since 1.8
 */
public class ZuulGatewayManager {
	private Logger logger = LoggerFactory.getLogger(ZuulGatewayManager.class);
	private List<ZuulRouteVO> zuulRoutesWatchDog = new ArrayList<>();
	private volatile Map<String, ZuulRoute> zuulRouteCache = new ConcurrentHashMap<String, ZuulRoute>();
	private ZuulRouteVOService zuulRouteService;
	private XMLUtil xmlUtil;
	@Value("${zuulUtil.gateway.xmlFileName:ZuulGatewayList}")
	private String xmlFileName;

	/**
	 * 
	 */
	public ZuulGatewayManager() {
	}

	/**
	 * @param zuulRouteService
	 * @param xmlUtil
	 */
	public ZuulGatewayManager(ZuulRouteVOService zuulRouteService, XMLUtil xmlUtil) {
		this.zuulRouteService = zuulRouteService;
		this.xmlUtil = xmlUtil;
	}

	/**
	 * @param xmlFileName
	 * @Description:
	 * @create date 2018年4月11日上午10:51:09
	 */
	public void setXmlFileName(String xmlFileName) {
		this.xmlFileName = xmlFileName;
	}

	/**
	 * @param delay
	 * @param period
	 * @Description: TODO
	 * @create date 2017年10月24日下午2:40:44
	 */
	public void runWriteZuulRouteTimeThread(long delay, long period) {
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
		service.scheduleWithFixedDelay(new Runnable() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void run() {
				try {
					final List<ZuulRouteVO> zuulRouteVOList = zuulRouteService.findAllZuulRoute();
					if (!zuulRouteVOList.isEmpty()) {
						boolean updateLabel = false;
						if (zuulRoutesWatchDog.isEmpty() || zuulRouteVOList.size() != zuulRoutesWatchDog.size()) {
							updateLabel = true;
						} else {
							for (int i = 0; i < zuulRouteVOList.size(); i++) {
								if (!zuulRoutesWatchDog.get(i).equals(zuulRouteVOList.get(i))) {
									updateLabel = true;
								}
							}
						}
						if (updateLabel) {
							wirteDataToXml(zuulRouteVOList);
							zuulRoutesWatchDog = zuulRouteVOList;
						}
					}
				} catch (Exception e) {
					zuulRoutesWatchDog.clear();
					logger.info("网关路由中动态路由的管理类,数据加载定时线程产生异常");
					e.printStackTrace();
				}
			}
		}, delay, period, TimeUnit.SECONDS);
	}

	/**
	 * @param delay
	 * @param period
	 * @Description: TODO
	 * @create date 2017年10月24日下午2:40:39
	 */
	public void runLoadZuulRouteTimeThread(long delay, long period) {
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
		service.scheduleWithFixedDelay(new Runnable() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void run() {
				try {
					Map<String, ZuulRoute> ZuulRoutes = readData();
					boolean updateLabel = false;
					if (ZuulRoutes != null) {
						if (zuulRouteCache.isEmpty() || ZuulRoutes.size() != zuulRouteCache.size()) {
							updateLabel = true;
						} else {
							Iterator<Map.Entry<String, ZuulRoute>> routsIterator = ZuulRoutes.entrySet().iterator();
							while (routsIterator.hasNext()) {
								Map.Entry<String, ZuulRoute> routEntry = routsIterator.next();
								ZuulRoute route = routEntry.getValue();
								String key = routEntry.getKey();
								if (!route.equals(zuulRouteCache.get(key))) {
									updateLabel = true;
									break;
								}
							}
						}
						if (updateLabel) {
							updateZuulRouteCache(ZuulRoutes);
						}
					}
				} catch (Exception e) {
					zuulRoutesWatchDog.clear();
					zuulRouteCache.clear();
					logger.info("网关路由中动态路由的管理类,xml文件读取定时线程产生异常;{}", e.getMessage());
					e.printStackTrace();
				}
			}
		}, delay, period, TimeUnit.SECONDS);
	}

	private boolean wirteDataToXml(List<ZuulRouteVO> routes) {
		Document document = xmlUtil.readXmlDocument(xmlFileName);
		if (document == null) {
			xmlUtil.createXMLFile(xmlFileName);
			document = xmlUtil.readXmlDocument(xmlFileName);
		}
		Element root = document.getRootElement();
		Element zuulElement = root.element("zuul");
		zuulElement = xmlUtil.judgeElementExsitAttr(root, zuulElement, "zuul", "name", "动态路由信息");
		Element listElement = zuulElement.element("routes");
		zuulElement.remove(listElement);
		listElement = DocumentHelper.createElement("routes");
		for (int i = 0; i < routes.size(); i++) {
			ZuulRouteVO gateway = routes.get(i);
			Element gatewayElement = DocumentHelper.createElement("gateway");
			gatewayElement.addAttribute("keyOnly", gateway.getKeyOnly() + "");
			xmlUtil.addsubElement(gatewayElement, "id", gateway.getId());
			xmlUtil.addsubElement(gatewayElement, "path", gateway.getPath());
			String serviceId = gateway.getServiceId();
			if (null == serviceId || "".equals(serviceId)) {
				xmlUtil.addsubElement(gatewayElement, "url", gateway.getUrl());
			} else {
				xmlUtil.addsubElement(gatewayElement, "serviceId", gateway.getServiceId());
			}
			xmlUtil.addsubElement(gatewayElement, "retryable", gateway.getRetryable() + "");
			xmlUtil.addsubElement(gatewayElement, "stripPrefix", gateway.isStripPrefix() + "");
			Element customSensitiveHeaders = DocumentHelper.createElement("customSensitiveHeaders");
			customSensitiveHeaders.addAttribute("value", gateway.isCustomSensitiveHeaders() + "");
			Iterator<String> iterator = gateway.getSensitiveHeaders().iterator();
			while (iterator.hasNext()) {
				String sensitiveHeader = iterator.next();
				xmlUtil.addsubElement(customSensitiveHeaders, "sensitiveHeader", sensitiveHeader);
			}
			logger.info("应用名称：" + gateway.getServiceId() + "；路由规则：" + gateway.getPath());
			gatewayElement.add(customSensitiveHeaders);
			listElement.add(gatewayElement);
		}
		zuulElement.add(listElement);
		return xmlUtil.writeDataToXml(document, xmlFileName);
	}

	private Map<String, ZuulRoute> readData() {
		Map<String, ZuulRoute> zuulRoutes = new LinkedHashMap<String, ZuulRoute>();
		Document document = xmlUtil.readXmlDocument(xmlFileName);
		if (document == null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		List<Element> listNodes = document.selectNodes("/rootConfig/zuul/routes/gateway");
		Iterator<Element> it = listNodes.iterator();
		while (it.hasNext()) {
			Element element = it.next();
			ZuulRoute geteway = new ZuulRoute();
			geteway.setId(element.element("id").getText());
			geteway.setPath(element.element("path").getText());
			try {
				geteway.setServiceId(element.element("serviceId").getText());
			} catch (Exception e) {
				geteway.setUrl(element.element("url").getText());
			}
			if ("true".equals(element.element("stripPrefix").getText())) {
				geteway.setStripPrefix(true);
			} else {
				geteway.setStripPrefix(false);
			}
			if ("true".equals(element.element("retryable").getText())) {
				geteway.setRetryable(true);
			} else {
				geteway.setRetryable(false);
			}
			if ("true".equals(element.element("customSensitiveHeaders").getText())) {
				geteway.setCustomSensitiveHeaders(true);
				@SuppressWarnings("unchecked")
				List<Element> sensitiveHeaders = element.elements("sensitiveHeader");
				Set<String> headers = new LinkedHashSet<>(sensitiveHeaders.size());
				for (int i = 0; i < sensitiveHeaders.size(); i++) {
					headers.add(sensitiveHeaders.get(i).getText());
				}
				geteway.setSensitiveHeaders(headers);
			} else {
				geteway.setCustomSensitiveHeaders(false);
			}
			zuulRoutes.put(element.element("path").getText(), geteway);
		}
		return zuulRoutes;
	}

	private void updateZuulRouteCache(Map<String, ZuulRoute> ZuulRoutes) {
		synchronized (zuulRouteCache) {
			zuulRouteCache.clear();
			zuulRouteCache.putAll(ZuulRoutes);
		}
	}

	/**
	 * @param key
	 * @return routesCache
	 * @Description:
	 * @create date 2017年9月14日上午9:02:28
	 */
	public ZuulRoute getRoutesCache(String key) {
		return zuulRouteCache.get(key);
	}

	/**
	 * @return zuulRouteCache
	 * @Description:
	 * @create date 2018年5月30日下午1:25:26
	 */
	public Map<String, ZuulRoute> getZuulRouteCache() {
		return zuulRouteCache;
	}

}
