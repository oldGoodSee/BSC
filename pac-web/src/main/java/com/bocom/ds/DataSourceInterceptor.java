package com.bocom.ds;

import org.aspectj.lang.JoinPoint;

/**
 * ClassName:DataSourceInterceptor
 * Function: 
 * Date:     2017年8月7日上午10:53:56
 * @author   chenzz
 * @since    JDK 1.7
 */
public class DataSourceInterceptor {
	public void setDataSource1(JoinPoint jp) {
		DataSourceContextHolder.set("dataSource");
	}

	public void setDataSource2(JoinPoint jp) {
		DataSourceContextHolder.set("dataSource2");
	}
}
