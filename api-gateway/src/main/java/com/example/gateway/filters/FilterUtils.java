package com.example.gateway.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import org.springframework.http.HttpHeaders;

import java.util.List;

@Component
public class FilterUtils {

    public static final String CORRELATION_ID = "tmx-correlation-id";

    public static final String AUTH_TOKEN = "Authorization";


    public String getCorrelationId(HttpHeaders requestHeaders) {
        if (requestHeaders.get(CORRELATION_ID) != null) {
            List<String> header = requestHeaders.get(CORRELATION_ID);
            return header.stream().findFirst().get();
        } else {
            return null;
        }
    }

    public String getAuthToken(HttpHeaders requestHeaders) {
        List<String> header = requestHeaders.get(AUTH_TOKEN);
        return header
                .stream()
                .findFirst().orElse(null);
    }

    public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String newValue) {
        return exchange.mutate().request(
                exchange.getRequest().mutate()
                        .header(name, newValue)
                        .build()).build();
    }

    public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
        return setRequestHeader(exchange, CORRELATION_ID, correlationId);
    }

//    public ServerWebExchange setAuthToken(ServerWebExchange exchange, String token) {
//        return setRequestHeader(exchange, AUTH_TOKEN, token);
//    }
}
