/*
 * XMLUtil.java
 * 2017年9月6日 上午10:00:49
 * Copyright 2017 Fosun Financial. All  Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * Please contact Fosun Corporation or visit
 * www.fosun.com
 * if you need additional information or have any questions.
 * @author kyle
 * @version 1.0
 */

package com.fomoney.horus.core.commons.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * xml文件读写工具类
 *
 * @version
 * @author kyle 2017年9月6日上午10:00:49
 * @since 1.8
 */
public class XMLUtil {
	private static Logger logger = LoggerFactory.getLogger(XMLUtil.class);
	@Value("${zuulUtil.xmlUtil.xmlFilePath:/apps/config/zuul/}")
	private String xmlFilePath;

	/**
	 * @param path
	 * @return boolean
	 * @Description: 创建xml文件
	 * @create date 2017年9月6日上午10:12:33
	 */
	public boolean createXMLFile(String path) {
		boolean writen = true;
		OutputStream outputStream = null;
		XMLWriter xmlWriter = null;
		Document document = null;
		String rootPath = getPath(path);
		try {
			document = DocumentHelper.createDocument();
			Element rootElement = DocumentHelper.createElement("rootConfig");
			document.add(rootElement);
			OutputFormat outputFormat = new OutputFormat();
			outputFormat.setEncoding("UTF-8");
			outputStream = new FileOutputStream(rootPath);
			xmlWriter = new XMLWriter(outputStream, outputFormat);
			xmlWriter.write(document);
		} catch (IOException e) {
			logger.error("ConfigFileServiceImpl.createServerDotXML error: " + e);
			writen = false;
		} catch (Exception e) {
			logger.error("ConfigFileServiceImpl.createServerDotXML error: " + e);
			writen = false;
		} finally {
			close(xmlWriter, outputStream, null, null);
		}
		return writen;
	}

	/**
	 * @param filePath
	 * @return Document
	 * @Description: 解析xml文件
	 * @create date 2017年9月6日上午10:15:00
	 */
	public Document readXmlDocument(String filePath) {
		if (filePath.isEmpty()) {
			return null;
		}
		InputStream inputStream = null;
		Document document = null;
		File file = null;
		Reader reader = null;
		// 解析xml文档内容
		try {
			file = new File(getPath(filePath));
			SAXReader saxReader = new SAXReader();
			inputStream = new FileInputStream(file);
			reader = new InputStreamReader(inputStream, "UTF-8");
			document = saxReader.read(reader);
		} catch (Exception e) {
			logger.error("XMLUtil.readXmlDocument error: " + e);
			document = null;
		} finally {
			close(null, null, inputStream, reader);
		}
		return document;
	}

	/**
	 * @param filePath
	 * @return Elements
	 * @Description: 取得xml文件获取顶级节点
	 * @create date 2017年9月6日上午10:27:32
	 */
	@SuppressWarnings("unchecked")
	public List<Element> readXmlElements(String filePath) {
		List<Element> elementList = null;
		// 解析xml文档内容
		try {
			Document document = readXmlDocument(filePath);
			Element root = document.getRootElement();
			elementList = root.elements();
			logger.debug("XMLUtil.readXmlElements root name:" + root.getName());
		} catch (Exception e) {
			logger.error("XMLUtil.readXmlElements error: " + e);
			return null;
		}
		return elementList;
	}

	/**
	 * @param parentElement
	 * @param labelName
	 * @param propertyName
	 * @param propertyValue
	 * @return element
	 * @Description: 根据父节点和节点属性获得目标子节点
	 * @create date 2017年9月6日上午10:37:08
	 */
	public Element getSubDocument(Element parentElement, String labelName, String propertyName, String propertyValue) {
		try {
			@SuppressWarnings("unchecked")
			List<Element> elements = parentElement.elements(labelName);
			Iterator<Element> iterator = elements.iterator();
			while (iterator.hasNext()) {
				Element element = iterator.next();
				String value = element.attributeValue(propertyName);
				if (propertyValue.equals(value)) {
					return element;
				}
			}
			return null;
		} catch (Exception e) {
			logger.error("XMLUtil.getSubDocument error: " + e);
			return null;
		}
	}

