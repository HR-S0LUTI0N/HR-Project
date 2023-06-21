package com.hrmanagement.repository.entity;

import com.hrmanagement.repository.entity.enums.EGender;
import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.repository.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class UserProfile extends Base {
    @Id
    private String userId;
    private Long authId;
    private String password;
    @Indexed(unique = true)
    private String email;
    private String name;
    private String middleName;
    private String surname;
    private Long dateOfBirth;
    private String birthPlace;
    private String identificationNumber;
    private List<ERole> role = new ArrayList<>();
    private EStatus status;
    private EGender gender;
    private String phone;
    private Double wage;
    private Long wageDate;
    private String avatar;
    private String neighbourhood;
    private String district;
    private String province;
    private String country;
    private Integer buildingNumber;
    private Integer apartmentNumber;
    private Integer postalCode;
    private String companyId;
    private int employeeLeaves;
    private int remainingDayOffs;
    private String department;
    private Long jobStartingDate;
    private Long jobEndingDate;
    private String jobBreak;
    private String jobShift;

}
