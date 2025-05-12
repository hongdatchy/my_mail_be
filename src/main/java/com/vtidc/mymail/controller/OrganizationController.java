package com.vtidc.mymail.controller;

import com.vtidc.mymail.dto.SaveEmailDto;
import com.vtidc.mymail.dto.search.SearchOrgRequest;
import com.vtidc.mymail.dto.validate.OnCreate;
import com.vtidc.mymail.service.OrganizationService;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organization")
@AllArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping("/search")
    public ResponseEntity<Object> search(@Validated @RequestBody SearchOrgRequest searchOrgRequest) {

        Object searchAccountsResponse = organizationService.search(searchOrgRequest);
        return ResponseEntity.ok(searchAccountsResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getDetails(@PathVariable Integer id) {
        Object searchAccountsResponse = organizationService.getDetails(id);
        return ResponseEntity.ok(searchAccountsResponse);
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<Object> approve(@PathVariable Integer id, @Validated({Default.class, OnCreate.class}) @RequestBody SaveEmailDto saveEmailDto) {
        organizationService.approveOrganization(id, saveEmailDto);
        return ResponseEntity.ok(saveEmailDto);
    }

//    @PostMapping
//    public ResponseEntity<Object> addDistributionList(@Validated @RequestBody SaveOrganizationDto saveOrganizationDto) {
//        return ResponseEntity.ok(organizationService.addOrganization(saveOrganizationDto));
//    }
//
//    @PutMapping()
//    public ResponseEntity<Object> editDistributionList(@Validated({Default.class, OnUpdate.class}) @RequestBody SaveOrganizationDto saveOrganizationDto) {
//        return ResponseEntity.ok(organizationService.editOrganization(saveOrganizationDto));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Object> deleteDistributionList(@PathVariable Integer id) {
//        return ResponseEntity.ok(organizationService.deleteOrganization(id));
//    }

}
