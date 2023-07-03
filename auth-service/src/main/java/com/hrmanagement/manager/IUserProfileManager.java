package com.hrmanagement.manager;


import com.hrmanagement.dto.request.ForgotPasswordUserRequestDto;
import com.hrmanagement.dto.request.NewCreateManagerUserRequestDto;
import com.hrmanagement.dto.request.NewCreateVisitorUserRequestDto;
import org.apache.catalina.authenticator.SavedRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@FeignClient(url = "http://localhost:9080/api/v1/user-profile", name = "auth-userprofile")
public interface IUserProfileManager {
    @PostMapping("/create-visitor")
    public ResponseEntity<Boolean> createVisitorUser(@RequestBody NewCreateVisitorUserRequestDto dto);

    @PostMapping("/create-manager")
    public ResponseEntity<Boolean> createManagerUser(@RequestBody NewCreateManagerUserRequestDto dto);

    @PutMapping("/forgot-password")
    public ResponseEntity<Boolean> forgotPassword(@RequestBody ForgotPasswordUserRequestDto dto);

    @PutMapping("/inactivate-user/{authId}")
    public ResponseEntity<Boolean> inactivateUser(@PathVariable Long authId);

    @GetMapping("/does-founder-exists/{companyId}")
    public ResponseEntity<Boolean> doesFounderExists(@PathVariable String companyId);
}
