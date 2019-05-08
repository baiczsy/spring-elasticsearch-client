package org.nf.elasticsearch.client.core;

/**
 * @author wangl
 * @date 2019-04-30
 */
public class ElasticsearchAccessException extends RuntimeException {

    public ElasticsearchAccessException(String msg) {
        super(msg);
    }

    public ElasticsearchAccessException(Throwable cause) {
        super(cause);
    }

    public ElasticsearchAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
