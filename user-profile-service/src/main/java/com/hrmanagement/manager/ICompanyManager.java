package com.hrmanagement.manager;

import com.hrmanagement.dto.request.SaveCompanyRequestDto;
import com.hrmanagement.dto.request.UpdateCompanyWageDateRequestDto;
import com.hrmanagement.dto.response.AllCompanyInfosForUserProfileResponseDto;
import com.hrmanagement.dto.response.CompanyNameAndWageDateResponseDto;
import com.hrmanagement.dto.response.PersonnelCompanyInformationResponseDto;
import org.apache.catalina.authenticator.SavedRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(url = "http://localhost:9070/api/v1/company", name = "userprofile-company",decode404 = true)
public interface ICompanyManager {



    @GetMapping("/get-personnel-company-information/{companyId}")
    public ResponseEntity<PersonnelCompanyInformationResponseDto> getPersonnelCompanyInformation(@PathVariable String companyId);
    @GetMapping("/get-company-name-with-company-id/{companyId}")
    ResponseEntity<String> getCompanyNameWithCompanyId(@PathVariable String companyId);
    @GetMapping("/get-company-infos-with-company-id/{companyId}")
    public ResponseEntity<AllCompanyInfosForUserProfileResponseDto> getAllInfosCompanyWithCompanyId(@PathVariable String companyId);

    @GetMapping("/get-company-name-and-wagedate-with-company-id/{companyId}")
    ResponseEntity<CompanyNameAndWageDateResponseDto> getCompanyNameAndWageDateResponseDto(@PathVariable String companyId);

    @PutMapping("/update-company-wage-date")
    ResponseEntity<Boolean> updateCompanyWageDate(@RequestBody UpdateCompanyWageDateRequestDto dto);
}
