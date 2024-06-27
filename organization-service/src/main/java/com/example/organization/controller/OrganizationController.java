package com.example.organization.controller;

import com.example.organization.model.Organization;
import com.example.organization.service.OrganizationService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping(value = "/{organizationId}")
    public ResponseEntity<Organization> getOrganization(@PathVariable("organizationId") String organizationId) {
        return ResponseEntity.ok(organizationService.findById(organizationId));
    }

    @RolesAllowed({"ADMIN", "USER"})
    @PutMapping(value = "/{organizationId}")
    public void updateOrganization(@PathVariable("organizationId") String id, @RequestBody Organization organization) {
        organizationService.update(organization);
    }


    @RolesAllowed({"ADMIN", "USER"})
    @PostMapping
    public ResponseEntity<Organization> saveOrganization(@RequestBody Organization organization) {
        return ResponseEntity.ok(organizationService.create(organization));
    }

    @RolesAllowed("ADMIN")
    @DeleteMapping(value = "/{organizationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization(@PathVariable("organizationId") String organizationId, @RequestBody Organization organization) {
        organizationService.delete(organization);
    }
}
