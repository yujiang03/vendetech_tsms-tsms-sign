package com.vendetech.project.dingtalk.utils;

/**
 * 三目运算返回 判断对象是否为空或null
 */
public class ObjectUtils {

	public static boolean isNull(Object obj) {
		return obj == null;
	}

	public static boolean isNotNull(Object obj) {
		return !isNull(obj);
	}

	public static boolean isEmpty(Object obj) {
		if (obj != null && !"".equals(obj) && !"null".equals(obj)) {
			return true;
		}
		return false;
	}

	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	public static String ternary(Object obj) {
		if (isEmpty(obj)) {
			return obj.toString();
		}
		return null;
	}

	public static Double doubleTernary(Object obj) {
		if (isEmpty(obj)) {
			return Double.valueOf(obj.toString());
		}
		return null;
	}

	public static Integer integerTernary(Object obj) {
		if (isEmpty(obj)) {
			return Integer.valueOf(obj.toString());
		}
		return null;
	}
}
