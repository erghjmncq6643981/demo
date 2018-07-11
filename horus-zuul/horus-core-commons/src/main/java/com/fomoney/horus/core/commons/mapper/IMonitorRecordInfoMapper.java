/*
 * IMonitorRecordInfoMapper.java
 * 2017年8月11日 下午4:20:27
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

import org.springframework.stereotype.Repository;

import com.fomoney.horus.core.commons.entity.MonitorRecordInfo;




/**
 * 类功能描述
 *
 * @version
 * @author kyle 2017年8月11日下午4:20:27
 * @since 1.8
 */
@Repository
public interface IMonitorRecordInfoMapper {
	/**
	 * @param monitorRecordInfo
	 * @return 主键
	 * @Description: TODO
	 * @create date 2017年8月11日下午4:43:55
	 */
	public int insertMonitorRecordInfo(MonitorRecordInfo monitorRecordInfo);
}
