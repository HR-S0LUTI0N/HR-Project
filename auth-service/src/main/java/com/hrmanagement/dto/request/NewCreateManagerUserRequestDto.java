package com.hrmanagement.dto.request;

import com.hrmanagement.repository.entity.enums.EGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class NewCreateManagerUserRequestDto {
    private Long authId;
    private String password;
    private String email;
    private String name;
    private String middleName;
    private String surname;
    private Long dateOfBirth;
    private String birthPlace;
    private String identificationNumber;
    private Double companyBalanceStatus;
    private String phone;
    private EGender gender;
    private Long wageDate;
    private Double wage;
    private String avatar;
    private String district;
    private String province;
    private String country;
    private Integer buildingNumber;
    private Integer apartmentNumber;
    private Integer postalCode;
    private String neighbourhood;
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
    private String companyId;
    private List<Long> holidayDates;
    private List<Long> employeeLeaves;
    private List<String> breakPeriods;
    private String department;
    private Long jobStartingDate;
    private Long jobEndingDate;
}
