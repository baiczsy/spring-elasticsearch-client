package com.github.elasticsearch.client.core;

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

import java.util.Map;

/**
 * @author wangl
 * @date 2023/2/23
 */
public class DefaultDocumentOperations implements DocumentOperations {

    private RestClientTemplate template;

    public DefaultDocumentOperations(RestClientTemplate template) {
        this.template = template;
    }

    @Override
    public IndexResponse persist(IndexRequest request) {
        return template.execute((highLevelClient) -> {
            return highLevelClient.index(request, RequestOptions.DEFAULT);
        });
    }

    @Override
    public void persist(IndexRequest request, ActionListener<IndexResponse> listener) {
        template.execute((highLevelClient) -> {
            highLevelClient.indexAsync(request, RequestOptions.DEFAULT, listener);
        });
    }

    @Override
    public UpdateResponse update(UpdateRequest request) {
        return template.execute((highLevelClient) -> {
            return highLevelClient.update(request, RequestOptions.DEFAULT);
        });
    }

    @Override
    public void update(UpdateRequest request, ActionListener<UpdateResponse> listener) {
        template.execute((highLevelClient) -> {
            highLevelClient.updateAsync(request, RequestOptions.DEFAULT, listener);
        });
    }

    @Override
    public DeleteResponse delete(DeleteRequest request) {
        return template.execute((highLevelClient) -> {
            return highLevelClient.delete(request, RequestOptions.DEFAULT);
        });
    }

    @Override
    public void delete(DeleteRequest request, ActionListener<DeleteResponse> listener) {
        template.execute((highLevelClient) -> {
            highLevelClient.deleteAsync(request, RequestOptions.DEFAULT, listener);
        });
    }

    @Override
    public Map<String, Object> get(GetRequest request) {
        return template.execute((highLevelClient) -> {
            GetResponse response = highLevelClient.get(request, RequestOptions.DEFAULT);
            return response.getSourceAsMap();
        });
    }

    @Override
    public void get(GetRequest request, ActionListener<GetResponse> listener) {
        template.execute((highLevelClient) -> {
            highLevelClient.getAsync(request, RequestOptions.DEFAULT, listener);
        });
    }
}
