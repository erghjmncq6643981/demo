package com.fomoney.horus.zuul.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Boot HelloWorld
 *
 * @author tim.loo
 *
 */
@RestController
public class HelloWorldController {

	private static final Logger logger = LoggerFactory.getLogger(HelloWorldController.class);

	/**
	 * 空白页，只返回状态码 ;
	 *
	 * @return 200 OK ;
	 * @throws Throwable
	 */
	@RequestMapping("/monitor/monitor.html")
	public ResponseEntity<String> monitor() throws Throwable {
		return new ResponseEntity<String>(HttpStatus.OK);
	}

}
