package com.bootdo.common.datasoures;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 
    * @ClassName: DynamicDataSource
    * @Description: TODO(多数据源配置)
    * @author Andrew
    * @date 2018年5月8日
    *
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }
    
}