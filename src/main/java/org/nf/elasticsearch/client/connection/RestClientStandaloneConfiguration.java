package org.nf.elasticsearch.client.connection;

import org.apache.http.HttpHost;

/**
 * @author wangl
 * @date 2019-04-30
 */
public class RestClientStandaloneConfiguration implements RestClientConfiguration{

    private String host;
    private Integer port;

    public RestClientStandaloneConfiguration(){
    }

    public RestClientStandaloneConfiguration(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public HttpHost[] getHttpHosts() {
        return new HttpHost[]{new HttpHost(host, port, "http")};
    }
}
