/*
 * FilterType.java
 * 2018年5月18日 下午5:38:42
 * Copyright 2017 Fosun Financial. All  Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * Please contact Fosun Corporation or visit         
 * www.fosun.com 
 * if you need additional information or have any questions.
 * @author kyle
 * @version 1.0
 */

package com.fomoney.horus.core.commons.constant;

/**
 * ZuulFilter执行类型："pre"表示路由之前；"route"表示路由的时候，"post"表示路由之后
 * 
 * @version
 * @author kyle 2018年5月18日下午5:38:42
 * @since 1.8
 */
public class FilterTypeLabel {
	/**
	 * "pre"表示路由之前；
	 */
	public static String ROUTEBEFORE = "pre";
	/**
	 * "route"表示路由的时候
	 */
	public static String ROUTETIME = "route";
	/**
	 * "post"表示路由之后
	 */
	public static String ROUTEAFTER = "post";
}
