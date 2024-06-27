package com.example.gateway.configuration;

import com.example.gateway.filters.FilterUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class ResponseFilterConfiguration {

    @Autowired
    private FilterUtils filterUtils;

    @Bean
    public GlobalFilter postGlobalFilter(){
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() ->{
            HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
            String correlationId = filterUtils.getCorrelationId(requestHeaders);
            String token = filterUtils.getAuthToken(requestHeaders);
            log.info("Adding the correlation id to the outbound headers. {}", correlationId);
            log.info("Adding auth token to the outbound headers. {}", token);
            exchange.getResponse().getHeaders().add(FilterUtils.CORRELATION_ID, correlationId);
            exchange.getResponse().getHeaders().add(FilterUtils.AUTH_TOKEN, token);
            log.info("Completing outgoing request for {}.", exchange.getRequest().getURI());
        }));
    }
}
