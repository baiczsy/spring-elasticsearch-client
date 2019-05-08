package com.github.elasticsearch.client.connection;

import org.apache.http.HttpHost;

/**
 * @author wangl
 * @date 2019-04-30
 */
public interface RestClientConfiguration {

    HttpHost[] getHttpHosts();
}
