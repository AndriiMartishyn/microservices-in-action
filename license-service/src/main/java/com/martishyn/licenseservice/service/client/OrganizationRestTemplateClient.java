package com.martishyn.licenseservice.service.client;

import com.martishyn.licenseservice.model.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class OrganizationRestTemplateClient {

    private final RestTemplate restTemplate;

    public Organization getOrganization(String organizationId) {
        return restTemplate
                .getForObject("http://organization-service/v1/organization/{organization}",
                        Organization.class, organizationId);
    }
}
