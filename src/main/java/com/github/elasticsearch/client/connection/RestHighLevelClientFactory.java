package com.github.elasticsearch.client.connection;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wangl
 * @date 2019-05-08
 */
public class RestHighLevelClientFactory implements PooledObjectFactory<RestHighLevelClient> {

    private RestClientBuilder builder;
    private RestClientConfiguration configuration;

    public RestHighLevelClientFactory(RestClientConfiguration configuration, Map<String, String> defaultHeaders) {
        this.configuration = configuration;
        this.builder = RestClient.builder(configuration.getHttpHosts());
        customizeRequestConfig();
        customizeHttpClient();
        if(defaultHeaders != null){
            builder.setDefaultHeaders(buildDefaultHeaders(defaultHeaders));
        }
    }

    @Override
    public PooledObject<RestHighLevelClient> makeObject() throws Exception {
        return new DefaultPooledObject(new RestHighLevelClient(builder));
    }

    @Override
    public void destroyObject(PooledObject<RestHighLevelClient> p) throws Exception {
        p.getObject().close();
    }

    @Override
    public boolean validateObject(PooledObject<RestHighLevelClient> p) {
        RestHighLevelClient client = p.getObject();
        try {
            return client.ping(RequestOptions.DEFAULT);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void activateObject(PooledObject<RestHighLevelClient> p) throws Exception {
        // TODO: code to be written
    }

    @Override
    public void passivateObject(PooledObject<RestHighLevelClient> p) throws Exception {
        // TODO: code to be written
    }

    private Header[] buildDefaultHeaders(Map<String, String> defaultHeaders){
        List<Header> headers = new ArrayList<>();
        for(String key : defaultHeaders.keySet()){
            Header header = new BasicHeader(key, defaultHeaders.get(key));
            headers.add(header);
        }
        return headers.toArray(new Header[headers.size()]);
    }

    private void customizeRequestConfig(){
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(configuration.getConnectTimeout());
            requestConfigBuilder.setConnectionRequestTimeout(configuration.getConnectionRequestTimeout());
            requestConfigBuilder.setSocketTimeout(configuration.getSocketTimeout());
            return requestConfigBuilder;
        });
    }

    private void customizeHttpClient(){
        builder.setHttpClientConfigCallback(httpAsyncClientBuilder -> {
            httpAsyncClientBuilder.setMaxConnTotal(configuration.getMaxConnTotal());
            httpAsyncClientBuilder.setMaxConnPerRoute(configuration.getMaxConnPerRoute());
            return httpAsyncClientBuilder;
        });
    }
}
