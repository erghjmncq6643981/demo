/*
 * ZuulRouteVO.java
 * 2017年7月16日 下午2:41:51
 * Copyright 2017 Fosun Financial. All  Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * Please contact Fosun Corporation or visit
 * www.fosun.com
 * if you need additional information or have any questions.
 * @author kyle
 * @version 1.0
 */

package com.fomoney.horus.core.commons.entity;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 动态路由对象
 *
 * @version
 * @author kyle 2017年7月16日下午2:41:51
 * @since 1.8
 */
public class ZuulRouteVO {

	private long keyOnly;
	/**
	 * The ID of the route (the same as its map key by default).
	 */
	private String id;

	/**
	 * The path (pattern) for the route, e.g. /foo/**.
	 */
	private String path;

	/**
	 * The service ID (if any) to map to this route. You can specify a physical
	 * URL or a service, but not both.
	 */
	private String serviceId;

	/**
	 * A full physical URL to map to the route. An alternative is to use a
	 * service ID and service discovery to find the physical address.
	 */
	private String url;

	/**
	 * Flag to determine whether the prefix for this route (the path, minus
	 * pattern patcher) should be stripped before forwarding.
	 */
	private boolean stripPrefix = true;

	/**
	 * Flag to indicate that this route should be retryable (if supported).
	 * Generally retry requires a service ID and ribbon.
	 */
	private boolean retryable;

	private boolean enabled;

	private Set<String> sensitiveHeaders = new LinkedHashSet<>();

	private boolean customSensitiveHeaders = false;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isStripPrefix() {
		return stripPrefix;
	}

	public void setStripPrefix(boolean stripPrefix) {
		this.stripPrefix = stripPrefix;
	}

	public boolean getRetryable() {
		return retryable;
	}

	public void setRetryable(boolean retryable) {
		this.retryable = retryable;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<String> getSensitiveHeaders() {
		return sensitiveHeaders;
	}

	public void setSensitiveHeaders(Set<String> sensitiveHeaders) {
		this.sensitiveHeaders = sensitiveHeaders;
	}

	public boolean isCustomSensitiveHeaders() {
		return customSensitiveHeaders;
	}

	public void setCustomSensitiveHeaders(boolean customSensitiveHeaders) {
		this.customSensitiveHeaders = customSensitiveHeaders;
	}

	public long getKeyOnly() {
		return keyOnly;
	}

	public void setKeyOnly(long keyOnly) {
		this.keyOnly = keyOnly;
	}

	/**
	 * @param zuulRouteVO
	 * @return boolean
	 * @Description:
	 * @create date 2018年5月18日下午4:58:50
	 */
	public Boolean equals(ZuulRouteVO zuulRouteVO) {
		if (null == zuulRouteVO || null == this) {
			return false;
		}
		int num1 = 0;
		int num2 = 0;
		if (keyOnly != zuulRouteVO.getKeyOnly()) {
			return false;
		}
		if (null == id || !id.equals(zuulRouteVO.getId())) {
			return false;
		}
		if (null == path || !path.equals(zuulRouteVO.getPath())) {
			return false;
		}
		if (null != serviceId && !serviceId.equals(zuulRouteVO.getServiceId())) {
			return false;
		}
		if ((null != url && !url.equals(zuulRouteVO.getUrl()))
				|| (null != zuulRouteVO.getUrl() && !zuulRouteVO.getUrl().equals(url))) {
			return false;
		}
		if (retryable) {
			num1 += 1;
		}
		if (zuulRouteVO.getRetryable()) {
			num2 += 1;
		}
		if (num1 != num2) {
			return false;
		}
		if (stripPrefix) {
			num1 += 1;
		}
		if (zuulRouteVO.isStripPrefix()) {
			num2 += 1;
		}
		if (num1 != num2) {
			return false;
		}
		if (customSensitiveHeaders) {
			num1 += 1;
		}
		if (zuulRouteVO.isCustomSensitiveHeaders()) {
			num2 += 1;
		}
		if (num1 != num2) {
			return false;
		}
		return true;
	}
}
