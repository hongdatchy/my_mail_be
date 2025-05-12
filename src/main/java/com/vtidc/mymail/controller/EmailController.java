package com.vtidc.mymail.controller;

import com.vtidc.mymail.dto.SaveEmailDto;
import com.vtidc.mymail.dto.search.SearchAccountRequest;
import com.vtidc.mymail.dto.validate.OnCreate;
import com.vtidc.mymail.dto.validate.OnUpdate;
import com.vtidc.mymail.service.EmailService;
import com.vtidc.mymail.service.ZimbraService;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
public class EmailController {

    private final ZimbraService zimbraService;

    private final EmailService emailService;

//    @GetMapping("/{id}")
////    @PreAuthorize("hasPermission(null, 'email:view')")
//    public ResponseEntity<Object> getById(@PathVariable Integer id) {
//        return ResponseEntity.ok(emailService.findById(id));
//    }
//
    @PostMapping
//    @PreAuthorize("hasPermission(null, 'email:create')")
    public ResponseEntity<Object> createEmail(@Validated({Default.class, OnCreate.class}) @RequestBody SaveEmailDto saveEmailDto) {
        return ResponseEntity.ok(emailService.createEmail(saveEmailDto));
    }
//
    @PutMapping()
//    @PreAuthorize("hasPermission(null, 'distribution:update')")
    public ResponseEntity<Object> updateEmail(@Validated({Default.class, OnUpdate.class}) @RequestBody SaveEmailDto emailDto) {
        return ResponseEntity.ok(emailService.modifyEmail(emailDto));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasPermission(null, 'distribution:delete')")
    public ResponseEntity<Object> deleteEmail(@PathVariable Integer id) {
        return ResponseEntity.ok(emailService.deleteEmail(id));
    }

    @PostMapping("/search")
    public ResponseEntity<Object> searchAccounts(@Validated @RequestBody SearchAccountRequest searchAccountRequest) {

        Object searchAccountsResponse = zimbraService.searchAccounts(searchAccountRequest);
        return ResponseEntity.ok(searchAccountsResponse);

    }

    @PostMapping("/search-directory")
    public ResponseEntity<Object> searchAccountsDirectory(@Validated @RequestBody SearchAccountRequest searchAccountRequest) {

        Object searchAccountsResponse = emailService.search(searchAccountRequest);
        return ResponseEntity.ok(searchAccountsResponse);

    }

}
