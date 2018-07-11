/*
 * LockService.java
 * 2017年9月8日 上午11:26:44
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fomoney.horus.core.commons.dao.LockDao;
import com.fomoney.horus.core.commons.dao.ServerWhiteInfoDao;
import com.fomoney.horus.core.commons.entity.Lcok;
import com.fomoney.horus.core.commons.entity.ServerWhiteInfo;

/**
 * 获取分布式锁，修改白名单数据状态
 *
 * @version
 * @author kyle 2017年9月8日上午11:26:44
 * @since 1.8
 */
@Service
@Transactional
public class LockService {
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(LockService.class);
	@Autowired
	private LockDao lockDao;
	@Autowired
	private ServerWhiteInfoDao serverWhiteInfoDao;
	@Value("${zuulUtil.white.updatePeers:3}")
	private Integer updatePeers;
	@Value("${zuulUtil.white.updateLabel:true}")
	private boolean updateLabel = true;

	/**
	 * @param updatePeers
	 * @Description:
	 * @create date 2017年12月29日下午1:39:02
	 */
	public void setUpdatePeers(Integer updatePeers) {
		this.updatePeers = updatePeers;
	}

	/**
	 * @param updateLabel
	 * @Description:
	 * @create date 2017年10月20日下午3:42:20
	 */
	public void setUpdateLabel(boolean updateLabel) {
		this.updateLabel = updateLabel;
	}

	/**
	 * @param serverWhiteInfo
	 * @return locks
	 * @Description:
	 * @create date 2017年9月8日下午3:33:15
	 */
	public List<Lcok> updateStatus(ServerWhiteInfo serverWhiteInfo) {
		List<Lcok> locks = lockDao.findAllLock();
		if (updateLabel) {
			updateDataRepository(serverWhiteInfo);
		}
		return locks;
	}

	private void updateDataRepository(ServerWhiteInfo serverWhiteInfo) {
		if (serverWhiteInfo.getUpdateLabel() >= (updatePeers - 1)) {
			serverWhiteInfo.setAddLabel("no");
			serverWhiteInfo.setUpdateLabel(0);
		} else {
			serverWhiteInfo.setUpdateLabel(serverWhiteInfo.getUpdateLabel() + 1);
		}
		serverWhiteInfoDao.updateWhiteInfoAddLabel(serverWhiteInfo);
	}

}
