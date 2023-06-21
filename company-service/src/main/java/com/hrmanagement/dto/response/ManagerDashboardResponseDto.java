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
public class ManagerDashboardResponseDto {
    private String companyName;
    private String title;
    private Integer companyPersonnelCount;
    private String sector;
    private Double companyBalanceStatus;
    private String holidayDates;
}
