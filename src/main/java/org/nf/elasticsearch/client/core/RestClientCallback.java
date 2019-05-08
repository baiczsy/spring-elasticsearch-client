package org.nf.elasticsearch.client.core;

import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * @author wangl
 * @date 2019-04-30
 */
public interface RestClientCallback<T> {

    T doInRestClient(RestHighLevelClient client) throws IOException;
}
