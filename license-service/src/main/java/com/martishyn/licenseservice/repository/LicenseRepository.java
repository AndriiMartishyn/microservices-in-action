package com.martishyn.licenseservice.repository;

import com.martishyn.licenseservice.dto.LicenseDto;
import com.martishyn.licenseservice.model.License;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LicenseRepository extends CrudRepository<License, String> {

    List<License> findByOrganizationId(String organizationId);

    Optional<License> findByLicenseId(String licenseId);

    Optional<License> findByOrganizationIdAndLicenseId(String organizationId, String licenseId);
}