	/**
	 * @param parentElement
	 * @param labelName
	 * @param labelContext
	 * @return boolean
	 * @Description: 一个标签中的多个子标签的文本是否存在labelContext
	 * @create date 2017年9月6日下午6:43:58
	 */
	public boolean judgeElementExsit(Element parentElement, String labelName, String labelContext) {
		try {
			@SuppressWarnings("unchecked")
			List<Element> elements = parentElement.elements(labelName);
			Iterator<Element> iterator = elements.iterator();
			while (iterator.hasNext()) {
				Element element = iterator.next();
				if (labelContext.equals(element.getText())) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			logger.error("XMLUtil.getSubDocument error: " + e);
			return false;
		}
	}

	/**
	 * @param parentElement
	 * @param element
	 * @param elementName
	 * @return Element
	 * @Description: 判断element是否存在，如果不存在就创造名为elementName的element；
	 * @create date 2017年9月7日下午4:14:04
	 */
	public Element judgeElementExsit(Element parentElement, Element element, String elementName) {
		if (element != null) {
			return element;
		}
		Element subElement = DocumentHelper.createElement(elementName);
		parentElement.add(subElement);
		return subElement;
	}

	/**
	 * @param parentElement
	 * @param element
	 * @param elementName
	 * @param attributeName
	 * @param attributeValue
	 * @return Element
	 * @Description: 判断element是否存在，如果不存在就创造名为elementName的element,并附带属性。
	 * @create date 2017年9月8日下午3:38:00
	 */
	public Element judgeElementExsitAttr(Element parentElement, Element element, String elementName,
			String attributeName, String attributeValue) {
		if (element != null) {
			return element;
		}
		Element subElement = DocumentHelper.createElement(elementName);
		if (!"".equals(attributeName)) {
			subElement.addAttribute(attributeName, attributeValue);
		}
		parentElement.add(subElement);
		return subElement;
	}

	/**
	 * @param parentElement
	 * @param targetLabelName
	 * @return List<String>
	 * @Description: 找出标签中的目标标签内容
	 * @create date 2017年9月6日下午1:41:19
	 */
	@SuppressWarnings("unchecked")
	public List<String> getDocumentValue(Element parentElement, String targetLabelName) {
		try {
			List<Element> elements = parentElement.elements(targetLabelName);
			Iterator<Element> iterator = elements.iterator();
			List<String> list = new LinkedList<>();
			while (iterator.hasNext()) {
				Element element = iterator.next();
				list.add(element.getText());
			}
			return list;
		} catch (Exception e) {
			logger.error("XMLUtil.getDocumentValue error: " + e);
			return null;
		}
	}

	/**
	 * @param parentElement
	 * @param xpath
	 * @param targetElement
	 * @return list<Element>
	 * @Description: TODO
	 * @create date 2017年9月8日下午5:34:41
	 */
	@SuppressWarnings("unchecked")
	public List<Element> getElementsByXPath(Element parentElement, String xpath, String targetElement) {
		String[] targets = xpath.split("/");
		List<Element> elements = null;
		for (int i = 0; i < targets.length; i++) {
			if (!"".equals(targets[i])) {
				if (targetElement.equals(targets[i])) {
					elements = parentElement.elements(targetElement);
					break;
				}
				parentElement = parentElement.element(targets[i]);
			}
		}
		return elements;
	}

	/**
	 * @param document
	 * @param fileName
	 * @return boolean
	 * @Description: 将document写入到指定xml文件中
	 * @create date 2017年9月7日下午4:08:33
	 */
	public boolean writeDataToXml(Document document, String fileName) {
		OutputStream outputStream = null;
		XMLWriter xmlWriter = null;
		boolean result = false;
		OutputFormat outputFormat = OutputFormat.createPrettyPrint();
		outputFormat.setEncoding("UTF-8");
		try {
			outputStream = new FileOutputStream(getPath(fileName));
			xmlWriter = new XMLWriter(outputStream, outputFormat);
			xmlWriter.write(document);
			outputStream.flush();
			xmlWriter.flush();
			result = true;
		} catch (Exception e) {
			result = false;
		} finally {
			close(xmlWriter, outputStream, null, null);
		}
		return result;
	}

	/**
	 * @param element
	 * @param subElements
	 * @param subElementValues
	 * @return Element
	 * @Description: 创建一个带多个子标签的标签
	 * @create date 2017年9月6日下午5:27:30
	 */
	public Element addsubElements(Element element, String[] subElements, String[] subElementValues) {
		try {
			for (int i = 0; i < subElements.length; i++) {
				Element subElement = DocumentHelper.createElement(subElements[i]);
				if (subElementValues != null) {
					if (i < subElementValues.length) {
						subElement.setText(subElementValues[i]);
					}
				}
				element.add(subElement);
			}
			return element;
		} catch (Exception e) {
			logger.error("XMLUtil.writeDocumentToXml error: " + e);
			return null;
		}
	}

	/**
	 * @param element
	 * @param subElement
	 * @param subElementValue
	 * @return Element
	 * @Description: 创建一个带一个子标签的标签
	 * @create date 2017年9月8日下午3:55:29
	 */
	public Element addsubElement(Element element, String subElement, String subElementValue) {
		try {
			if (!"".equals(subElement)) {
				Element newElement = DocumentHelper.createElement(subElement);
				newElement.setText(subElementValue);
				element.add(newElement);
			}
			return element;
		} catch (Exception e) {
			logger.error("XMLUtil.writeDocumentToXml error: " + e);
			return null;
		}
	}

	/**
	 * @param xmlWriter
	 * @param outputStream
	 * @param inputStream
	 * @param reader
	 * @Description: 关闭流
	 * @create date 2017年9月6日上午10:05:21
	 */
	public void close(XMLWriter xmlWriter, OutputStream outputStream, InputStream inputStream, Reader reader) {
		try {
			if (xmlWriter != null) {
				xmlWriter.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
			if (reader != null) {
				reader.close();
			}
		} catch (IOException e) {
			logger.error("XMLUtil.close error: " + e);
		}
	}

	/**
	 * @param fileName
	 * @return rootPath
	 * @Description: eclipse中读取resource中的文件，部署读取jar同级包config中的文件
	 * @create date 2017年9月7日下午4:07:37
	 */
	public String getPath(String fileName) {
		String rootPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		if (rootPath.indexOf(".jar!/") == -1) {
			xmlFilePath = rootPath.split("target")[0] + "src/main/resources/";
		}
		rootPath = xmlFilePath + fileName + ".xml";
		return rootPath;
	}
}
