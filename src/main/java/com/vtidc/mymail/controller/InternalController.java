package com.vtidc.mymail.controller;

import com.vtidc.mymail.dto.EmailDtoForZimbra;
import com.vtidc.mymail.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal")
@AllArgsConstructor
public class InternalController {

    private final EmailService emailService;

    @PostMapping("/createEmailFromZimbra")
    public ResponseEntity<Object> createUser(@RequestBody EmailDtoForZimbra emailDtoForZimbra) {
        return ResponseEntity.ok(emailService.createEmailFromZimbra(emailDtoForZimbra));
    }


}
