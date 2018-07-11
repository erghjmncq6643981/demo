/*
 * IZuulRouteMapper.java
 * 2017年7月16日 下午3:04:18
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

import com.fomoney.horus.core.commons.entity.ZuulRouteVO;




/**
 * 类功能描述
 *
 * @version
 * @author kyle 2017年7月16日下午3:04:18
 * @since 1.8
 */
@Repository
public interface IZuulRouteMapper {
	public List<ZuulRouteVO> seletZuulRoute();
}
