package com.bocom.ds;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * ClassName:DynamicDataSource
 * Function: 
 * Date:     2017年8月7日上午10:51:10
 * @author   chenzz
 * @since    JDK 1.7
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceContextHolder.get();
	}

}
