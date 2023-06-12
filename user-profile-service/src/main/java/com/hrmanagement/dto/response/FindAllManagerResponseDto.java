package com.hrmanagement.dto.response;

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
public class FindAllManagerResponseDto {
    private String userId;
    private EStatus status;
    private List<ERole> role;
    private String name;
    private String middleName;
    private String surname;
    private String avatar;
    private String companyId;
}
