package com.github.elasticsearch.client.core;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wangl
 * @date 2019-05-01
 */
class DefaultQueryOperations implements QueryOperations {

    private RestClientTemplate template;
    private SearchSourceBuilder builder;

    public DefaultQueryOperations(RestClientTemplate template, SearchSourceBuilder builder){
        this.template = template;
        this.builder = builder;
    }

    DefaultQueryOperations setSearchSourceBuilder(SearchSourceBuilder builder){
        this.builder = builder;
        return this;
    }

    @Override
    public List<Map<String, Object>> match(String index, String field, String param) {
        return template.execute((highLevelClient) -> {
            builder.query(QueryBuilders.matchQuery(field, param));
            return executeQuery(index, highLevelClient);
        });
    }

    @Override
    public void match(String index, String field, String param, ActionListener<SearchResponse> listener) {
        template.execute((highLevelClient) -> {
            builder.query(QueryBuilders.matchQuery(field, param));
            executeAsyncQuery(index, highLevelClient, listener);
        });
    }

    @Override
    public List<Map<String, Object>> matchAll(String index) {
        return template.execute((highLevelClient) -> {
            builder.query(QueryBuilders.matchAllQuery());
            return executeQuery(index, highLevelClient);
        });
    }

    @Override
    public void matchAll(String index, ActionListener<SearchResponse> listener) {
        template.execute((highLevelClient) -> {
            builder.query(QueryBuilders.matchAllQuery());
            executeAsyncQuery(index, highLevelClient, listener);
        });
    }

    @Override
    public List<Map<String, Object>> multiMatch(String index, String param, String[] fields) {
        return template.execute((highLevelClient) -> {
            builder.query(QueryBuilders.multiMatchQuery(param, fields));
            return executeQuery(index, highLevelClient);
        });
    }

    @Override
    public void multiMatch(String index, String param, String[] fields, ActionListener<SearchResponse> listener) {
        template.execute((highLevelClient) -> {
            builder.query(QueryBuilders.multiMatchQuery(param, fields));
            executeAsyncQuery(index, highLevelClient, listener);
        });
    }

    @Override
    public List<Map<String, Object>> term(String index, String field, Object param) {
        return template.execute((highLevelClient) -> {
            builder.query(QueryBuilders.termQuery(field, param));
            return executeQuery(index, highLevelClient);
        });
    }

    @Override
    public void term(String index, String field, Object param, ActionListener<SearchResponse> listener) {
        template.execute((highLevelClient) -> {
            builder.query(QueryBuilders.termQuery(field, param));
            executeAsyncQuery(index, highLevelClient, listener);
        });
    }

    @Override
    public List<Map<String, Object>> terms(String index, String field, Object... param) {
        return template.execute((highLevelClient) -> {
            builder.query(QueryBuilders.termsQuery(field, param));
            return executeQuery(index, highLevelClient);
        });
    }

    @Override
    public void terms(String index, String field, ActionListener<SearchResponse> listener, Object... param) {
        template.execute((highLevelClient) -> {
            builder.query(QueryBuilders.termsQuery(field, param));
            executeAsyncQuery(index, highLevelClient, listener);
        });
    }

    private List<Map<String, Object>> executeQuery(String index, RestHighLevelClient client) throws IOException {
        SearchRequest request = new SearchRequest(index);
        request.source(builder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : hits) {
            list.add(hit.getSourceAsMap());
        }
        return list;
    }

    private void executeAsyncQuery(String index, RestHighLevelClient client, ActionListener<SearchResponse> listener) {
        SearchRequest request = new SearchRequest(index);
        request.source(builder);
        client.searchAsync(request, RequestOptions.DEFAULT, listener);
    }

    @Override
    public QueryOperations timeout(long timeout){
        builder.timeout(TimeValue.timeValueNanos(timeout));
        return this;
    }

    @Override
    public QueryOperations from(int from){
        builder.from(from);
        return this;
    }

    @Override
    public QueryOperations size(int size){
        builder.size(size);
        return this;
    }

    @Override
    public QueryOperations sort(String field, SortOrder order){
        builder.sort(field, order);
        return this;
    }

}
