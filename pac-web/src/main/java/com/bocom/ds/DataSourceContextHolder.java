package com.bocom.ds;

/**
 * ClassName:DataSourceContextHolder
 * Function: 
 * Date:     2017年8月7日上午10:51:37
 * @author   chenzz
 * @since    JDK 1.7
 */
public class DataSourceContextHolder {

	private static final ThreadLocal<String> dataSources = new ThreadLocal<String>();

	public static void set(String customerType) {
		dataSources.set(customerType);
	}

	public static String get() {
		return dataSources.get();
	}

	public static void clear() {
		dataSources.remove();
	}
}
