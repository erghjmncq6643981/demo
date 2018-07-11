/*
 * ServerIdenManager.java
 * 2017年12月29日 下午2:27:41
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

import com.fomoney.horus.core.commons.entity.ServerIdenInfo;
import com.fomoney.horus.core.commons.service.ServerIdenInfoService;

/**
 * 网关路由之微服务身份信息管理类
 * 
 * @version
 * @author kyle 2017年12月29日下午2:27:41
 * @since 1.8
 */
public class ServerIdenManager {
	private Logger logger = LoggerFactory.getLogger(ServerIdenManager.class);
	private List<ServerIdenInfo> serverIdentityWatchDog = new ArrayList<>();
	private volatile Map<String, ServerIdenInfo> serverIdentityCache = new ConcurrentHashMap<>();
	private ServerIdenInfoService serverIdenInfoService;
	private XMLUtil xmlUtil;
	@Value("${zuulUtil.serverIdentity.xmlFileName:ServerIdentityList}")
	private String xmlFileName;

	/**
	 * 
	 */
	public ServerIdenManager() {
	}

	/**
	 * @param serverIdenInfoService
	 * @param xmlUtil
	 */
	public ServerIdenManager(ServerIdenInfoService serverIdenInfoService, XMLUtil xmlUtil) {
		this.serverIdenInfoService = serverIdenInfoService;
		this.xmlUtil = xmlUtil;
	}

	/**
	 * @param xmlFileName
	 * @Description:
	 * @create date 2018年4月11日上午10:50:14
	 */
	public void setXmlFileName(String xmlFileName) {
		this.xmlFileName = xmlFileName;
	}

