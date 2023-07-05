package com.hrmanagement.dto.response;

import com.hrmanagement.repository.entity.enums.EAdvanceStatus;
import com.hrmanagement.repository.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class FindAllAdvanceRequestListResponseDto {
    private String advancedPermissionId;
    private String avatar;
    private String name;
    private String surname;
    private String description;
    private Double advanceRequest; //amount
    private String requestDate;
    private EAdvanceStatus status;
    private String currency;
}
