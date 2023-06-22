package com.hrmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class PersonnelDashboardResponseDto {
    //company
    private String companyName;
    private String logo;
    private String sector;
    private List<String> holidayDates;
    //userprofile
    private String jobBreak;
    private String jobShift;
    private String department;
    private Double wage;
    private Long wageDate;
    private int employeeCount;
}
