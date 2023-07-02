package com.hrmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UserProfilePersonnelDashboardResponseDto {
    private String userId;
    private List<String> jobBreak;
    private String jobShift;
    private String department;
    private Double wage;
    private String companyId;
}
