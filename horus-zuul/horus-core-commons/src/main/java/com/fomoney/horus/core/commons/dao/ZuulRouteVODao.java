/*
 * ZuulRouteVDao.java
 * 2017年7月16日 下午3:15:08
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

import com.fomoney.horus.core.commons.entity.ZuulRouteVO;
import com.fomoney.horus.core.commons.mapper.IZuulRouteMapper;




/**
 * 类功能描述
 *
 * @version
 * @author kyle 2017年7月16日下午3:15:08
 * @since 1.8
 */
@Repository
public class ZuulRouteVODao {
	@Autowired(required=true)
	private IZuulRouteMapper zuulRouteMapper;

	public List<ZuulRouteVO> findAllZuulRoute() {
		final List<ZuulRouteVO> result = zuulRouteMapper.seletZuulRoute();
		return result;
	}

}
