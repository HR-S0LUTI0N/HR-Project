package com.hrmanagement.controller;

import com.hrmanagement.dto.request.CompanyNameAndWageDateRequestDto;
import com.hrmanagement.dto.request.FindPendingCommentWithCompanyName;
import com.hrmanagement.dto.response.*;
import com.hrmanagement.dto.request.SaveCompanyRequestDto;
import com.hrmanagement.repository.entity.Company;
import com.hrmanagement.service.CompanyService;
import io.swagger.v3.oas.annotations.Hidden;
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

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/save/{token}")
    public ResponseEntity<Boolean> saveCompanyRequestDto(@PathVariable String token, @RequestBody SaveCompanyRequestDto dto){
        return ResponseEntity.ok(companyService.save(token,dto));
    }

    @GetMapping("/show-company-information/{token}")
    public ResponseEntity<CompanyInformationResponseDto> showCompanyInformation(@PathVariable String token){
        return ResponseEntity.ok(companyService.showCompanyInformation(token));
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/find-all-company-preview-information")
    public ResponseEntity<List<VisitorCompanyInformations>> findAllCompanyPreviewInformation(){
        return ResponseEntity.ok(companyService.findAllCompanyPreviewInformation());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/find-all-detailed-company-information/{companyId}")
    public ResponseEntity<VisitorDetailedCompanyInformationResponse> findCompanyDetailedInformation(@PathVariable String companyId){
        return ResponseEntity.ok(companyService.findCompanyDetailedInformation(companyId));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
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

    @GetMapping("/find-comments-with-company-name-by-status/{token}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<List<FindPendingCommentWithCompanyName>> findCommentWithCompanyNameByStatus(@PathVariable String token) {
        return ResponseEntity.ok(companyService.findCommentWithCompanyNameByStatus(token));
    }
    @Hidden
    @GetMapping("/get-company-name-with-company-id/{companyId}")
    public ResponseEntity<String> getCompanyNameWithCompanyId(@PathVariable String companyId) {
        return ResponseEntity.ok(companyService.getCompanyNameWithCompanyId(companyId));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("get-personnel-dashboard-information/{token}")
    public ResponseEntity<PersonnelDashboardResponseDto>getPersonnelDashboardInformation(@PathVariable String token){
        return ResponseEntity.ok(companyService.getPersonnelDashboardInformation(token));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("get-manager-dashboard-information/{token}")
    public ResponseEntity<ManagerDashboardResponseDto> getManagerDashboardInformation(@PathVariable String token){
        return ResponseEntity.ok(companyService.getManagerDashboardInformation(token));
    }
    @Hidden
    @GetMapping("/get-company-infos-with-company-id/{companyId}")
    public ResponseEntity<AllCompanyInfosForUserProfileResponseDto> getAllInfosCompanyWithCompanyId(@PathVariable String companyId) {
        return ResponseEntity.ok(companyService.getAllInfosCompanyWithCompanyId(companyId));
    }

    @Hidden
    @GetMapping("/get-company-name-and-wagedate-with-company-id/{companyId}")
    ResponseEntity<CompanyNameAndWageDateRequestDto> getCompanyNameAndWageDateResponseDto(@PathVariable String companyId){
        return ResponseEntity.ok(companyService.getCompanyNameAndWageDateResponseDto(companyId));
    }
    @Hidden
    @PutMapping("/subscribe-company")
    public ResponseEntity<Boolean> subscribeCompany(@RequestBody SubscribeCompanyResponseDto dto){
        return ResponseEntity.ok(companyService.subscribeCompany(dto));
    }
    @Hidden
    @GetMapping("/does-company-subscription-exist/{companyId}")
    public ResponseEntity<Boolean> doesCompanySubscriptionExist(@PathVariable String companyId){
        return ResponseEntity.ok(companyService.doesCompanySubscriptionExist(companyId));
    }


}
