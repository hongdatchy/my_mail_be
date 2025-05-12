package com.vtidc.mymail.controller;

import com.vtidc.mymail.dto.SaveDistributionListDto;
import com.vtidc.mymail.dto.search.SearchDistributionListRequest;
import com.vtidc.mymail.dto.validate.OnUpdate;
import com.vtidc.mymail.service.DistributionListService;
import com.vtidc.mymail.service.ZimbraService;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/distribution-list")
@AllArgsConstructor
public class DistributionListController {

    private final ZimbraService zimbraService;

    private final DistributionListService distributionListService;

    @PostMapping("/search")
    public ResponseEntity<Object> searchDistributionList(@Validated @RequestBody SearchDistributionListRequest searchDistributionListRequest) {

        Object searchAccountsResponse = zimbraService.searchDistributionList(searchDistributionListRequest);
        return ResponseEntity.ok(searchAccountsResponse);

    }

    @PostMapping("/search-directory")
    public ResponseEntity<Object> searchDistributionListDirectory(@Validated @RequestBody SearchDistributionListRequest searchDistributionListRequest) {

        Object searchAccountsResponse = distributionListService.search(searchDistributionListRequest);
        return ResponseEntity.ok(searchAccountsResponse);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDetails(@PathVariable Integer id) {
        return ResponseEntity.ok(distributionListService.getDetails(id));

    }

    @PostMapping
    public ResponseEntity<Object> addDistributionList(@Validated @RequestBody SaveDistributionListDto saveDistributionListDto) {
        return ResponseEntity.ok(distributionListService.addDistributionList(saveDistributionListDto));
    }

    @PutMapping()
    public ResponseEntity<Object> editDistributionList(@Validated({Default.class, OnUpdate.class}) @RequestBody SaveDistributionListDto saveDistributionListDto) {
        return ResponseEntity.ok(distributionListService.editDistributionList(saveDistributionListDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDistributionList(@PathVariable Integer id) {
        return ResponseEntity.ok(distributionListService.deleteDistributionList(id));
    }


}
