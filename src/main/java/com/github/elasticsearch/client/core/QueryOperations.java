package com.github.elasticsearch.client.core;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;
import java.util.Map;

/**
 * @author wangl
 * @date 2019-05-01
 */
public interface QueryOperations {

    List<Map<String, Object>> match(String index, MatchQueryBuilder queryBuilder);

    void match(String index, MatchQueryBuilder queryBuilder, ActionListener<SearchResponse> listener);

    List<Map<String, Object>> matchAll(String index, MatchAllQueryBuilder queryBuilder);

    void matchAll(String index, MatchAllQueryBuilder queryBuilder, ActionListener<SearchResponse> listener);

    List<Map<String, Object>> multiMatch(String index, MultiMatchQueryBuilder queryBuilder);

    void multiMatch(String index, MultiMatchQueryBuilder queryBuilder, ActionListener<SearchResponse> listener);

    List<Map<String, Object>> term(String index, TermQueryBuilder queryBuilder);

    void term(String index, TermQueryBuilder queryBuilder, ActionListener<SearchResponse> listener);

    List<Map<String, Object>> terms(String index, TermsQueryBuilder queryBuilder);

    void terms(String index, TermsQueryBuilder queryBuilder, ActionListener<SearchResponse> listener, Object...param);

    List<Map<String, Object>> range(String index, RangeQueryBuilder queryBuilder);

    void range(String index, RangeQueryBuilder queryBuilder, ActionListener<SearchResponse> listener);

    List<Map<String, Object>> bool(String index, BoolQueryBuilder queryBuilder);

    void bool(String index, BoolQueryBuilder queryBuilder, ActionListener<SearchResponse> listener);

    QueryOperations highlight(HighlightBuilder highlightBuilder);

    QueryOperations timeout(long timeout);

    QueryOperations from(int from);

    QueryOperations size(int size);

    QueryOperations sort(String field, SortOrder order);
}
