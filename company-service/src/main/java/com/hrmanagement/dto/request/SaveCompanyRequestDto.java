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
    private double companyBalanceStatus;
    @NotBlank
    private String logo;
    @NotBlank
    private String taxNumber;
    @NotBlank
    private String title;
    @NotBlank
    private String sector;
}
