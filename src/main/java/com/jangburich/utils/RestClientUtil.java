package com.jangburich.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class RestClientUtil {
    private final RestClient restClient;

    // TODO: 추후 log 들은 Interceptor 로 로직을 빼서 로깅하기!
    public <T, R> T callPostRestClient (String url, R requestBody, Class<T> responseClass) {
        log.info("Request URL: {}, RequestBody: {}", url, requestBody);

        T response = restClient.post()
            .uri(url)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .body(requestBody)
            .retrieve()
            .body(responseClass);
        log.info("[Post] Response: {}", response);

        return response;
    }

    public <T> T callGetRestClient (String url, Class<T> responseClass) {
        log.info("Request URL: {}", url);

        T response = restClient.get()
            .uri(url)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(responseClass);
        log.info("[GET] Response: {}", response);

        return response;
    }

}
