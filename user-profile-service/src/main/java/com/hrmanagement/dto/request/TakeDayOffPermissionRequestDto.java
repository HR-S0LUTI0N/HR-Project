package com.hrmanagement.dto.request;

import com.hrmanagement.repository.entity.enums.EPermissionTypes;
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
public class TakeDayOffPermissionRequestDto {
    private String startingDate;
    private String endingDate;

    private String description;
    private EPermissionTypes ePermissionTypes;
}
