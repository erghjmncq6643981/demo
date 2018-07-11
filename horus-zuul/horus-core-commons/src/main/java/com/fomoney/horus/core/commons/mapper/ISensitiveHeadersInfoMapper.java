/*
 * ISensitiveHeadersInfo.java
 * 2017年12月28日 下午5:36:36
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

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 类功能描述
 * @version   
 * @author kyle 2017年12月28日下午5:36:36
 * @since 1.8
 */
@Repository
public interface ISensitiveHeadersInfoMapper {
	/**
	 * @param key
	 * @return List<String>
	 * @Description: TODO
	 * @create date 2017年12月28日下午5:37:33
	 */
	List<String> getSensitiveInfoList(Long key);
}

