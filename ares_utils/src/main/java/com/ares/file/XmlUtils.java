package com.ares.file;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ares.reflect.ReflectUtil;

@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
public class XmlUtils {
	/**
	 * 获取xml的实例,支持该xml结构（在最下面）
	 *
	 * @param path
	 * @param fileName
	 * @param configClass
	 * @param             <T>
	 * @return
	 */
	public static <T extends Object> T getConfigXML(String path, String fileName, Class<T> configClass) {
		T ob = null;
		// fileName = path + File.separatorChar + fileName;
		fileName = path + fileName;
		if (!new File(fileName).exists()) {
			return ob;
		}

		ob = xmlStrToBean(new File(fileName), configClass);
		return ob;
	}

	/**
	 * xml字符串转换成bean对象
	 *
	 * @param clazz 待转换的class
	 * @return 转换后的对象
	 */
	public static <T> T xmlStrToBean(File file, Class<T> clazz) {
		T t = null;
		try {
			// 获取所有的节点及该节点对应的子节点
			List<XmlNode> list = xmlStrToList(file);
			// 将map对象的数据转换成Bean对象
			t = listToBean(list, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * 将Map对象通过反射机制转换成Bean对象
	 *
	 * @param list  第一层级的节点集合
	 * @param clazz 待转换的class
	 * @return 转换后的Bean对象
	 * @throws Exception 异常
	 */
	public static <T> T listToBean(List<XmlNode> list, Class<T> clazz) throws Exception {
		// 实例的set方法
		T t = clazz.newInstance();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				XmlNode xmlNode = list.get(i);
				Element currentNode = xmlNode.getCurrentNode();
				// 如果当前有子节点，那么需要先解析子节点
				String textTrim = currentNode.getTextTrim();
				String name = currentNode.getName();

				Field field = ReflectUtil.getDeclaredField(clazz, name);
				if (field == null) {
					throw new NullPointerException("xml节点【" + name + "】在class中没有对应的字段");
				}
				field.setAccessible(true);

				Class fieldTypeClass = field.getType();

				if (fieldTypeClass.getSimpleName().equals("List")) {

					List<XmlNode> childNodes = xmlNode.getChildNode();
					if (childNodes.size() > 0) {
						List _list = new ArrayList();
						for (XmlNode childNode : childNodes) {
							Element _currentNode = childNode.getCurrentNode();

							Class<?> genericClass = ReflectUtil.getGenericClassByList(field);

							_list.add(listToBean(childNode.getChildNode(), genericClass));
						}

						field.set(t, _list);
					} else {
						field.setAccessible(true);
						Object obj = field.get(t);

						List _list = obj == null ? new ArrayList() : (List) obj;

						// 获取当前list的泛型
						Class<?> genericClass = ReflectUtil.getGenericClassByList(field);

						// 检查当前节点类型与list的泛型类型是否一致
						if (_list.size() > 0) {
							Object o = _list.get(0);
							if (!o.getClass().getName().equals(genericClass.getName())) {
								throw new IllegalArgumentException("类型不一致");
							}
						}

						// 如果当前的节点的子节点为0，说明当前xml是<X>XX</X><X>XX</X>平行结构(当前节点是基础类型)
						_list.add(ReflectUtil.convertValType(textTrim, genericClass));

						field.set(t, _list);
					}

					continue;
				}
				Object value = ReflectUtil.convertValType(textTrim, fieldTypeClass);
				field.set(t, value);
			}
		}
		return t;
	}

//    /**
//     * 将Object类型的值，转换成bean对象属性里对应的类型值
//     *
//     * @param value          Object对象值
//     * @param fieldTypeClass 属性的类型
//     * @return 转换后的值
//     */
//    private static Object convertValType(Object value, Class fieldTypeClass) {
//        Object retVal = null;
//        if (Long.class.getName().equals(fieldTypeClass.getName())
//                || long.class.getName().equals(fieldTypeClass.getName())) {
//            retVal = Long.parseLong(value.toString());
//        } else if (Integer.class.getName().equals(fieldTypeClass.getName())
//                || int.class.getName().equals(fieldTypeClass.getName())) {
//            retVal = Integer.parseInt(value.toString());
//        } else if (Float.class.getName().equals(fieldTypeClass.getName())
//                || float.class.getName().equals(fieldTypeClass.getName())) {
//            retVal = Float.parseFloat(value.toString());
//        } else if (Double.class.getName().equals(fieldTypeClass.getName())
//                || double.class.getName().equals(fieldTypeClass.getName())) {
//            retVal = Double.parseDouble(value.toString());
//        } else if (String.class.getName().equals(fieldTypeClass.getName())) {
//            retVal = value.toString();
//        } else {
//            retVal = value;
//        }
//        return retVal;
//    }

	/**
	 * 获取List集合的泛型class
	 *
	 * @param field
	 * @return
	 */
//    public static Class<?> getGenericClass(Field field) {
//        Type genericSuperclass = field.getGenericType();
//        if (genericSuperclass instanceof ParameterizedType) {
//            ParameterizedType genericClassList = (ParameterizedType) genericSuperclass;
//            //得到泛型里的class类型对象(list只会有一个参数)
//            Class<?> genericClazz = (Class<?>) genericClassList.getActualTypeArguments()[0];
//            return genericClazz;
//        }
//        return null;
//    }

	/**
	 * 获取指定字段名称查找在class中的对应的Field对象(包括查找父类)
	 *
	 * @param clazz     指定的class
	 * @param fieldName 字段名称
	 * @return Field对象
	 */
//    private static Field getClassField(Class clazz, String fieldName) {
//        if (Object.class.getName().equals(clazz.getName())) {
//            return null;
//        }
//        Field[] declaredFields = clazz.getDeclaredFields();
//        for (Field field : declaredFields) {
//            if (field.getName().equals(fieldName)) {
//                return field;
//            }
//        }
//
//        Class superClass = clazz.getSuperclass();
//        if (superClass != null) {// 简单的递归一下
//            return getClassField(superClass, fieldName);
//        }
//        return null;
//    }

	/**
	 * 返回第一层级节点集合
	 *
	 * @throws Exception 异常
	 */
	public static List<XmlNode> xmlStrToList(File file) throws Exception {
		Map<String, XmlNode> map = new HashMap<String, XmlNode>();
		// 获取根节点
		Element root = getRootElement(file);
		// 获取根节点下的所有元素
		List<Element> children = root.elements();

		// 第一层级下的节点
		List<XmlNode> firstNodes = new ArrayList<>();

		for (Element node : children) {
			XmlNode xmlNode = new XmlNode();
			xmlNode.setCurrentNode(node);

			List<XmlNode> childs = new ArrayList<>();

			getNextNodes(node, childs);
			xmlNode.setChildNode(childs);

			firstNodes.add(xmlNode);
		}

		return firstNodes;
	}

	// 找到下一层子节点
	private static void getNextNodes(Element currentNode, List<XmlNode> nodes) {
		// 找到当前节点的子节点
		List<Element> childs = currentNode.elements();
		if (childs != null && childs.size() > 0) {
			for (Element node : childs) {
				XmlNode xmlNode = new XmlNode();
				xmlNode.setCurrentNode(node);

				List<XmlNode> _childs = new ArrayList<>();

				getNextNodes(node, _childs);

				xmlNode.setChildNode(_childs);

				nodes.add(xmlNode);
			}
		}
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
//    public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
//        Field field = getAccessibleField(obj, fieldName);
//        if (field == null) {
//            throw new IllegalArgumentException("Could not find field ["
//                    + fieldName + "] on target [" + obj + "]");
//        }
//        try {
//            field.set(obj, value);
//        } catch (IllegalAccessException e) {
//        }
//    }

	public static Element getRootElement(String xml) {
		Document doc = null; // 将字符串转为XML
		try {
			doc = DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return doc.getRootElement();
	}

	public static Element getRootElement(File filePath) {
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(filePath);
			String text = document.getText();
			return document.getRootElement();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

//<?xml version="1.0" encoding="UTF-8"?>
//<config>
//    <nodes>
//        <RedisClusterNodesConfig>
//            <ip>192.168.0.101</ip>
//            <port>6380</port>
//            <host>21312312</host>
//            <host>21312312</host>
//            <host>21312312</host>
//            <host>21312312</host>
//            <abc>21312312</abc>
//            <ddd>21312312</ddd>
//            <ccc>123882293</ccc>
//            <ccc>123882293</ccc>
//            <ccc>123882293</ccc>
//            <ccc>123882293</ccc>
//        </RedisClusterNodesConfig>
//        <RedisClusterNodesConfig>
//            <ip>192.168.0.101</ip>
//            <port>6381</port>
//            <host>21312312</host>
//            <host>21312312</host>
//            <host>21312312</host>
//            <host>21312312</host>
//            <ccc>123882293</ccc>
//            <ccc>123882293</ccc>
//        </RedisClusterNodesConfig>
//        <RedisClusterNodesConfig>
//            <ip>192.168.0.101</ip>
//            <port>6382</port>
//            <host>21312312</host>
//            <host>21312312</host>
//            <host>21312312</host>
//            <host>21312312</host>
//        </RedisClusterNodesConfig>
//        <RedisClusterNodesConfig>
//            <ip>192.168.0.101</ip>
//            <port>6383</port>
//            <host>21312312</host>
//            <host>21312312</host>
//            <host>21312312</host>
//            <host>21312312</host>
//        </RedisClusterNodesConfig>
//        <RedisClusterNodesConfig>
//            <ip>192.168.0.101</ip>
//            <port>6384</port>
//            <host>21312312</host>
//            <host>21312312</host>
//            <host>21312312</host>
//        </RedisClusterNodesConfig>
//        <RedisClusterNodesConfig>
//            <ip>192.168.0.101</ip>
//            <port>6385</port>
//            <host>21312312</host>
//            <host>21312312</host>
//        </RedisClusterNodesConfig>
//    </nodes>
//    <password>abc..123>></password>
//    <timeout>5000</timeout>
//    <database>6</database>
//</config>
}
