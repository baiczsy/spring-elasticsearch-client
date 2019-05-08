package com.github.elasticsearch.client.core;

import com.github.elasticsearch.client.connection.ElasticsearchClientFactory;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Map;

/**
 * @author wangl
 * @date 2019-04-30
 */
public class RestClientTemplate extends RestClientAccessor implements RestClientOperations {


    private QueryOperations queryOperations;
    private IndicesOperations indicesOperations;

    public RestClientTemplate() {
    }

    public RestClientTemplate(ElasticsearchClientFactory restClientFactory) {
        setConnectionFactory(restClientFactory);
    }

    @Override
    public <T> T execute(RestClientCallback<T> callback) {
        try {
            RestHighLevelClient client = getRestClientFactory().getResource();
            T t = callback.doInRestClient(client);
            getRestClientFactory().returnResource(client);
            return t;
        } catch (IOException e) {
            throw new ElasticsearchAccessException(e);
        }
    }

    @Override
    public void execute(RestClientAsyncCallback callback) {
        try {
            RestHighLevelClient client = getRestClientFactory().getResource();
            callback.doInRestAsyncClient(client);
            getRestClientFactory().returnResource(client);
        } catch (IOException e) {
            throw new ElasticsearchAccessException(e);
        }
    }

    @Override
    public IndexResponse persist(String index, String id, Map<String, Object> params) {
        return persist(index, id, params, 0);
    }

    @Override
    public IndexResponse persist(String index, String id, Map<String, Object> params, long timeout) {
        return execute((highLevelClient) -> {
            IndexRequest request = getIndexRequest(index, id, params, timeout);
            return highLevelClient.index(request, RequestOptions.DEFAULT);
        });
    }

    @Override
    public void persist(String index, String id, Map<String, Object> params, ActionListener<IndexResponse> listener) {
        persist(index, id, params, listener, 0);
    }

    @Override
    public void persist(String index, String id, Map<String, Object> params, ActionListener<IndexResponse> listener, long timeout) {
        execute((highLevelClient) -> {
            IndexRequest request = getIndexRequest(index, id, params, timeout);
            highLevelClient.indexAsync(request, RequestOptions.DEFAULT, listener);
        });
    }

    @Override
    public UpdateResponse update(String index, String id, Map<String, Object> params) {
        return update(index, id, params, 0);
    }

    @Override
    public UpdateResponse update(String index, String id, Map<String, Object> params, long timeout) {
        return execute((highLevelClient) -> {
            UpdateRequest request = getUpdateRequest(index, id, params, timeout);
            return highLevelClient.update(request, RequestOptions.DEFAULT);
        });
    }

    @Override
    public void update(String index, String id, Map<String, Object> params, ActionListener<UpdateResponse> listener) {
        update(index, id, params, listener, 0);
    }

    @Override
    public void update(String index, String id, Map<String, Object> params, ActionListener<UpdateResponse> listener, long timeout) {
        execute((highLevelClient) -> {
            UpdateRequest request = getUpdateRequest(index, id, params, timeout);
            highLevelClient.updateAsync(request, RequestOptions.DEFAULT, listener);
        });
    }

    @Override
    public DeleteResponse delete(String index, String id) {
        return delete(index, id, 0);
    }

    @Override
    public DeleteResponse delete(String index, String id, long timeout) {
        return execute((highLevelClient) -> {
            DeleteRequest request = getDeleteRequest(index, id, timeout);
            return highLevelClient.delete(request, RequestOptions.DEFAULT);
        });
    }

    @Override
    public void delete(String index, String id, ActionListener<DeleteResponse> listener) {
        delete(index, id, listener, 0);
    }

    @Override
    public void delete(String index, String id, ActionListener<DeleteResponse> listener, long timeout) {
        execute((highLevelClient) -> {
            DeleteRequest request = getDeleteRequest(index, id, timeout);
            highLevelClient.deleteAsync(request, RequestOptions.DEFAULT, listener);
        });
    }

    @Override
    public Map<String, Object> get(String index, String id) {
        return execute((highLevelClient) -> {
            GetRequest request = getGetRequest(index, id);
            GetResponse response = highLevelClient.get(request, RequestOptions.DEFAULT);
            return response.getSourceAsMap();
        });
    }

    @Override
    public void get(String index, String id, ActionListener<GetResponse> listener) {
        execute((highLevelClient) -> {
            GetRequest request = getGetRequest(index, id);
            highLevelClient.getAsync(request, RequestOptions.DEFAULT, listener);
        });
    }

    public QueryOperations opsForQuery(){
        if(queryOperations == null){
            queryOperations = new DefaultQueryOperations(this, new SearchSourceBuilder());
        } else {
            ((DefaultQueryOperations)queryOperations).setSearchSourceBuilder(new SearchSourceBuilder());
        }
        return queryOperations;
    }

    public IndicesOperations opsForIndices(){
        if(indicesOperations == null){
            indicesOperations = new DefaultIndicesOperations(this);
        }
        return indicesOperations;
    }

}
