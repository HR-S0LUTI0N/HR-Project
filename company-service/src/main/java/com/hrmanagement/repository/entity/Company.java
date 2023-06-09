package com.hrmanagement.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@AllArgsConstructor
@SuperBuilder
@Data
@NoArgsConstructor
@Document
public class Company extends Base{
    @Id
    private String companyId;
    @Indexed(unique = true)
    private String companyName;
    private String companyNeighbourhood;
    private String companyDistrict;
    private String companyProvince;
    private String companyCountry;
    private Integer companyBuildingNumber;
    private Integer companyApartmentNumber;
    private Integer companyPostalCode;
    private Double companyBalanceStatus;
    private String logo;
    private String taxNumber;
    private String title;
    private String sector;
    private String description;
    private List<Long> holidayDates;
}
