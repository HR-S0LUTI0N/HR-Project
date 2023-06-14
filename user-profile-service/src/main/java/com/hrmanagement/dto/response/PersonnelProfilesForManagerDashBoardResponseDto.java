package com.hrmanagement.dto.response;

import com.hrmanagement.repository.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class PersonnelProfilesForManagerDashBoardResponseDto {

    private String userId;
    private String avatar;
    private String name;
    private String middleName;
    private String surname;
    private String companyName;             //Company Service
    private String roleString;    //Role'ü
    private String department;
    private String wage;
    private String wageDate;
    private int remainingDayOffs;
    private Long dateOfBirth;   //verified yerine koyulacak
    private EStatus eStatus;

}
