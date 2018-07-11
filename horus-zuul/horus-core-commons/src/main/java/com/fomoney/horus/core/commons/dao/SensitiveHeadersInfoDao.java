/*
 * SensitiveHeadersInfoDao.java
 * 2017年12月28日 下午5:40:40
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

import com.fomoney.horus.core.commons.mapper.ISensitiveHeadersInfoMapper;


/**
 * 类功能描述
 * @version   
 * @author kyle 2017年12月28日下午5:40:40
 * @since 1.8
 */
@Repository
public class SensitiveHeadersInfoDao {
	@Autowired
	private ISensitiveHeadersInfoMapper sensitiveHeadersInfoMapper;
	/**
	 * @param key
	 * @return List<String>
	 * @Description: 
	 * @create date 2017年12月28日下午5:42:23
	 */
	public List<String> getSensitiveInfoList(long key){
		return sensitiveHeadersInfoMapper.getSensitiveInfoList(key);
	}
}

