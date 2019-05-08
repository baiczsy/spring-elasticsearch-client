package org.nf.elasticsearch.client.connection;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author wangl
 * @date 2019-05-08
 */
public class RestClientPoolConfig extends GenericObjectPoolConfig {

    public RestClientPoolConfig() {
        setTestWhileIdle(true);
        setMinEvictableIdleTimeMillis(60000);
        setTimeBetweenEvictionRunsMillis(30000);
        setNumTestsPerEvictionRun(-1);
    }
}