	/**
	 * @param delay
	 * @param period
	 * @Description:
	 * @create date 2017年12月29日下午2:41:26
	 */
	public void runServerIdenListTimeThread(long delay, long period) {
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
		service.scheduleWithFixedDelay(new Runnable() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void run() {
				try {
					List<ServerIdenInfo> serverIdenInfo = serverIdenInfoService.getServerIdenInfoList();
					if (!serverIdenInfo.isEmpty()) {
						boolean updateLabel = false;
						if (serverIdentityWatchDog.isEmpty()
								|| serverIdenInfo.size() != serverIdentityWatchDog.size()) {
							updateLabel = true;
						} else {
							for (int i = 0; i < serverIdenInfo.size(); i++) {
								if (!serverIdentityWatchDog.get(i).equals(serverIdenInfo.get(i))) {
									updateLabel = true;
								}
							}
						}
						if (updateLabel) {
							wirteDataToXml(serverIdenInfo);
							serverIdentityWatchDog = serverIdenInfo;
						}
					}
				} catch (Exception e) {
					serverIdentityWatchDog.clear();
					logger.error("微服务身份信息的管理类,数据加载定时线程产生异常;{}", e.getCause().toString());
					e.printStackTrace();
				}
			}
		}, delay, period, TimeUnit.SECONDS);
	}

	/**
	 * @param delay
	 * @param period
	 * @Description:
	 * @create date 2017年12月29日下午4:37:00
	 */
	public void runLoadServerIdenTimeThread(long delay, long period) {
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
		service.scheduleWithFixedDelay(new Runnable() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void run() {
				try {
					Map<String, ServerIdenInfo> serverIdentities = readData();
					boolean updateLabel = false;
					if (serverIdentities != null) {
						if (serverIdentityCache.isEmpty() || serverIdentities.size() != serverIdentityCache.size()) {
							updateLabel = true;
						} else {
							Iterator<Map.Entry<String, ServerIdenInfo>> identityIterator = serverIdentities.entrySet()
									.iterator();
							while (identityIterator.hasNext()) {
								Map.Entry<String, ServerIdenInfo> routEntry = identityIterator.next();
								ServerIdenInfo identity = routEntry.getValue();
								String key = routEntry.getKey();
								ServerIdenInfo idenInfoCache = serverIdentityCache.get(key);
								if (!identity.equals(idenInfoCache)) {
									updateLabel = true;
									break;
								}
							}
						}
						if (updateLabel) {
							updateIdentityCacheCache(serverIdentities);
						}
					}
				} catch (Exception e) {
					serverIdentityCache.clear();
					logger.error("微服务身份信息的管理类,xml文件读取定时线程产生异常;{}", e.getMessage());
					e.printStackTrace();
				}
			}
		}, delay, period, TimeUnit.SECONDS);
	}

	/**
	 * @param serverIdenInfo
	 * @return boolean
	 * @Description:
	 * @create date 2017年12月29日下午3:32:23
	 */
	public boolean wirteDataToXml(List<ServerIdenInfo> serverIdenInfo) {
		Document document = xmlUtil.readXmlDocument(xmlFileName);
		if (document == null) {
			xmlUtil.createXMLFile(xmlFileName);
			document = xmlUtil.readXmlDocument(xmlFileName);
		}
		Element root = document.getRootElement();
		Element server = root.element("server");
		server = xmlUtil.judgeElementExsitAttr(root, server, "server", "name", "微服务身份信息");
		Element identity = server.element("identity");
		server.remove(identity);
		identity = DocumentHelper.createElement("identity");
		for (int i = 0; i < serverIdenInfo.size(); i++) {
			Element microService = DocumentHelper.createElement("microService");
			ServerIdenInfo serverIden = serverIdenInfo.get(i);
			microService.addAttribute("id", serverIden.getIdenId() + "");
			xmlUtil.addsubElement(microService, "projectName", serverIden.getProjectName());
			xmlUtil.addsubElement(microService, "serviceId", serverIden.getServiceId());
			xmlUtil.addsubElement(microService, "appId", serverIden.getAppId());
			xmlUtil.addsubElement(microService, "judgeSecurity", serverIden.isJudgeSecurity() + "");
			if (serverIden.isJudgeSecurity()) {
				xmlUtil.addsubElement(microService, "ipLabel", serverIden.isIpLabel() + "");
				xmlUtil.addsubElement(microService, "tokenLabel", serverIden.isTokenLabel() + "");
			} else {
				xmlUtil.addsubElement(microService, "ipLabel", "false");
				xmlUtil.addsubElement(microService, "tokenLabel", "false");
			}
			logger.info("工程名：" + serverIden.getProjectName() + "；应用名：" + serverIden.getServiceId());
			identity.add(microService);
		}
		server.add(identity);
		return xmlUtil.writeDataToXml(document, xmlFileName);
	}

	/**
	 * @return serverIdentities
	 * @Description:
	 * @create date 2017年12月29日下午4:36:47
	 */
	public Map<String, ServerIdenInfo> readData() {
		Map<String, ServerIdenInfo> serverIdentities = new LinkedHashMap<String, ServerIdenInfo>();
		Document document = xmlUtil.readXmlDocument(xmlFileName);
		if (document == null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		List<Element> listNodes = document.selectNodes("/rootConfig/server/identity/microService");
		Iterator<Element> it = listNodes.iterator();
		while (it.hasNext()) {
			Element element = it.next();
			Long idenId = Long.parseLong(element.attributeValue("id"));
			String judgeSecurity = element.element("judgeSecurity").getText();
			String ipLabel = element.element("ipLabel").getText();
			String tokenLabel = element.element("tokenLabel").getText();
			String serviceId = element.element("serviceId").getText();
			ServerIdenInfo serverIdenInfo = new ServerIdenInfo();
			serverIdenInfo.setIdenId(idenId);
			serverIdenInfo.setProjectName(element.element("projectName").getText());
			serverIdenInfo.setServiceId(serviceId);
			serverIdenInfo.setAppId(element.element("appId").getText());
			serverIdenInfo.setIpLabel(false);
			serverIdenInfo.setTokenLabel(false);
			if ("true".equals(judgeSecurity)) {
				serverIdenInfo.setJudgeSecurity(true);
				if ("true".equals(ipLabel)) {
					serverIdenInfo.setIpLabel(true);
				}
				if ("true".equals(tokenLabel)) {
					serverIdenInfo.setTokenLabel(true);
				}
			} else {
				serverIdenInfo.setJudgeSecurity(false);
			}
			serverIdentities.put(serviceId, serverIdenInfo);
		}
		return serverIdentities;
	}

	private void updateIdentityCacheCache(Map<String, ServerIdenInfo> serverIdentities) {
		synchronized (serverIdentityCache) {
			serverIdentityCache.clear();
			serverIdentityCache.putAll(serverIdentities);
		}
	}

	/**
	 * @param key
	 * @return serverIdentityCache
	 * @Description:
	 * @create date 2017年12月29日下午4:36:29
	 */
	public ServerIdenInfo getServerIdentityCache(String key) {
		return serverIdentityCache.get(key);
	}

	/**
	 * 
	 * @Description:
	 * @create date 2018年5月30日上午9:08:15
	 */
	public void refresh() {
		serverIdentityWatchDog.clear();
		serverIdentityCache.clear();
	}
}
