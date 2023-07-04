package com.hrmanagement.repository.entity;

import com.hrmanagement.repository.entity.enums.EPermissionTypes;
import com.hrmanagement.repository.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DayOffPermission extends Base {
    @Id
    private String permissionId;
    private String userId;
    private String startingDate;
    private String endingDate;
    private List<String> permissionDates = new ArrayList<>();
    private String description;
    private EPermissionTypes ePermissionTypes;
    @Builder.Default
    private EStatus status = EStatus.PENDING;
}
