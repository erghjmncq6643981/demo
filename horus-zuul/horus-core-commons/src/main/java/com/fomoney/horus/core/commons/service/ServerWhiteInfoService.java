/*
 * ServerWhiteInfoService.java
 * 2017年9月28日 下午3:21:01
 * Copyright 2017 Fosun Financial. All  Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * Please contact Fosun Corporation or visit         
 * www.fosun.com 
 * if you need additional information or have any questions.
 * @author kyle
 * @version 1.0
 */

package com.fomoney.horus.core.commons.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fomoney.horus.core.commons.dao.ServerWhiteInfoDao;
import com.fomoney.horus.core.commons.entity.ServerWhiteInfo;

/**
 * 白名单服务类
 * @version   
 * @author kyle 2017年9月28日下午3:21:01
 * @since 1.8
 */
@Service
public class ServerWhiteInfoService {
	@Autowired
	private ServerWhiteInfoDao serverWhiteInfoDao;
	/**
	 * @return List<ServerWhiteInfo>
	 * @Description: 
	 * @create date 2017年8月11日下午4:19:45
	 */
	public List<ServerWhiteInfo> getServerWhiteInfoList(){
		return serverWhiteInfoDao.getServerWhiteInfoList();
	}
}

