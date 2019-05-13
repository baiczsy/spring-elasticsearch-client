package com.github.elasticsearch.client.connection;

import org.apache.http.HttpHost;

/**
 * @author wangl
 * @date 2019-04-30
 */
public abstract class RestClientConfiguration {

    private int connectTimeout = -1;
    private int connectionRequestTimeout = -1;
    private int socketTimeout = -1;
    private int maxConnTotal = 0;
    private int maxConnPerRoute = 0;

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getMaxConnTotal() {
        return maxConnTotal;
    }

    public void setMaxConnTotal(int maxConnTotal) {
        this.maxConnTotal = maxConnTotal;
    }

    public int getMaxConnPerRoute() {
        return maxConnPerRoute;
    }

    public void setMaxConnPerRoute(int maxConnPerRoute) {
        this.maxConnPerRoute = maxConnPerRoute;
    }

    abstract HttpHost[] getHttpHosts();
}
