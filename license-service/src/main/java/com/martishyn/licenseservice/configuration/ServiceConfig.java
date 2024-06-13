package com.martishyn.licenseservice.configuration;

import com.martishyn.licenseservice.service.utils.UserContextInterceptor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ConfigurationProperties(prefix = "example")
@Configuration
@Getter
@Setter
public class ServiceConfig {

    private String property;

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        RestTemplate template = new RestTemplate();
        if (template.getInterceptors().isEmpty()){
            template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
        } else {
            template.getInterceptors().add(new UserContextInterceptor());
        }
        return template;
    }
}

