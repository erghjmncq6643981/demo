/*
 * ResponseErrorCode.java
 * 2018年1月15日 下午4:48:57
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

import org.apache.commons.lang.StringUtils;

import com.netflix.zuul.context.RequestContext;

/**
 * 网关路由异常类
 * 
 * @version
 * @author kyle 2018年1月15日下午4:48:57
 * @since 1.8
 */
public class ResponseErrorCode {
	/**
	 * 异常反馈枚举类
	 * 
	 * @version
	 * @author kyle 2018年1月15日下午5:02:05
	 * @since 1.8
	 */
	public enum ErrorCodeEnum {
		/** 微服务缺失 - 435 */
		SERVICEERROR("435", "serviceId is no exist，please check request  path"),
		/** 非白名单用户 - 431 */
		WHITEERROR("431", "request is not whiteIP"),
		/** token无效 - 432 */
		TOKENERROR("432", "token is disabled");

		private String code;
		private String content;

		/**
		 * @return code
		 * @Description:
		 * @create date 2018年1月15日下午5:02:42
		 */
		public String getCode() {
			return code;
		}

		private ErrorCodeEnum(String code, String content) {
			this.code = code;
			this.content = content;
		}

		/**
		 * @param code
		 * @return content
		 * @Description:
		 * @create date 2018年1月15日下午5:02:11
		 */
		public String getTypeName(String code) {
			if (StringUtils.isEmpty(code)) {
				return null;
			}
			for (ErrorCodeEnum status : values()) {
				if (status.code.equals(code)) {
					return status.content;
				}
			}
			return null;
		}
	}

	/**
	 * @param context
	 * @param status
	 * @param message
	 * @Description:
	 * @create date 2018年1月15日下午5:27:23
	 */
	public static void setContext(RequestContext context, String status, String message) {
		int code = Integer.parseInt(status);
		context.setSendZuulResponse(false);
		context.setResponseStatusCode(code);
		context.set("isSuccess", false);
		context.setResponseBody(message);
	}
}
