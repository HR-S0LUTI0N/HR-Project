package com.hrmanagement.dto.request;

import com.hrmanagement.repository.entity.enums.EGender;
import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.repository.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserProfileRequestDto {

    @Indexed(unique = true)
    @Email
    private String email;
    @NotBlank
    private String name;
    private String middleName;
    @NotBlank
    private String surname;
    @NotBlank
    private String birthPlace;
    @NotBlank
    private String identificationNumber;
    private EGender gender;
    @NotBlank
    private String phone;
    @NotNull
    private Double wage;
    private String base64Avatar;
    @NotBlank
    private String neighbourhood;
    @NotBlank
    private String district;
    @NotBlank
    private String province;
    @NotBlank
    private String country;
    @NotNull
    private Integer buildingNumber;
    @NotNull
    private Integer apartmentNumber;
    @NotNull
    private Integer postalCode;
    @NotBlank
    private String department;
    @NotBlank
    private String jobShift;
    @NotBlank
    private String jobBreak;
    @NotNull
    private int employeeLeaves;
    private String dateOfBirth;
    private String jobStartingDate;
    private String wageDate;


}
