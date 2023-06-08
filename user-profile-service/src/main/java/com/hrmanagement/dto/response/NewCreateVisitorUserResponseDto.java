package com.hrmanagement.dto.response;

import com.hrmanagement.repository.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCreateVisitorUserResponseDto {
    private Long authId;
    private String password;
    private String email;
    private String name;
    private String middleName;
    private String surname;
    private EStatus status;

}
