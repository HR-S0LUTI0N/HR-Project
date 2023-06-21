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
public class PersonnelInformationResponseDto {
    private Double wage;
    private Long wageDate;
    private String avatar;
    private String department;
    private int employeeLeaves;
    //Company Service
    private String companyName;
    private String logo;
    private List<Long> holidayDates;
}
