package com.hrmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrmanagement.repository.entity.enums.EGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterManagerRequestDto {

    @NotBlank(message = "Adınızı boş bırakmayınız.")
    private String name;
    private String middleName;
    @NotBlank(message = "Soyadınızı boş bırakmayınız.")
    private String surname;
    private Long dateOfBirth;
    private String birthPlace;
    private String identificationNumber;
    private String phone;
    private EGender gender;
    private Long wageDate;
    private Double wage;
    private String avatar;
    @Email(message = "Lütfen geçerli bir email giriniz.")
    private String email;
    private String district;
    private String province;
    private String country;
    private Integer buildingNumber;
    private Integer apartmentNumber;
    private Integer postalCode;
    private String neighbourhood;
    @NotBlank
    @Size(min = 8, max = 32, message = "Şifre en az 8 en çok 32 karakter olabilir.")
    private String password;
    private String repassword;
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
    private String companyId;
    private List<Long> holidayDates;
    private List<String> breakPeriods;
    private List<Long> employeeLeaves;
    private String department;
    private Long jobStartingDate;
    private Long jobEndingDate;
}
