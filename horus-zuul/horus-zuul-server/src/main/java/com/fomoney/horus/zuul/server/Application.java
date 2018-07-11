package com.fomoney.horus.zuul.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;

import com.fomoney.chaos.framework.util.support.util.logback.LogbackExtraposition;
import com.fomoney.chaos.framework.util.support.util.ribbon.CustomRibbonClientConfigurationRegistrar;
import com.fomoney.chaos.framework.util.support.util.ribbon.ExcludeFromComponentScan;

/**
 * Created by luqs on 2017/5/17.
 */

@EnableZuulProxy
@EnableEurekaClient
@Configuration
@EnableAutoConfiguration
@MapperScan(basePackages = { "com.fomoney.horus.core.commons.mapper", "com.fomoney.jjwt.mapper" })
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = ExcludeFromComponentScan.class), basePackages = {
		"com.fomoney.horus.*", "com.fomoney.jjwt.*" })
@ConfigurationProperties(prefix = "zuulUtil.application")
@ImportResource(locations = { "classpath*:spring-horus-zuul.xml" })
public class Application {
	private boolean logbackExtrapositionWay;
	private String logbackName;
	private String logbackPath;
	private String ribbonClientNames;
	private String ribbonRuleName;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * @param registry
	 * @return CustomRibbonClientConfigurationRegistrar
	 * @Description:
	 * @create date 2017年10月23日下午1:23:32
	 */
	@Bean
	public CustomRibbonClientConfigurationRegistrar getCustomRibbonClientConfigurationRegistrar(
			BeanDefinitionRegistry registry) {
		CustomRibbonClientConfigurationRegistrar ribbonClientRegistar = new CustomRibbonClientConfigurationRegistrar(
				ribbonClientNames, ribbonRuleName);
		ribbonClientRegistar.registerBeanDefinitions(registry);
		return ribbonClientRegistar;
	}

	/**
	 * @return BeanDefinitionRegistry
	 * @Description:
	 * @create date 2018年4月10日上午10:45:30
	 */
	@Bean
	public BeanDefinitionRegistry getBeanDefinitionRegistry() {
		BeanDefinitionRegistry registry = new DefaultListableBeanFactory();
		return registry;
	}

	/**
	 * @return LogbackExtraposition
	 * @Description:
	 * @create date 2018年4月10日上午10:45:39
	 */
	@Bean
	public LogbackExtraposition loadLogbackConfiguration() {
		LogbackExtraposition logback = new LogbackExtraposition();
		logback.setLogbackExtrapositionWay(logbackExtrapositionWay);
		logback.setLogbackName(logbackName);
		logback.setLogbackPath(logbackPath);
		logback.loadLogbackFile();
		return logback;
	}

	public void setLogbackExtrapositionWay(boolean logbackExtrapositionWay) {
		this.logbackExtrapositionWay = logbackExtrapositionWay;
	}

	public void setLogbackName(String logbackName) {
		this.logbackName = logbackName;
	}

	public void setLogbackPath(String logbackPath) {
		this.logbackPath = logbackPath;
	}

	public void setRibbonClientNames(String ribbonClientNames) {
		this.ribbonClientNames = ribbonClientNames;
	}

	public void setRibbonRuleName(String ribbonRuleName) {
		this.ribbonRuleName = ribbonRuleName;
	}

}
