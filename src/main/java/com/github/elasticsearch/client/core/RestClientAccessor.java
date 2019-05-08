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

    protected CreateIndexRequest getCreateIndexRequest(String index, Map<String, Object> mapping, long timeout){
        CreateIndexRequest request = new CreateIndexRequest(index);
        request.mapping(mapping);
        if(timeout != 0){
            request.setTimeout(TimeValue.timeValueNanos(timeout));
        }
        return request;
    }

    protected DeleteIndexRequest getDeleteIndexRequest(String index, long timeout){
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        if(timeout != 0){
            request.timeout(TimeValue.timeValueNanos(timeout));
        }
        return request;
    }

    protected IndexRequest getIndexRequest(String index, String id, Map<String, Object> params, long timeout){
        IndexRequest request = new IndexRequest(index).id(id).source(params);
        if(timeout != 0){
            request.timeout(TimeValue.timeValueNanos(timeout));
        }
        return request;
    }

    protected UpdateRequest getUpdateRequest(String index, String id, Map<String, Object> params, long timeout){
        UpdateRequest request = new UpdateRequest(index, id).doc(params);
        if(timeout != 0){
            request.timeout(TimeValue.timeValueNanos(timeout));
        }
        return request;
    }

    protected DeleteRequest getDeleteRequest(String index, String id, long timeout){
        DeleteRequest request = new DeleteRequest(index, id);
        if(timeout != 0){
            request.timeout(TimeValue.timeValueNanos(timeout));
        }
        return request;
    }

    protected GetRequest getGetRequest(String index, String id){
        GetRequest request = new GetRequest(index, id);
        return request;
    }
}
