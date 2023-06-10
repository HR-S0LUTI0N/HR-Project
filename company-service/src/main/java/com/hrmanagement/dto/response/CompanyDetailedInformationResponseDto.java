package com.hrmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class CompanyDetailedInformationResponseDto {
    private String companyName;
    private String companyNeighbourhood;
    private String companyDistrict;
    private String companyProvince;
    private String companyCountry;
    private Integer companyBuildingNumber;
    private Integer companyApartmentNumber;
    private Integer companyPostalCode;
    private String logo;
    private String taxNumber;
    private String title;
    private String sector;
    private String description;
    private String companyPhone;
    private String companyEmail;
}
