package com.martishyn.licenseservice.service;

import com.martishyn.licenseservice.configuration.ServiceConfig;
import com.martishyn.licenseservice.model.License;
import com.martishyn.licenseservice.model.Organization;
import com.martishyn.licenseservice.repository.LicenseRepository;
import com.martishyn.licenseservice.service.client.OrganizationDiscoveryClient;
import com.martishyn.licenseservice.service.client.OrganizationFeignClient;
import com.martishyn.licenseservice.service.client.OrganizationRestTemplateClient;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class LicenseService {

    private final MessageSource messageSource;

    private final LicenseRepository licenseRepository;

    private final ServiceConfig serviceConfig;

    private final OrganizationFeignClient organizationFeignClient;

    private final OrganizationRestTemplateClient organizationRestClient;

    private final OrganizationDiscoveryClient organizationDiscoveryClient;

    public License getLicense(String organizationId, String licenseId, String clientType) {
        Optional<License> license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (license.isEmpty()) {
            throw new IllegalArgumentException(String.format(messageSource.getMessage("license.search.error.message", null, null), licenseId, organizationId));
        }
        Organization organization = retrieveOrganizationInfo(organizationId,
                clientType);
        if (null != organization) {
            license.get().setOrganizationName(organization.getName());
            license.get().setContactName(organization.getContactName());
            license.get().setContactEmail(organization.getContactEmail());
            license.get().setContactPhone(organization.getContactPhone());
        }
        return license.get().withComment(serviceConfig.getProperty());
    }

    private Organization retrieveOrganizationInfo(String organizationId, String clientType) {
        Organization organization = null;

        switch (clientType) {
            case "feign":
                System.out.println("I am using the feign client");
                organization = organizationFeignClient.getOrganization(organizationId);
                break;
            case "rest":
                System.out.println("I am using the rest client");
                organization = organizationRestClient.getOrganization(organizationId);
                break;
            case "discovery":
                System.out.println("I am using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;
            default:
                organization = organizationRestClient.getOrganization(organizationId);
                break;
        }
        return organization;
    }

    @CircuitBreaker(name = "licenseService", fallbackMethod = "buildFallBackLicenseList")
    @Bulkhead(name = "bulkheadLicenseService", fallbackMethod = "buildFallbackLicenseList")
    @Retry(name = "retryLicenseService", fallbackMethod = "buildFallbackLicenseList")
    public List<License> getLicensesByOrganization(String organizationId) throws TimeoutException {
        randomlyRunLong();
        return licenseRepository.findByOrganizationId(organizationId);
    }

    private List<License> buildFallBackLicenseList(String organizationId, Throwable t) {
        List<License> fallbackList = new ArrayList<>();
        License license = new License();
        license.setLicenseId("0000000-00-00000");
        license.setOrganizationId(organizationId);
        license.setProductName("Sorry no licensing information currently available");
        fallbackList.add(license);
        return fallbackList;
    }

    public License createLicense(License license) {
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);
        return license.withComment(serviceConfig.getProperty());
    }

    //will not call repository save  because dirty reads will update after transaction
    public License updateLicense(License license) {
        return license.withComment(serviceConfig.getProperty());
    }

    public String deleteLicense(String licenseId) {
        Optional<License> license = licenseRepository.findByLicenseId(licenseId);
        license.ifPresent(licenseRepository::delete);
        return String.format(messageSource.getMessage(
                "license.delete.message", null, null), licenseId);
    }


    private void randomlyRunLong() throws TimeoutException {
        Random rand = new Random();
        int randomNum = rand.nextInt((3 - 1) + 1) + 1;
        if (randomNum == 3) sleep();
    }

    private void sleep() throws TimeoutException {
        try {
            System.out.println("Sleep");
            Thread.sleep(5000);
            throw new java.util.concurrent.TimeoutException();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}

