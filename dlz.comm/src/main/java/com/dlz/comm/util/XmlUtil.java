package com.dlz.comm.util;

import com.dlz.comm.exception.SystemException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * XML解析工具类
 * 
 * 使用XPath表达式解析XML文档，提供便捷的XML节点查询和数据提取功能
 * 
 * 文档地址：http://www.w3school.com.cn/xpath/index.asp
 * 
 * @author dk
 * @since 2023
 */
public class XmlUtil {
	private final XPath path;
	private final Document doc;

	/**
	 * 构造函数，初始化XML解析器
	 * 
	 * @param inputSource XML输入源
	 * @throws ParserConfigurationException 解析器配置异常
	 * @throws SAXException SAX解析异常
	 * @throws IOException IO异常
	 */
	private XmlUtil(InputSource inputSource) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = getDocumentBuilderFactory();
		DocumentBuilder db = dbf.newDocumentBuilder();
		doc = db.parse(inputSource);
		path = getXPathFactory().newXPath();
	}

	/**
	 * 创建XML工具类实例
	 *
	 * @param inputSource XML输入源
	 * @return XmlUtil实例
	 */
	private static XmlUtil create(InputSource inputSource) {
		try {
			return new XmlUtil(inputSource);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw SystemException.build(e);
		}
	}

	/**
	 * 从输入流创建XML工具类实例
	 *
	 * @param inputStream XML输入流
	 * @return XmlUtil实例
	 */
	public static XmlUtil of(InputStream inputStream) {
		InputSource inputSource = new InputSource(inputStream);
		return create(inputSource);
	}

	/**
	 * 从XML字符串创建XML工具类实例
	 *
	 * @param xmlStr XML字符串
	 * @return XmlUtil实例
	 */
	public static XmlUtil of(String xmlStr) {
		StringReader sr = new StringReader(xmlStr.trim());
		InputSource inputSource = new InputSource(sr);
		XmlUtil xmlUtil = create(inputSource);
		IoUtil.closeQuietly(sr);
		return xmlUtil;
	}

	/**
	 * 执行XPath表达式评估
	 *
	 * @param expression XPath表达式
	 * @param item 节点对象，如果为null则使用根文档
	 * @param returnType 返回类型
	 * @return 评估结果
	 */
	private Object evalXPath(String expression, Object item, QName returnType) {
		item = null == item ? doc : item;
		try {
			return path.evaluate(expression, item, returnType);
		} catch (XPathExpressionException e) {
			throw SystemException.build(e);
		}
	}

	/**
	 * 获取字符串类型的节点值
	 *
	 * @param expression XPath表达式
	 * @return 字符串类型的节点值
	 */
	public String getString(String expression) {
		return (String) evalXPath(expression, null, XPathConstants.STRING);
	}

	/**
	 * 获取布尔类型的节点值
	 *
	 * @param expression XPath表达式
	 * @return 布尔类型的节点值
	 */
	public Boolean getBoolean(String expression) {
		return (Boolean) evalXPath(expression, null, XPathConstants.BOOLEAN);
	}

	/**
	 * 获取数字类型的节点值
	 *
	 * @param expression XPath表达式
	 * @return 数字类型的节点值
	 */
	public Number getNumber(String expression) {
		return (Number) evalXPath(expression, null, XPathConstants.NUMBER);
	}

	/**
	 * 获取单个节点
	 *
	 * @param expression XPath表达式
	 * @return 节点对象
	 */
	public Node getNode(String expression) {
		return (Node) evalXPath(expression, null, XPathConstants.NODE);
	}

	/**
	 * 获取节点列表
	 *
	 * @param expression XPath表达式
	 * @return 节点列表
	 */
	public NodeList getNodeList(String expression) {
		return (NodeList) evalXPath(expression, null, XPathConstants.NODESET);
	}


	/**
	 * 获取相对于指定节点的字符串值
	 *
	 * @param node 起始节点
	 * @param expression 相对于节点的XPath表达式
	 * @return 字符串类型的节点值
	 */
	public String getString(Object node, String expression) {
		return (String) evalXPath(expression, node, XPathConstants.STRING);
	}

	/**
	 * 获取相对于指定节点的布尔值
	 *
	 * @param node 起始节点
	 * @param expression 相对于节点的XPath表达式
	 * @return 布尔类型的节点值
	 */
	public Boolean getBoolean(Object node, String expression) {
		return (Boolean) evalXPath(expression, node, XPathConstants.BOOLEAN);
	}

	/**
	 * 获取相对于指定节点的数字值
	 *
	 * @param node 起始节点
	 * @param expression 相对于节点的XPath表达式
	 * @return 数字类型的节点值
	 */
	public Number getNumber(Object node, String expression) {
		return (Number) evalXPath(expression, node, XPathConstants.NUMBER);
	}

	/**
	 * 获取相对于指定节点的子节点
	 *
	 * @param node 起始节点
	 * @param expression XPath表达式
	 * @return 节点对象
	 */
	public Node getNode(Object node, String expression) {
		return (Node) evalXPath(expression, node, XPathConstants.NODE);
	}

	/**
	 * 获取相对于指定节点的子节点列表
	 *
	 * @param node 起始节点
	 * @param expression 相对于节点的XPath表达式
	 * @return 节点列表
	 */
	public NodeList getNodeList(Object node, String expression) {
		return (NodeList) evalXPath(expression, node, XPathConstants.NODESET);
	}

	/**
	 * 将XML文档转换为Map格式（适用于简单的无嵌套节点结构）
	 *
	 * @return Map格式的XML数据
	 */
	public Map<String, String> toMap() {
		Element root = doc.getDocumentElement();
		Map<String, String> params = new HashMap<>(16);

		// 将节点封装成map形式
		NodeList list = root.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node instanceof Element) {
				params.put(node.getNodeName(), node.getTextContent());
			}
		}
		return params;
	}

	private static volatile boolean preventedXXE = false;

	/**
	 * 获取文档构建工厂
	 * 
	 * @return 文档构建工厂
	 * @throws ParserConfigurationException 解析器配置异常
	 */
	private static DocumentBuilderFactory getDocumentBuilderFactory() throws ParserConfigurationException {
		DocumentBuilderFactory dbf = XmlHelperHolder.documentBuilderFactory;
		if (!preventedXXE) {
			preventXXE(dbf);
		}
		return dbf;
	}

	/**
	 * 防止XXE（XML外部实体）攻击的安全配置
	 *
	 * @param dbf 文档构建工厂
	 * @throws ParserConfigurationException 解析器配置异常
	 */
	private static void preventXXE(DocumentBuilderFactory dbf) throws ParserConfigurationException {
		// 这是主要防御措施。如果禁止DTD（文档类型定义），几乎所有的XML实体攻击都能被阻止
		// Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
		dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

		// 如果不能完全禁用DTD，则至少做以下操作：
		// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
		// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities

		// JDK7+ - http://xml.org/sax/features/external-general-entities
		dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);

		// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
		// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities

		// JDK7+ - http://xml.org/sax/features/external-parameter-entities
		dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

		// 同样禁用外部DTD
		dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		// 以及这些，根据Timothy Morgan在2014年的论文："XML Schema, DTD, and Entity Attacks"
		dbf.setXIncludeAware(false);
		dbf.setExpandEntityReferences(false);
		preventedXXE = true;
	}

	/**
	 * 获取XPath工厂
	 * 
	 * @return XPath工厂
	 */
	private static XPathFactory getXPathFactory() {
		return XmlHelperHolder.xPathFactory;
	}

	/**
	 * 内部静态辅助类，用于单例模式持有工厂实例
	 */
	private static class XmlHelperHolder {
		private static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		private static XPathFactory xPathFactory = XPathFactory.newInstance();
	}

}