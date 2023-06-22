package com.hrmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class SaveCompanyRequestDto {
    @NotBlank
    private String companyName;
    @NotBlank
    private String sector;
    @NotBlank
    private String taxNumber;
    @NotNull
    private double companyBalanceStatus;
    @NotBlank
    private String companyPhone;
    @NotBlank
    private String companyMail;
    @NotBlank
    private String description;
    @NotBlank
    private String companyNeighbourhood;
    @NotBlank
    private String companyDistrict;
    @NotBlank
    private String companyProvince;
    @NotBlank
    private String companyCountry;
    @NotNull
    private Integer companyBuildingNumber;
    @NotNull
    private Integer companyApartmentNumber;
    @NotNull
    private Integer companyPostalCode;
    @NotBlank
    private String base64Logo;
    @NotNull
    private List<String> holidayDates;
}
