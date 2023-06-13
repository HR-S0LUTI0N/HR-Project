package com.hrmanagement.manager;

import com.hrmanagement.dto.request.SaveCompanyRequestDto;
import com.hrmanagement.dto.response.PersonnelCompanyInformationResponseDto;
import org.apache.catalina.authenticator.SavedRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:9070/api/v1/company", name = "userprofile-company",decode404 = true)
public interface ICompanyManager {



    @GetMapping("/get-personnel-company-information/{companyId}")
    public ResponseEntity<PersonnelCompanyInformationResponseDto> getPersonnelCompanyInformation(@PathVariable String companyId);
    @GetMapping("/get-company-name-with-company-id/{companyId}")
    ResponseEntity<String> getCompanyNameWithCompanyId(@PathVariable String companyId);

}
