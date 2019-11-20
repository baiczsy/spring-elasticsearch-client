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
public class RestClientTemplate extends RestClientAccessor implements DocumentOperations {


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
            ElasticsearchClientFactory clientFactory = getRestClientFactory();
            RestHighLevelClient client = clientFactory.getResource();
            T t = callback.doInRestClient(client);
            clientFactory.returnResource(client);
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
    public IndexResponse persist(IndexRequest request) {
        return execute((highLevelClient) -> {
            return highLevelClient.index(request, RequestOptions.DEFAULT);
        });
    }

    @Override
    public void persist(IndexRequest request, ActionListener<IndexResponse> listener) {
        execute((highLevelClient) -> {
            highLevelClient.indexAsync(request, RequestOptions.DEFAULT, listener);
        });
    }

    @Override
    public UpdateResponse update(UpdateRequest request) {
        return execute((highLevelClient) -> {
            return highLevelClient.update(request, RequestOptions.DEFAULT);
        });
    }

    @Override
    public void update(UpdateRequest request, ActionListener<UpdateResponse> listener) {
        execute((highLevelClient) -> {
            highLevelClient.updateAsync(request, RequestOptions.DEFAULT, listener);
        });
    }

    @Override
    public DeleteResponse delete(DeleteRequest request) {
        return execute((highLevelClient) -> {
            return highLevelClient.delete(request, RequestOptions.DEFAULT);
        });
    }

    @Override
    public void delete(DeleteRequest request, ActionListener<DeleteResponse> listener) {
        execute((highLevelClient) -> {
            highLevelClient.deleteAsync(request, RequestOptions.DEFAULT, listener);
        });
    }

    @Override
    public Map<String, Object> get(GetRequest request) {
        return execute((highLevelClient) -> {
            GetResponse response = highLevelClient.get(request, RequestOptions.DEFAULT);
            return response.getSourceAsMap();
        });
    }

    @Override
    public void get(GetRequest request, ActionListener<GetResponse> listener) {
        execute((highLevelClient) -> {
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
