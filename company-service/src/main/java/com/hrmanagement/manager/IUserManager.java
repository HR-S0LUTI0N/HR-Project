package com.hrmanagement.manager;

import com.hrmanagement.dto.response.PersonnelDashboardCommentResponseDto;
import com.hrmanagement.dto.response.UserProfileCommentResponseDto;
import com.hrmanagement.dto.response.UserProfileManagerDashboardResponseDto;
import com.hrmanagement.dto.response.UserProfilePersonnelDashboardResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(url = "http://localhost:9080/api/v1/user-profile", name = "company-userprofile",decode404 = true)
public interface IUserManager {

    @GetMapping("/get-manager-id/{authId}")
    public ResponseEntity<String> getCompanyId(@PathVariable Long authId);

    @GetMapping("/get-manager-names/{companyId}")
    public ResponseEntity<List<String>> getManagerNames(@PathVariable String companyId);

    @GetMapping("/get-userprofile-comment-information/{authId}")
    public ResponseEntity<UserProfileCommentResponseDto> getUserProfileCommentInformation(@PathVariable Long authId);
    @GetMapping("/get-userprofile-avatar-by-user-id/{userId}")
    ResponseEntity<String> getUserAvatarByUserId(@PathVariable String userId);

    @GetMapping("/get-userprofile-personnel-dashboard-information/{authId}")
    ResponseEntity<UserProfilePersonnelDashboardResponseDto> getUserProfilePersonnelDashboardInformation(@PathVariable Long authId);

    @GetMapping("/find-all-active-company-comments/{authId}")
    ResponseEntity<PersonnelDashboardCommentResponseDto> findAllActiveCompanyComments(@PathVariable Long authId);

    @GetMapping("/get-userprofile-manager-dashboard/{authId}")
    ResponseEntity<UserProfileManagerDashboardResponseDto> getUserProfileManagerDashboard(@PathVariable Long authId);

}
