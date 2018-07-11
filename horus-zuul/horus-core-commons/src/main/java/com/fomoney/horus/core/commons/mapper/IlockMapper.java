/*
 * IlockMapper.java
 * 2017年9月7日 下午2:12:10
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

import com.fomoney.horus.core.commons.entity.Lcok;



/**
 * 类功能描述
 * @version
 * @author kyle 2017年9月7日下午2:12:10
 * @since 1.8
 */
@Repository
public interface IlockMapper {
	@Select("select lock_name,lock_name from distributed_lock for update")
	@Results({ @Result(column = "lock_name", property = "lockName"),
			@Result(column = "lock_name", property = "expireTime") })
	public List<Lcok> findAllLock();
}
