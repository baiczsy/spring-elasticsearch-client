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

import java.util.Map;

/**
 * @author wangl
 * @date 2019-04-30
 */
public interface DocumentOperations {

    <T> T execute(RestClientCallback<T> callback);

    void execute(RestClientAsyncCallback callback);

    IndexResponse persist(IndexRequest request);

    void persist(IndexRequest request, ActionListener<IndexResponse> listener);

    UpdateResponse update(UpdateRequest request);

    void update(UpdateRequest request, ActionListener<UpdateResponse> listener);

    DeleteResponse delete(DeleteRequest request);

    void delete(DeleteRequest request, ActionListener<DeleteResponse> listener);

    Map<String, Object> get(GetRequest request);

    void get(GetRequest request, ActionListener<GetResponse> listener);
}
