package com.github.elasticsearch.client.core;

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
    public CreateIndexResponse create(CreateIndexRequest request) {
        return template.execute((highLevelClient) -> {
            return highLevelClient.indices().create(request, RequestOptions.DEFAULT);
        });
    }

    @Override
    public void create(CreateIndexRequest request, ActionListener<CreateIndexResponse> listener) {
        template.execute((highLevelClient) -> {
            highLevelClient.indices().createAsync(request, RequestOptions.DEFAULT, listener);
        });
    }

    @Override
    public AcknowledgedResponse delete(DeleteIndexRequest request) {
        return template.execute((highLevelClient) -> {
            return highLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        });
    }

    @Override
    public void delete(DeleteIndexRequest request, ActionListener<AcknowledgedResponse> listener) {
        template.execute((highLevelClient) -> {
            highLevelClient.indices().deleteAsync(request, RequestOptions.DEFAULT, listener);
        });
    }
}
