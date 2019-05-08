package org.nf.elasticsearch.client.core;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;
import java.util.Map;

/**
 * @author wangl
 * @date 2019-05-01
 */
public interface QueryOperations {

    List<Map<String, Object>> match(String index, String field, String param);

    void match(String index, String field, String param, ActionListener<SearchResponse> listener);

    List<Map<String, Object>> matchAll(String index);

    void matchAll(String index, ActionListener<SearchResponse> listener);

    List<Map<String, Object>> multiMatch(String index, String param, String[] fields);

    void multiMatch(String index, String param, String[] fields, ActionListener<SearchResponse> listener);

    List<Map<String, Object>> term(String index, String field, Object param);

    void term(String index, String field, Object param, ActionListener<SearchResponse> listener);

    List<Map<String, Object>> terms(String index, String field, Object... param);

    void terms(String index, String field, ActionListener<SearchResponse> listener, Object... param);

    QueryOperations timeout(long timeout);

    QueryOperations from(int from);

    QueryOperations size(int size);

    QueryOperations sort(String field, SortOrder order);
}
