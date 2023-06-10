package com.hrmanagement.controller;

import com.hrmanagement.dto.response.CompanyDetailedInformationResponseDto;
import com.hrmanagement.dto.response.CompanyInformationResponseDto;
import com.hrmanagement.dto.response.PersonnelCompanyInformationResponseDto;
import com.hrmanagement.dto.request.SaveCompanyRequestDto;
import com.hrmanagement.dto.response.VisitorCompanyInformations;
import com.hrmanagement.repository.entity.Company;
import com.hrmanagement.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hrmanagement.constants.ApiUrls.*;

@RestController
@RequestMapping(COMPANY)
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/save")
    public ResponseEntity<Boolean> saveCompanyRequestDto(@RequestBody SaveCompanyRequestDto dto){
        return ResponseEntity.ok(companyService.save(dto));
    }

    @GetMapping("/show-company-information/{token}")
    public ResponseEntity<CompanyInformationResponseDto> showCompanyInformation(@PathVariable String token){
        return ResponseEntity.ok(companyService.showCompanyInformation(token));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/find-all-company-preview-information")
    public ResponseEntity<List<VisitorCompanyInformations>> findAllCompanyInformations(){
        return ResponseEntity.ok(companyService.findAllCompanyPreviewInformation());
    }
    @GetMapping("/find-all")
    public ResponseEntity<List<Company>> findAll(){
        return ResponseEntity.ok(companyService.findAll());
    }

    @GetMapping("/get-personnel-company-information/{companyId}")
    public ResponseEntity<PersonnelCompanyInformationResponseDto> getPersonnelCompanyInformation(@PathVariable String companyId){
        return ResponseEntity.ok(companyService.getPersonnelCompanyInformation(companyId));
    }

    @GetMapping("/does-company-exists/{companyId}")
    public ResponseEntity<Boolean> doesCompanyExist(@PathVariable String companyId){
        return ResponseEntity.ok(companyService.doesCompanyIdExist(companyId));
    }
    //Basılınca ilgili şirketin detaylarını getirme
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/find-company-detailed-information/{token}/{companyId}")
    public ResponseEntity<CompanyDetailedInformationResponseDto> findCompanyDetailedInformation(@PathVariable String token, @PathVariable String companyId){
        return ResponseEntity.ok(companyService.findCompanyDetailedInformation(token,companyId));
    }

}
