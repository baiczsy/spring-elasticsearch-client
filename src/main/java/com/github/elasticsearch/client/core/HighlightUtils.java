package com.github.elasticsearch.client.core;

import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

/**
 * @author wangl
 * @date 2019/9/1
 */
public class HighlightUtils {

    public static HighlightBuilder createHighlightBuilder(HighlightType type, String...fields){
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        for (String field : fields) {
            HighlightBuilder.Field highlightField = new HighlightBuilder.Field(field);
            highlightField.highlighterType(type.getValue());
            highlightBuilder.field(highlightField);
        }
        return highlightBuilder;
    }
}
