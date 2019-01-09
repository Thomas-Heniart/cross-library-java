package com.crossover.techtrial.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HttpEntityHelper {

    private static final String INSTANTIATION_ERROR = "This class cannot be instantiate";

    private HttpEntityHelper() {
        throw new RuntimeException(INSTANTIATION_ERROR);
    }

    public static HttpEntity<Object> getHttpEntityForJson(final String body) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, httpHeaders);
    }
}
