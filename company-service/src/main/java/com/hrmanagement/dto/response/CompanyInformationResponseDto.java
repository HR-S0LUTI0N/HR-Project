package com.hrmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class CompanyInformationResponseDto {
    private String companyName;
    private String companyNeighbourhood;
    private String companyDistrict;
    private String companyProvince;
    private String companyCountry;
    private Integer companyBuildingNumber;
    private Integer companyApartmentNumber;
    private Integer companyPostalCode;
    private Double profit;
    private Double loss;
    private String logo;
    private String taxNumber;
    private String title;
    private String sector;
    private Double companyBalanceStatus;
    private List<String> holidayDates;
}
