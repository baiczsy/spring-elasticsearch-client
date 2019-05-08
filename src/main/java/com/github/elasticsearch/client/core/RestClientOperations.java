package com.github.elasticsearch.client.core;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;

import java.util.Map;

/**
 * @author wangl
 * @date 2019-04-30
 */
public interface RestClientOperations {

    <T> T execute(RestClientCallback<T> callback);

    void execute(RestClientAsyncCallback callback);

    IndexResponse persist(String index, String id, Map<String, Object> params);

    IndexResponse persist(String index, String id, Map<String, Object> params, long timeout);

    void persist(String index, String id, Map<String, Object> params, ActionListener<IndexResponse> listener);

    void persist(String index, String id, Map<String, Object> params, ActionListener<IndexResponse> listener, long timeout);

    UpdateResponse update(String index, String id, Map<String, Object> params);

    UpdateResponse update(String index, String id, Map<String, Object> params, long timeout);

    void update(String index, String id, Map<String, Object> params, ActionListener<UpdateResponse> listener);

    void update(String index, String id, Map<String, Object> params, ActionListener<UpdateResponse> listener, long timeout);

    DeleteResponse delete(String index, String id);

    DeleteResponse delete(String index, String id, long timeout);

    void delete(String index, String id, ActionListener<DeleteResponse> listener);

    void delete(String index, String id, ActionListener<DeleteResponse> listener, long timeout);

    Map<String, Object> get(String index, String id);

    void get(String index, String id, ActionListener<GetResponse> listener);
}
