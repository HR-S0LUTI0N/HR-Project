package com.hrmanagement.dto.response;

import com.hrmanagement.repository.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindAllPendingDayOfPermissionResponseDto {
    private String permissionId;
    private String avatar;
    private String name;
    private String middleName;
    private String surname;
    private String description;
    private Integer dayOffPermission;
    private EStatus permissionStatus;

}
