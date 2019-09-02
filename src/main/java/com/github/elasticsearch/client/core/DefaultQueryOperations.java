package com.github.elasticsearch.client.core;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
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
    private HighlightBuilder highlightBuilder;

    public DefaultQueryOperations(RestClientTemplate template, SearchSourceBuilder builder) {
        this.template = template;
        this.builder = builder;
    }

    DefaultQueryOperations setSearchSourceBuilder(SearchSourceBuilder builder) {
        this.builder = builder;
        return this;
    }

    @Override
    public List<Map<String, Object>> match(String index, MatchQueryBuilder queryBuilder) {
        return execute(index, queryBuilder);
    }

    @Override
    public void match(String index, MatchQueryBuilder queryBuilder, ActionListener<SearchResponse> listener) {
        asyncExecute(index, queryBuilder, listener);
    }

    @Override
    public List<Map<String, Object>> matchAll(String index, MatchAllQueryBuilder queryBuilder) {
        return execute(index, queryBuilder);
    }

    @Override
    public void matchAll(String index, MatchAllQueryBuilder queryBuilder, ActionListener<SearchResponse> listener) {
        asyncExecute(index, queryBuilder, listener);
    }

    @Override
    public List<Map<String, Object>> multiMatch(String index, MultiMatchQueryBuilder queryBuilder) {
        return execute(index, queryBuilder);
    }

    @Override
    public void multiMatch(String index, MultiMatchQueryBuilder queryBuilder, ActionListener<SearchResponse> listener) {
        asyncExecute(index, queryBuilder, listener);
    }

    @Override
    public List<Map<String, Object>> term(String index, TermQueryBuilder queryBuilder) {
        return execute(index, queryBuilder);
    }

    @Override
    public void term(String index, TermQueryBuilder queryBuilder, ActionListener<SearchResponse> listener) {
        asyncExecute(index, queryBuilder, listener);
    }

    @Override
    public List<Map<String, Object>> terms(String index, TermsQueryBuilder queryBuilder) {
        return execute(index, queryBuilder);
    }

    @Override
    public void terms(String index, TermsQueryBuilder queryBuilder, ActionListener<SearchResponse> listener, Object... param) {
        asyncExecute(index, queryBuilder, listener);
    }

    @Override
    public List<Map<String, Object>> range(String index, RangeQueryBuilder queryBuilder) {
        return execute(index, queryBuilder);
    }

    @Override
    public void range(String index, RangeQueryBuilder queryBuilder, ActionListener<SearchResponse> listener) {
        asyncExecute(index, queryBuilder, listener);
    }

    @Override
    public List<Map<String, Object>> bool(String index, BoolQueryBuilder queryBuilder) {
        return execute(index, queryBuilder);
    }

    @Override
    public void bool(String index, BoolQueryBuilder queryBuilder, ActionListener<SearchResponse> listener) {
        asyncExecute(index, queryBuilder, listener);
    }

    private List<Map<String, Object>> execute(String index, QueryBuilder queryBuilder) {
        return template.execute((highLevelClient) -> {
            builder.query(queryBuilder);
            return executeQuery(index, highLevelClient);
        });
    }

    private void asyncExecute(String index, QueryBuilder queryBuilder, ActionListener<SearchResponse> listener) {
        template.execute((highLevelClient) -> {
            builder.query(queryBuilder);
            executeAsyncQuery(index, highLevelClient, listener);
        });
    }

    private List<Map<String, Object>> executeQuery(String index, RestHighLevelClient client) throws IOException {
        List<Map<String, Object>> listResult = new ArrayList<>();
        SearchRequest request = new SearchRequest(index);
        if (highlightBuilder != null) {
            builder.highlighter(highlightBuilder);
            SearchHit[] hits = search(request, client);
            for (SearchHit hit : hits) {
                resolveHighlightFields(hit);
                listResult.add(hit.getSourceAsMap());
            }
        } else {
            SearchHit[] hits = search(request, client);
            for (SearchHit hit : hits) {
                listResult.add(hit.getSourceAsMap());
            }
        }
        return listResult;
    }

    private void resolveHighlightFields(SearchHit hit){
        Map<String, HighlightField> highlightFields = hit.getHighlightFields();
        for (Map.Entry<String, HighlightField> entry : highlightFields.entrySet()) {
            HighlightField highlight = highlightFields.get(entry.getKey());
            if (highlight != null) {
                Text[] fragments = highlight.fragments();
                String fragmentString = fragments[0].string();
                hit.getSourceAsMap().put(entry.getKey(), fragmentString);
            }
        }
    }

    private SearchHit[] search(SearchRequest request, RestHighLevelClient client) throws IOException {
        request.source(builder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        return hits;
    }

    private void executeAsyncQuery(String index, RestHighLevelClient client, ActionListener<SearchResponse> listener) {
        SearchRequest request = new SearchRequest(index);
        if (highlightBuilder != null) {
            builder.highlighter(highlightBuilder);
        }
        request.source(builder);
        client.searchAsync(request, RequestOptions.DEFAULT, listener);
    }

    @Override
    public QueryOperations highlight(HighlightBuilder highlightBuilder) {
        this.highlightBuilder = highlightBuilder;
        return this;
    }

    @Override
    public QueryOperations timeout(long timeout) {
        builder.timeout(TimeValue.timeValueNanos(timeout));
        return this;
    }

    @Override
    public QueryOperations from(int from) {
        builder.from(from);
        return this;
    }

    @Override
    public QueryOperations size(int size) {
        builder.size(size);
        return this;
    }

    @Override
    public QueryOperations sort(String field, SortOrder order) {
        builder.sort(field, order);
        return this;
    }

}
