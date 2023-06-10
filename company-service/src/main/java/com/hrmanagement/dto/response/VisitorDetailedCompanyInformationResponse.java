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
public class VisitorDetailedCompanyInformationResponse {
    private String companyName;
    private String companyNeighbourhood;
    private String companyDistrict;
    private String companyProvince;
    private String companyCountry;
    private Integer companyBuildingNumber;
    private Integer companyApartmentNumber;
    private Integer companyPostalCode;
    private String logo;
    private String title;
    private String sector;
    private String description;
    private String companyPhone;
    private String companyMail;
    private List<FindCompanyCommentsResponseDto> companyComments;
}
