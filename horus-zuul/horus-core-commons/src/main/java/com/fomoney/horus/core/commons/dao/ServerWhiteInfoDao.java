/*
 * ServerWhiteInfoDao.java
 * 2017年9月28日 下午3:21:18
 * Copyright 2017 Fosun Financial. All  Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * Please contact Fosun Corporation or visit         
 * www.fosun.com 
 * if you need additional information or have any questions.
 * @author kyle
 * @version 1.0
 */

package com.fomoney.horus.core.commons.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fomoney.horus.core.commons.entity.ServerWhiteInfo;
import com.fomoney.horus.core.commons.mapper.IServerWhiteInfoMapper;

/**
 * 类功能描述
 * @version   
 * @author kyle 2017年9月28日下午3:21:18
 * @since 1.8
 */
@Repository
public class ServerWhiteInfoDao {
	@Autowired
	private IServerWhiteInfoMapper serverWhiteInfoMapper;
	/**
	 * @return List<ServerWhiteInfo>
	 * @Description: 
	 * @create date 2017年8月11日下午4:19:45
	 */
	public List<ServerWhiteInfo> getServerWhiteInfoList(){
		return serverWhiteInfoMapper.getServerWhiteInfoList();
	}
	
	/**
	 * @param customer
	 * @Description: 
	 * @create date 2017年9月28日下午3:19:37
	 */
	public void updateWhiteInfoAddLabel(ServerWhiteInfo customer){
		serverWhiteInfoMapper.updateWhiteInfoAddLabel(customer);
	}
}

