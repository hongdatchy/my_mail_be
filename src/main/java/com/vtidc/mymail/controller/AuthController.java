package com.vtidc.mymail.controller;

import com.vtidc.mymail.dto.zimbra.AuthResponse;
import com.vtidc.mymail.dto.LoginRequest;
import com.vtidc.mymail.service.ZimbraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/zimbra")
public class AuthController {

    @Autowired
    private ZimbraService zimbraService;


//    @GetMapping("/getAllAccounts")
//    public ResponseEntity<Object> getAllAccounts() {
//
//        Object allAccounts = zimbraService.getAllAccounts();
//        return ResponseEntity.ok(allAccounts);
//
//    }


}
