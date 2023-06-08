package com.hrmanagement.manager;

import com.hrmanagement.dto.request.AuthCreatePersonnelProfileRequestDto;
import com.hrmanagement.dto.request.DeletePersonnelFromAuthRequestDto;
import com.hrmanagement.dto.request.UpdateManagerStatusRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:9090/api/v1/auth", name = "userprofile-auth",decode404 = true)
public interface IAuthManager {
    @PostMapping("/manager-create-personnel-userProfile")
    public ResponseEntity<Long> managerCreatePersonnelUserProfile(@RequestBody AuthCreatePersonnelProfileRequestDto dto);

    @PutMapping("/update-manager-status")
    public ResponseEntity<Boolean> updateManagerStatus(@RequestBody UpdateManagerStatusRequestDto dto);

    @PutMapping("/manager-delete-personnel")
    public ResponseEntity<Boolean> managerDeletePersonnel(@RequestBody DeletePersonnelFromAuthRequestDto dto);
}
