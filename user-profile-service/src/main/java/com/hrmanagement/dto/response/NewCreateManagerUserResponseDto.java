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
    private String name;
    private String middleName;
    private String surname;
    private String identificationNumber;
    private String email;
    private String password;
    private String repassword;
    private String companyId;
    private String department;
}
