package com.hrmanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {
    /**
     * CircuitBreaker --> Bu bir devre kesicidir. Gateway'e gelen isteklerde bir sorun olduğunda servisler için bir mesaj döner.
     *                       Hataları tespit ederek devam etmemesini sağlar.
     * @return
     */
    @GetMapping("/auth-service")
    public ResponseEntity<String> authServiceFallback(){
        return ResponseEntity.ok("Auth service şu anda hizmet veremiyor.");
    }

    @GetMapping("/user-profile-service")
    public ResponseEntity<String> userProfileServiceFallback(){
        return ResponseEntity.ok("UserProfile şu anda hizmet veremiyor.");
    }
    @GetMapping("/company-service")
    public ResponseEntity<String> companyServiceFallback(){
        return ResponseEntity.ok("Company service şu anda hizmet veremiyor.");
    }
    @GetMapping("/mail-service")
    public ResponseEntity<String> mailServiceFallback(){
        return ResponseEntity.ok("Mail service şu anda hizmet veremiyor.");
    }
}
