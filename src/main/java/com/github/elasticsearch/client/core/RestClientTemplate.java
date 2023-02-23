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
public class RestClientTemplate extends RestClientAccessor {


    private QueryOperations queryOperations;
    private IndicesOperations indicesOperations;
    private DocumentOperations documentOperations;

    public RestClientTemplate(ElasticsearchClientFactory restClientFactory) {
        setConnectionFactory(restClientFactory);
    }

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

    public void execute(RestClientAsyncCallback callback) {
        try {
            RestHighLevelClient client = getRestClientFactory().getResource();
            callback.doInRestAsyncClient(client);
            getRestClientFactory().returnResource(client);
        } catch (IOException e) {
            throw new ElasticsearchAccessException(e);
        }
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

    public DocumentOperations opsForDocument(){
        if(documentOperations == null){
            documentOperations = new DefaultDocumentOperations(this);
        }
        return documentOperations;
    }

}
