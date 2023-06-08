package com.hrmanagement.dto.response;

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
public class NewCreateManagerUserResponseDto {
    private Long authId;
    private String password;
    private String email;
    private String name;
    private String middleName;
    private String surname;
    private Long dateOfBirth;
    private String birthPlace;
    private String identificationNumber;
    private String phone;
    private Double wage;
    private EGender gender;
    private Long wageDate;
    private String avatar;
    private String district;
    private String province;
    private String country;
    private Integer buildingNumber;
    private Integer apartmentNumber;
    private String neighbourhood;
    private Integer postalCode;
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
