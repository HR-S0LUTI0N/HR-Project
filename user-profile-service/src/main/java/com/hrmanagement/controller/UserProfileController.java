package com.hrmanagement.controller;

import com.hrmanagement.dto.request.CreateUserProfileRequestDto;
import com.hrmanagement.dto.response.*;
import com.hrmanagement.repository.entity.UserProfile;
import com.hrmanagement.service.UserProfileService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.hrmanagement.constant.ApiUrls.*;
import static com.hrmanagement.constant.ApiUrls.ADMINCHANGEVISITORSTATUS;

@RestController
@RequestMapping(USER_PROFILE)
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;
    @PutMapping(ADMINCHANGEVISITORSTATUS)
    public ResponseEntity<Boolean> adminChangeManagerStatus(String token,String userId, Boolean action) {
        return ResponseEntity.ok(userProfileService.adminChangeManagerStatus(token,userId, action));
    }

    @PutMapping(FORGOT_PASSWORD)
    public ResponseEntity<Boolean> forgotPassword(@RequestBody ForgotPasswordUserResponseDto dto){
        return ResponseEntity.ok(userProfileService.forgotPassword(dto));
    }

    @PostMapping(CREATE_PERSONAL+"/{token}")
    public ResponseEntity<Boolean> managerCreatePersonelUserProfile(@PathVariable String token, @RequestBody @Valid CreateUserProfileRequestDto dto){

        return ResponseEntity.ok(userProfileService.managerCreatePersonelUserProfile(token,dto));
    }

    @PostMapping("/create-visitor")
    public ResponseEntity<Boolean> createVisitorUser(@RequestBody NewCreateVisitorUserResponseDto dto){
        return ResponseEntity.ok(userProfileService.createVisitorUser(dto));
    }

    @PostMapping("/create-manager")
    public ResponseEntity<Boolean> createManagerUser(@RequestBody NewCreateManagerUserResponseDto dto){
        return ResponseEntity.ok(userProfileService.createManagerUser(dto));
    }

    @Hidden
    @GetMapping("/get-manager-id/{authId}")
    public ResponseEntity<String> getCompanyId(@PathVariable Long authId){
        return ResponseEntity.ok(userProfileService.getCompanyId(authId));
    }

    @GetMapping("/get-manager-names/{companyId}")
    public ResponseEntity<List<String>> getManagerNames(@PathVariable String companyId){
        return ResponseEntity.ok(userProfileService.getManagerNames(companyId));
    }

    @PutMapping("/manager-delete-personnel/{token}/{userId}")
    public ResponseEntity<Boolean> managerDeletePersonnel(@PathVariable String token,@PathVariable String userId){
        return ResponseEntity.ok(userProfileService.managerDeletePersonnel(token,userId));
    }

    @GetMapping("/show-personnel-information/{token}")
    public ResponseEntity<PersonnelInformationResponseDto> showPersonnelInformation(@PathVariable String token){
        return ResponseEntity.ok(userProfileService.showPersonnelInformation(token));
    }

    @PutMapping("/inactivate-user/{authId}")
    public ResponseEntity<Boolean> inactivateUser(@PathVariable Long authId){
        return ResponseEntity.ok(userProfileService.inactivateUser(authId));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/get-profile-avatar/{token}")
    public ResponseEntity<UserProfileAvatarResponseDto> getProfileAvatar(@PathVariable String token){
        return ResponseEntity.ok(userProfileService.getProfileAvatar(token));
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<UserProfile>> findAll(){
        return ResponseEntity.ok(userProfileService.findAll());
    }

}
