package com.github.elasticsearch.client.connection;

/**
 * @author wangl
 * @date 2019-05-08
 */
public class RestClientException extends RuntimeException {

    public RestClientException(String message) {
        super(message);
    }

    public RestClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestClientException(Throwable cause) {
        super(cause);
    }
}
