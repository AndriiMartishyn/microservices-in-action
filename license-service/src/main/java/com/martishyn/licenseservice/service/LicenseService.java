package com.martishyn.licenseservice.service;

import com.martishyn.licenseservice.configuration.ServiceConfig;
import com.martishyn.licenseservice.model.License;
import com.martishyn.licenseservice.repository.LicenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LicenseService {

    private final MessageSource messageSource;

    private final LicenseRepository licenseRepository;

    private final ServiceConfig serviceConfig;

    public License getLicense(String licenseId, String organizationId) {
        Optional<License> foundLicense = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (foundLicense.isEmpty()) {
            throw new IllegalArgumentException(String.format(messageSource.getMessage("license.searcherror.message", null, null), licenseId, organizationId));
        }
        return foundLicense.get().withComment(serviceConfig.getProperty());
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
}

