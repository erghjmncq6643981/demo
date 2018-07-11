/*
 * ZuulWhiteSecurityManager.java
 * 2017年9月7日 上午9:16:57
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
import java.util.List;
import java.util.Map;
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

import com.fomoney.horus.core.commons.entity.ServerWhiteInfo;
import com.fomoney.horus.core.commons.service.ServerWhiteInfoService;

/**
 * 网关路由之白名单管理类
 *
 * @version
 * @author kyle 2017年9月7日上午9:16:57
 * @since 1.8
 */
public class ZuulWhiteSecurityManager {
	private static Logger logger = LoggerFactory.getLogger(ZuulWhiteSecurityManager.class);
	private List<ServerWhiteInfo> whiteInfsWatchDog = new ArrayList<>();
	private volatile Map<String, String> whitesCache = new ConcurrentHashMap<String, String>();
	private ServerWhiteInfoService serverWhiteInfoService;
	private XMLUtil xmlUtil;
	@Value("${zuulUtil.white.xmlFileName:WhiteSecurityList}")
	private String xmlFileName;

	/**
	 * 
	 */
	public ZuulWhiteSecurityManager() {
	}

	/**
	 * @param serverWhiteInfoService
	 * @param lockService
	 * @param xmlUtil
	 */
	public ZuulWhiteSecurityManager(ServerWhiteInfoService serverWhiteInfoService, XMLUtil xmlUtil) {
		this.serverWhiteInfoService = serverWhiteInfoService;
		this.xmlUtil = xmlUtil;
	}

	/**
	 * @param xmlFileName
	 * @Description:
	 * @create date 2017年10月20日下午3:41:26
	 */
	public void setXmlFileName(String xmlFileName) {
		this.xmlFileName = xmlFileName;
	}

	/**
	 * @param delay
	 * @param period
	 * @Description: 启动定时线程，把数据库中的白名单加载到xml文件
	 * @create date 2017年9月7日上午9:28:35
	 */
	public void runWhiteListTimeThread(long delay, long period) {
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
		service.scheduleWithFixedDelay(new Runnable() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void run() {
				try {
					List<ServerWhiteInfo> serverWhiteInfoList = serverWhiteInfoService.getServerWhiteInfoList();
					if (!serverWhiteInfoList.isEmpty()) {
						boolean updateLabel = false;
						if (whiteInfsWatchDog.isEmpty() || serverWhiteInfoList.size() != whiteInfsWatchDog.size()) {
							updateLabel = true;
						} else {
							for (int i = 0; i < serverWhiteInfoList.size(); i++) {
								if (!whiteInfsWatchDog.get(i).equals(serverWhiteInfoList.get(i))) {
									updateLabel = true;
								}
							}
						}
						if (updateLabel) {
							wirteDataToXml(serverWhiteInfoList);
							whiteInfsWatchDog = serverWhiteInfoList;
						}
					}
				} catch (Exception e) {
					whiteInfsWatchDog.clear();
					logger.error("网关路由中白名单的管理类,数据加载定时线程产生异常;{}", e.getMessage());
					e.printStackTrace();
				}
			}
		}, delay, period, TimeUnit.SECONDS);
	}

	/**
	 * @param delay
	 * @param period
	 * @Description:
	 * @create date 2017年9月14日上午9:57:55
	 */
	public void runReadXmlToCacheTimeThread(long delay, long period) {
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
		service.scheduleWithFixedDelay(new Runnable() {
			@SuppressWarnings({ "synthetic-access" })
			@Override
			public void run() {
				try {
					Map<String, String> hostCache = readData();
					boolean updateLabel = false;
					if (hostCache != null) {
						if (whitesCache.isEmpty() || hostCache.size() != whitesCache.size()) {
							updateLabel = true;
						} else {
							Iterator<Map.Entry<String, String>> hostIterator = hostCache.entrySet().iterator();
							while (hostIterator.hasNext()) {
								Map.Entry<String, String> routEntry = hostIterator.next();
								String key = routEntry.getKey();
								String value = routEntry.getValue();
								if (!value.equals(whitesCache.get(key))) {
									updateLabel = true;
									break;
								}
							}
						}
						if (updateLabel) {
							whitesCache.clear();
							whitesCache.putAll(hostCache);
						}
					}
				} catch (Exception e) {
					logger.error("网关路由中白名单的管理类,xml文件读取定时线程产生异常;{}", e.getMessage());
					e.printStackTrace();
				}
			}
		}, delay, period, TimeUnit.SECONDS);
	}

	/**
	 * @return Map<String, String>
	 * @Description:
	 * @create date 2017年9月14日上午9:57:44
	 */
	public Map<String, String> readData() {
		Map<String, String> hostCache = new LinkedHashMap<String, String>();
		Document document = xmlUtil.readXmlDocument(xmlFileName);
		if (document == null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		List<Element> hostNode = document.selectNodes("/rootConfig/security/hosts/host");
		for (int i = 0; i < hostNode.size(); i++) {
			Element element = hostNode.get(i);
			hostCache.put(element.element("whiteHost").getText() + element.element("serviceId").getText(),
					element.attributeValue("id"));
		}
		return hostCache;
	}

	private boolean wirteDataToXml(List<ServerWhiteInfo> serverWhiteInfoList) {
		Document document = xmlUtil.readXmlDocument(xmlFileName);
		if (document == null) {
			xmlUtil.createXMLFile(xmlFileName);
			document = xmlUtil.readXmlDocument(xmlFileName);
		}
		Element root = document.getRootElement();
		Element securityElement = root.element("security");
		securityElement = xmlUtil.judgeElementExsitAttr(root, securityElement, "security", "name", "白名单信息");
		Element listElement = securityElement.element("hosts");
		securityElement.remove(listElement);
		listElement = DocumentHelper.createElement("hosts");
		for (int i = 0; i < serverWhiteInfoList.size(); i++) {
			ServerWhiteInfo serverWhiteInfo = serverWhiteInfoList.get(i);
			String whiteId = serverWhiteInfo.getWhiteId() + "";
			String whiteHost = serverWhiteInfo.getWhiteHost();
			String serviceId = serverWhiteInfo.getServiceId();
			Element host = DocumentHelper.createElement("host");
			host.addAttribute("id", whiteId);
			xmlUtil.addsubElement(host, "whiteHost", whiteHost);
			xmlUtil.addsubElement(host, "serviceId", serviceId);
			listElement.add(host);
			logger.info("应用：" + serviceId + "的白名单host：" + whiteHost);
		}
		securityElement.add(listElement);
		return xmlUtil.writeDataToXml(document, xmlFileName);
	}

	/**
	 * @param key
	 * @return cache
	 * @Description:
	 * @create date 2017年9月8日下午5:48:01
	 */
	public String getHostCahce(String key) {
		String value = whitesCache.get(key);
		logger.info("=======>" + value + "****");
		return value;
	}

	/**
	 * 
	 * @Description:
	 * @create date 2018年5月30日上午9:07:03
	 */
	public void refresh() {
		whiteInfsWatchDog.clear();
		whitesCache.clear();
	}

}
