package com.martishyn.licenseservice.service.client;

import com.martishyn.licenseservice.model.Organization;
import com.netflix.appinfo.InstanceInfo;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrganizationDiscoveryClient {

    @Autowired
    private DiscoveryClient discoveryClient;

    public Organization getOrganization(String organizationId) {
        RestTemplate restTemplate = new RestTemplate();
        List<ServiceInstance> instances = discoveryClient.getInstances("organization-service");

        if (instances.isEmpty()) {
            return null;
        }
        String serviceURI = String.format("%s/v1/organization/%s", instances.get(0).getUri(), organizationId);
        return restTemplate.getForObject(serviceURI, Organization.class);
    }
}
