package com.hrmanagement.dto.request;

import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.repository.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthCreatePersonnelProfileRequestDto {
    private String email;
    private String name;
    private String middleName;
    private String surname;
    private String password;
    private List<ERole> role;
    private EStatus status;
}
