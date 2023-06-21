package com.hrmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class PersonelUpdateRequestDto {
    private String name;
    private String middleName;
    private String surname;
    private String email;
    private String phone;
    private String token;
}
