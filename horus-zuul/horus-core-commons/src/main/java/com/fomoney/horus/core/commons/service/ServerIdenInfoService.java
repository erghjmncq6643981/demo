/*
 * ServerIdenInfoService.java
 * 2017年12月28日 下午5:00:16
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

import com.fomoney.horus.core.commons.dao.ServerIdenInfoDao;
import com.fomoney.horus.core.commons.entity.ServerIdenInfo;

/**
 * 类功能描述
 * @version   
 * @author kyle 2017年12月28日下午5:00:16
 * @since 1.8
 */
@Service
public class ServerIdenInfoService {
	@Autowired
	private ServerIdenInfoDao serverIdenInfoDao;
	
	/**
	 * @return List<ServerIdenInfo>
	 * @Description: 
	 * @create date 2017年12月28日下午5:00:59
	 */
	public List<ServerIdenInfo> getServerIdenInfoList(){
		return serverIdenInfoDao.getServerIdenInfoList();
	}
}

