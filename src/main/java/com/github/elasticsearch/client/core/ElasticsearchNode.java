package com.github.elasticsearch.client.core;

/**
 * @author wangl
 * @date 2019-04-30
 */
public class ElasticsearchNode {

    private String host;
    private Integer port;

    public ElasticsearchNode() {
    }

    public ElasticsearchNode(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
