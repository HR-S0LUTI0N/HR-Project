package com.hrmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UserProfilePersonnelDashboardRequestDto {
    private String userId;
    private String jobBreak;
    private String jobShift;
    private String department;
    private Double wage;
    private Long wageDate;
    private int employeeCount;
    private String companyId;

}
