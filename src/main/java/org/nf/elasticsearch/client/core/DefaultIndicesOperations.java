package org.nf.elasticsearch.client.core;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;

import java.util.Map;

/**
 * @author wangl
 * @date 2019-05-08
 */
class DefaultIndicesOperations implements IndicesOperations{

    private RestClientTemplate template;

    public DefaultIndicesOperations(RestClientTemplate template) {
        this.template = template;
    }

    @Override
    public CreateIndexResponse create(String index, Map<String, Object> mapping) {
        return create(index, mapping, 0);
    }

    @Override
    public CreateIndexResponse create(String index, Map<String, Object> mapping, long timeout) {
        return template.execute((highLevelClient) -> {
            CreateIndexRequest request = template.getCreateIndexRequest(index, mapping, timeout);
            return highLevelClient.indices().create(request, RequestOptions.DEFAULT);
        });
    }

    @Override
    public void create(String index, Map<String, Object> mapping, ActionListener<CreateIndexResponse> listener) {
        create(index, mapping, listener, 0);
    }

    @Override
    public void create(String index, Map<String, Object> mapping, ActionListener<CreateIndexResponse> listener, long timeout) {
        template.execute((highLevelClient) -> {
            CreateIndexRequest request = template.getCreateIndexRequest(index, mapping, timeout);
            highLevelClient.indices().createAsync(request, RequestOptions.DEFAULT, listener);
        });
    }

    @Override
    public AcknowledgedResponse delete(String index) {
        return delete(index, 0);
    }

    @Override
    public AcknowledgedResponse delete(String index, long timeout) {
        return template.execute((highLevelClient) -> {
            DeleteIndexRequest request = template.getDeleteIndexRequest(index, timeout);
            return highLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        });
    }

    @Override
    public void delete(String index, ActionListener<AcknowledgedResponse> listener) {
        delete(index,listener, 0);
    }

    @Override
    public void delete(String index, ActionListener<AcknowledgedResponse> listener, long timeout) {
        template.execute((highLevelClient) -> {
            DeleteIndexRequest request = template.getDeleteIndexRequest(index, timeout);
            highLevelClient.indices().deleteAsync(request, RequestOptions.DEFAULT, listener);
        });
    }
}
