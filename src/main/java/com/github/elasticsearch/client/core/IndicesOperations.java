package com.github.elasticsearch.client.core;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.indices.CreateIndexResponse;

import java.util.Map;

/**
 * @author wangl
 * @date 2019-05-08
 */
public interface IndicesOperations {

    CreateIndexResponse create(String index, Map<String, Object> mapping);

    CreateIndexResponse create(String index, Map<String, Object> mapping, long timeout);

    void create(String index, Map<String, Object> mapping, ActionListener<CreateIndexResponse> listener);

    void create(String index, Map<String, Object> mapping, ActionListener<CreateIndexResponse> listener, long timeout);

    AcknowledgedResponse delete(String index);

    AcknowledgedResponse delete(String index, long timeout);

    void delete(String index, ActionListener<AcknowledgedResponse> listener);

    void delete(String index, ActionListener<AcknowledgedResponse> listener, long timeout);
}
