package com.hrmanagement.controller;

import com.hrmanagement.dto.request.ChangeManagerStatusRequestDto;
import com.hrmanagement.dto.request.CreateUserProfileRequestDto;
import com.hrmanagement.dto.request.PersonnelDashboardCommentRequestDto;
import com.hrmanagement.dto.request.UserProfilePersonnelDashboardRequestDto;
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
    @PutMapping(ADMINCHANGEVISITORSTATUS+"/{token}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Boolean> adminChangeManagerStatus(@PathVariable String token, @RequestBody ChangeManagerStatusRequestDto dto) {
        return ResponseEntity.ok(userProfileService.adminChangeManagerStatus(token,dto));
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
        System.out.println("burdayım");
        return ResponseEntity.ok(userProfileService.inactivateUser(authId));
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<UserProfile>> findAll(){
        return ResponseEntity.ok(userProfileService.findAll());
    }

    @Hidden
    @GetMapping("/get-userprofile-comment-information/{authId}")
    public ResponseEntity<UserProfileCommentResponseDto> getUserProfileCommentInformation(@PathVariable Long authId){
        return ResponseEntity.ok(userProfileService.getUserProfileCommentInformation(authId));
    }

    @GetMapping("/find-all-manager-list/{token}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<List<FindAllManagerResponseDto>> findAllInactiveManager(@PathVariable String token) {
        return ResponseEntity.ok(userProfileService.findAllInactiveManager(token));
    }

    @Hidden
    @GetMapping("/get-userprofile-avatar-by-user-id/{userId}")
    ResponseEntity<String> getUserAvatarByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(userProfileService.getUserAvatarByUserId(userId));
    }

    @Hidden
    @GetMapping("/get-userprofile-personnel-dashboard-information/{authId}")
    ResponseEntity<UserProfilePersonnelDashboardRequestDto> getUserProfilePersonnelDashboardInformation(@PathVariable Long authId){
        return ResponseEntity.ok(userProfileService.getUserProfilePersonnelDashboardInformation(authId));
    }

    @Hidden
    @GetMapping("/find-all-active-company-comments/{authId}")
    ResponseEntity<PersonnelDashboardCommentRequestDto> findAllActiveCompanyComments(@PathVariable Long authId){
        return ResponseEntity.ok(userProfileService.findAllActiveCompanyComments(authId));
    }
}
