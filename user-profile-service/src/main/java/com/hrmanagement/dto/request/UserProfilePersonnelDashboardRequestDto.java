package com.hrmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UserProfilePersonnelDashboardRequestDto {
    private String userId;
    private List<String> jobBreak;
    private String jobShift;
    private String department;
    private Double wage;
    private String wageDate;
    private int employeeCount;
    private String companyId;

}
