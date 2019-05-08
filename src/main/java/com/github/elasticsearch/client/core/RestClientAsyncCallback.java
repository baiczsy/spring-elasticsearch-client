package com.github.elasticsearch.client.core;

import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * @author wangl
 * @date 2019-05-02
 */
public interface RestClientAsyncCallback {

    void doInRestAsyncClient(RestHighLevelClient client) throws IOException;
}
