package com.hrmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterVisitorRequestDto {

    @NotBlank(message = "Adınızı boş bırakmayınız.")
    private String name;
    private String middleName;
    @NotBlank(message = "Soyadınızı boş bırakmayınız.")
    private String surname;
    @Email(message = "Lütfen geçerli bir email giriniz.")
    private String email;

    @NotBlank
    @Size(min = 8, max = 32, message = "Şifre en az 8 en çok 32 karakter olabilir.")
    private String password;
    private String repassword;

}
