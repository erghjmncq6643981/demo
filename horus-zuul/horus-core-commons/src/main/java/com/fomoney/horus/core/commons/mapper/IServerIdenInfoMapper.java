/*
 * ServerIdenInfoMapper.java
 * 2017年12月28日 下午4:55:36
 * Copyright 2017 Fosun Financial. All  Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * Please contact Fosun Corporation or visit         
 * www.fosun.com 
 * if you need additional information or have any questions.
 * @author kyle
 * @version 1.0
 */

package com.fomoney.horus.core.commons.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.fomoney.horus.core.commons.entity.ServerIdenInfo;

/**
 * 类功能描述
 * @version   
 * @author kyle 2017年12月28日下午4:55:36
 * @since 1.8
 */
@Repository
public interface IServerIdenInfoMapper {
	/**
	 * @return List<ServerIdenInfo>
	 * @Description: 
	 * @create date 2017年12月28日下午4:57:03
	 */
	List<ServerIdenInfo> getServerIdenInfoList();
}

