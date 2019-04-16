package com.ares.reflect;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.ares.log.LogUtil;

/**
 * 反射工具
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReflectUtil {

	/**
	 * 循环向上转型, 获取对象的 DeclaredField
	 *
	 * @param clazz     : Class
	 * @param fieldName : 父类中的属性名
	 * @return 父类中的属性对象
	 */
	public static Field getDeclaredField(Class<?> clazz, String fieldName) {
		Field field = null;
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				field = clazz.getDeclaredField(fieldName);
				return field;
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * 获取所有属性，包括父类
	 *
	 * @return
	 */
	public static List<Field> getDeclaredFields(Object object) {
		List<Field> fields = new ArrayList<>();
		Class<?> clazz = object.getClass();
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				Field[] declaredFields = clazz.getDeclaredFields();
				fields.addAll(Arrays.asList(declaredFields));
			} catch (Exception e) {
			}
		}
		return fields;
	}

	/**
	 * 获取类的setter方法
	 *
	 * @return
	 */
	public static Map<String, Method> getWriteMethod(Class clazz) {
		Map<String, Method> getMethods = new ConcurrentHashMap<>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method write = property.getWriteMethod();
					if (write != null) {
						getMethods.put(key, write);
					}
				}
			}
		} catch (Exception e) {
			LogUtil.error("ReflectError", e);
		}
		return getMethods;
	}

	/**
	 * 获取类的getter方法
	 *
	 * @return
	 */
	public static Map<String, Method> getReadMethod(Class clazz) {
		Map<String, Method> getMethods = new ConcurrentHashMap<>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method write = property.getReadMethod();
					if (write != null) {
						getMethods.put(key, write);
					}
				}
			}
		} catch (Exception e) {
			LogUtil.error("ReflectError", e);
		}
		return getMethods;
	}

	/**
	 * 拼接在某属性的 set方法
	 *
	 * @param field
	 * @return String
	 */
	public static String parSetName(Field field) {
		String fieldName = field.getName();
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		int startIndex = 0;
		return new StringBuilder("set").append(fieldName.substring(startIndex, startIndex + 1).toUpperCase())
				.append(fieldName.substring(startIndex + 1)).toString();
	}

	/**
	 * 判断是否存在某属性的 set方法
	 *
	 * @param methods
	 * @param fieldSetMet
	 * @return boolean
	 */
	public static Method getSetMet(Method[] methods, String fieldSetMet) {
		for (Method met : methods) {
			if (fieldSetMet.equals(met.getName())) {
				return met;
			}
		}
		return null;
	}

	/**
	 * 通过反射获取指定接口的所有实现类
	 *
	 * @param interfaceName 接口全类名
	 */
	public static List<Class<?>> getAllSubClass(String interfaceName) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Class<?> classOfClassLoader = classLoader.getClass();

		Class<?> interfaceClass = null;
		try {
			interfaceClass = (Class<?>) Class.forName(interfaceName);
		} catch (ClassNotFoundException e) {
			System.out.println("无法获取到TestInterface的Class对象!查看包名,路径是否正确");
		}

		Field field = null;
		while (classOfClassLoader != ClassLoader.class) {
			classOfClassLoader = classOfClassLoader.getSuperclass();
		}
		try {
			field = classOfClassLoader.getDeclaredField("classes");
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			System.out.println("无法获取到当前线程的类加载器的classes域!");
		}

		field.setAccessible(true);

		Vector v = null;
		try {
			v = (Vector) field.get(classLoader);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			System.out.println("无法从类加载器中获取到类属性!");
		}
		List<Class<?>> allSubclass = new ArrayList<>();

		for (int i = 0; i < v.size(); i++) {
			Class<?> c = (Class<?>) v.get(i);
			if (interfaceClass.isAssignableFrom(c) && !interfaceClass.equals(c)) {
				allSubclass.add((Class<?>) c);
			}
		}

		return allSubclass;
	}

	/**
	 * 获取List集合的泛型class
	 *
	 * @param field
	 * @return
	 */
	public static Class<?> getGenericClassByList(Field field) {
		Type genericSuperclass = field.getGenericType();
		if (genericSuperclass instanceof ParameterizedType) {
			ParameterizedType genericClassList = (ParameterizedType) genericSuperclass;
			// 得到泛型里的class类型对象(list只会有一个参数)
			Class<?> genericClazz = (Class<?>) genericClassList.getActualTypeArguments()[0];
			return genericClazz;
		}
		return null;
	}

	/**
	 * 将Object类型的值，转换成bean对象属性里对应的类型值
	 *
	 * @param value          Object对象值
	 * @param fieldTypeClass 属性的类型
	 * @return 转换后的值
	 */
	public static Object convertValType(Object value, Class fieldTypeClass) {
		Object retVal = null;
		if (Long.class.getName().equals(fieldTypeClass.getName())
				|| long.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Long.parseLong(value.toString());
		} else if (Integer.class.getName().equals(fieldTypeClass.getName())
				|| int.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Integer.parseInt(value.toString());
		} else if (Float.class.getName().equals(fieldTypeClass.getName())
				|| float.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Float.parseFloat(value.toString());
		} else if (Double.class.getName().equals(fieldTypeClass.getName())
				|| double.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Double.parseDouble(value.toString());
		} else if (String.class.getName().equals(fieldTypeClass.getName())) {
			retVal = value.toString();
		} else if (Boolean.class.getName().equals(fieldTypeClass.getName())
				|| boolean.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Boolean.valueOf(value.toString());
		} else {
			retVal = value;
		}
		return retVal;
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		Field field = getDeclaredField(obj.getClass(), fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}
		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
		}
	}

	/**
	 * 获取并创建内部类的对象
	 *
	 * @param clazz 父类
	 */
	public static void getInnerClasses(Class<?> clazz) {
		try {
			Object container = clazz.newInstance();
			Class innerClazz[] = clazz.getDeclaredClasses();
			for (Class cls : innerClazz) {
				int mod = cls.getModifiers();
				String modifier = Modifier.toString(mod);
				if (modifier.contains("static")) {
					// 构造静态内部类实例
					Object obj1 = cls.newInstance();
					Field field1 = cls.getDeclaredField("f");
					field1.setAccessible(true);
					System.out.println(field1.get(obj1));

				} else {
					// 构造成员内部类实例
					Constructor con2 = cls.getDeclaredConstructor(clazz);
					con2.setAccessible(true);
					Object obj2 = con2.newInstance(container);
					Field field2 = cls.getDeclaredField("f");
					field2.setAccessible(true);
					System.out.println(field2.get(obj2));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}