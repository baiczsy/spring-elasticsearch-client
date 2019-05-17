package com.github.elasticsearch.client.core;

import com.github.elasticsearch.client.connection.ElasticsearchClientFactory;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.unit.TimeValue;

import java.util.Map;

/**
 * @author wangl
 * @date 2019-04-30
 */
public class RestClientAccessor {

    private ElasticsearchClientFactory restClientFactory;

    public RestClientAccessor(){
    }

    public RestClientAccessor(ElasticsearchClientFactory restClientFactory){
        this.restClientFactory = restClientFactory;
    }

    public void setConnectionFactory(ElasticsearchClientFactory restClientFactory) {
        this.restClientFactory = restClientFactory;
    }

    public ElasticsearchClientFactory getRestClientFactory() {
        if (restClientFactory == null) {
            throw new IllegalStateException("RestHighLevelClientFactory is required");
        }
        return restClientFactory;
    }
}
