package com.hrmanagement.dto.request;

import com.hrmanagement.repository.entity.enums.EGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class NewCreateManagerUserRequestDto {
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
