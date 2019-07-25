package com.ares.common;

import java.math.BigDecimal;

/**
 * 该工具类提供精 确的浮点数运算，包括加减乘除和四舍五入<br>
 * 只支持小数点后四舍五入
 */
public class ArithUtil {
	private static final int add = 1;
	private static final int sub = 2;
	private static final int mul = 3;
	private static final int div = 4;

	// 这个类不能实例化
	private ArithUtil() {
	}

	public static void main(String[] args) {
		double a = (252.0 + 252 / 15 * 0) / 1000 * 1 * 1;
		double b = 252 / 15 * 0;
		double c = divDouble(252, 1000);
		System.out.println(a);
		System.out.println(b);
		System.out.println(c);

		int a1 = 139;
		float b1 = 15.0002f;

		float divFloat = divFloat(a1, b1);
		System.err.println(divFloat);
	}

	/**
	 * 精确乘法运算
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回int型
	 */
	public static int mulInt(Object v1, Object v2) {
		return op(v1, v2, mul).intValue();
	}

	/**
	 * 精确乘法运算
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回long型
	 */
	public static long mulLong(Object v1, Object v2) {
		return op(v1, v2, mul).longValue();
	}

	/**
	 * 精确乘法运算
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回double型
	 */
	public static double mulDouble(Object v1, Object v2) {
		return op(v1, v2, mul).doubleValue();
	}

	/**
	 * 精确乘法运算
	 * 
	 * @param v1
	 * @param v2
	 * @param scale 需要指定保留小数点后几位
	 * @return 返回double型
	 */
	public static double mulDouble(Object v1, Object v2, int scale) {
		return op(v1, v2, mul).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 精确乘法运算
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回float型
	 */
	public static float mulFloat(Object v1, Object v2) {
		return op(v1, v2, mul).floatValue();
	}

	/**
	 * 精确乘法运算
	 * 
	 * @param v1
	 * @param v2
	 * @param scale 需要指定保留小数点后几位
	 * @return 返回float型
	 */
	public static float mulFloat(Object v1, Object v2, int scale) {
		return op(v1, v2, mul).setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * 精确除法运算（默认保留两位）
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回int型
	 */
	public static int divInt(Object v1, Object v2) {
		return op(v1, v2, div).intValue();
	}

	/**
	 * 精确除法运算（默认保留两位）
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回long型
	 */
	public static long divLong(Object v1, Object v2) {
		return op(v1, v2, div).longValue();
	}

	/**
	 * 精确除法运算（默认保留两位）
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回double型
	 */
	public static double divDouble(Object v1, Object v2) {
		return op(v1, v2, div).doubleValue();
	}

	/**
	 * 精确除法运算
	 * 
	 * @param v1
	 * @param v2
	 * @param scale 需要指定保留小数点后几位
	 * @return 返回double型
	 */
	public static double divDouble(Object v1, Object v2, int scale) {
		return op(v1, v2, div).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 精确除法运算（默认保留两位）
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回float型
	 */
	public static float divFloat(Object v1, Object v2) {
		return op(v1, v2, div).floatValue();
	}

	/**
	 * 精确除法运算
	 * 
	 * @param v1
	 * @param v2
	 * @param scale 需要指定保留小数点后几位
	 * @return 返回float型
	 */
	public static float divFloat(Object v1, Object v2, int scale) {
		return op(v1, v2, div).setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * 精确加法运算
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回int型
	 */
	public static int addInt(Object v1, Object v2) {
		return op(v1, v2, add).intValue();
	}

	/**
	 * 精确加法运算
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回long型
	 */
	public static long addLong(Object v1, Object v2) {
		return op(v1, v2, add).longValue();
	}

	/**
	 * 精确加法运算
	 * 
	 * @param v1
	 * @param v2
	 * @param scale 需要指定保留小数点后几位
	 * @return 返回double型
	 */
	public static double addDouble(Object v1, Object v2) {
		return op(v1, v2, add).doubleValue();
	}

	/**
	 * 精确加法运算
	 * 
	 * @param v1
	 * @param v2
	 * @param scale 需要指定保留小数点后几位
	 * @return 返回double型
	 */
	public static double addDouble(Object v1, Object v2, int scale) {
		return op(v1, v2, add).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 精确加法运算
	 * 
	 * @param v1
	 * @param v2
	 * @param scale 需要指定保留小数点后几位
	 * @return 返回float型
	 */
	public static float addFloat(Object v1, Object v2) {
		return op(v1, v2, add).floatValue();
	}

	/**
	 * 精确加法运算
	 * 
	 * @param v1
	 * @param v2
	 * @param scale 需要指定保留小数点后几位
	 * @return 返回float型
	 */
	public static float addFloat(Object v1, Object v2, int scale) {
		return op(v1, v2, add).setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * 精确减法运算
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回int型
	 */
	public static int subInt(Object v1, Object v2) {
		return op(v1, v2, sub).intValue();
	}

	/**
	 * 精确减法运算
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回long型
	 */
	public static long subLong(Object v1, Object v2) {
		return op(v1, v2, sub).longValue();
	}

	/**
	 * 精确减法运算
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回double型
	 */
	public static double subDouble(Object v1, Object v2) {
		return op(v1, v2, sub).doubleValue();
	}

	/**
	 * 精确减法运算
	 * 
	 * @param v1
	 * @param v2
	 * @param scale 需要指定保留小数点后几位
	 * @return 返回double型
	 */
	public static double subDouble(Object v1, Object v2, int scale) {
		return op(v1, v2, sub).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 精确减法运算
	 * 
	 * @param v1
	 * @param v2
	 * @return 返回float型
	 */
	public static float subFloat(Object v1, Object v2) {
		return op(v1, v2, sub).floatValue();
	}

	/**
	 * 精确减法运算
	 * 
	 * @param v1
	 * @param v2
	 * @param scale 需要指定保留小数点后几位
	 * @return 返回float型
	 */
	public static float subFloat(Object v1, Object v2, int scale) {
		return op(v1, v2, sub).setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	private static BigDecimal op(Object v1, Object v2, int operate) {
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());

		switch (operate) {
		case add:
			return b1.add(b2);
		case sub:
			return b1.subtract(b2);
		case mul:
			return b1.multiply(b2);
		case div:
			return b1.divide(b2, 10, BigDecimal.ROUND_HALF_UP);
		}
		return b2;
	}

//	private static String parseString(Object v) {
//		if (v instanceof Short) {
//			return Short.toString((short) v);
//		} else if (v instanceof Integer) {
//			return Integer.toString((int) v);
//		} else if (v instanceof Long) {
//			return Long.toString((long) v);
//		} else if (v instanceof Double) {
//			return Double.toString((double) v);
//		} else if (v instanceof Float) {
//			return Float.toString((float) v);
//		}
//		return null;
//	}
}
