/*
 * LockDao.java
 * 2017年9月8日 上午11:04:36
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

import com.fomoney.horus.core.commons.entity.Lcok;
import com.fomoney.horus.core.commons.mapper.IlockMapper;


/**
 * 类功能说明
 * <p>
 * <strong></strong>
 * </p>
 *
 * @version
 * @author kyle 2017年9月8日上午11:26:10
 * @since 1.8
 */
@Repository
public class LockDao {
	@Autowired
	private IlockMapper lockMapper;

	public List<Lcok> findAllLock() {
		return lockMapper.findAllLock();
	}
}
