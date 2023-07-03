package com.hrmanagement.manager;

import com.hrmanagement.dto.request.SubscribeCompanyRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:9070/api/v1/company", name = "auth-company",decode404 = true)
public interface ICompanyManager {
    @GetMapping("/does-company-exists/{companyId}")
    public ResponseEntity<Boolean> doesCompanyExist(@PathVariable String companyId);

    @PutMapping("/subscribe-company")
    public ResponseEntity<Boolean> subscribeCompany(@RequestBody SubscribeCompanyRequestDto subscribeCompanyRequestDto);

    @GetMapping("/does-company-subscription-exist/{companyId}")
    public ResponseEntity<Boolean> doesCompanySubscriptionExist(@PathVariable String companyId);
}
