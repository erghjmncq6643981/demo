/*
 * ZuulRouteVOService.java
 * 2017年7月16日 下午3:13:36
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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fomoney.horus.core.commons.dao.SensitiveHeadersInfoDao;
import com.fomoney.horus.core.commons.dao.ZuulRouteVODao;
import com.fomoney.horus.core.commons.entity.ZuulRouteVO;




/**
 * 用于转换成ZuulRoute,实现数据库加载路由策略
 *
 * @version
 * @author kyle 2017年7月16日下午3:13:36
 * @since 1.8
 */
@Service
public class ZuulRouteVOService {
	@Autowired
	private ZuulRouteVODao zuulRouteDao;
	@Autowired
	private SensitiveHeadersInfoDao sensitiveHeadersInfoDao;

	/**
	 * @return List<ZuulRouteVO>
	 * @Description: 
	 * @create date 2017年10月20日下午3:59:40
	 */
	public List<ZuulRouteVO> findAllZuulRoute() {
		List<ZuulRouteVO> zuulRouteVOList=zuulRouteDao.findAllZuulRoute();
		for (int i = 0; i < zuulRouteVOList.size(); i++) {
			ZuulRouteVO zuul=zuulRouteVOList.get(i);
			if(zuul.isCustomSensitiveHeaders()){
				List<String> sensitiveHeaders=sensitiveHeadersInfoDao.getSensitiveInfoList(zuul.getKeyOnly());
				Set<String> sensitive=new LinkedHashSet<>(sensitiveHeaders.size());
				for (int j = 0; j < sensitiveHeaders.size(); j++) {
					sensitive.add(sensitiveHeaders.get(j));
				}
				zuul.setSensitiveHeaders(sensitive);
			}
		}
		return zuulRouteVOList;
	}
}
