package com.hrmanagement.repository.entity;

import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.repository.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import javax.persistence.*;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Auth extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authId;
    @Column(unique = true)
    private String email;
    private String name;
    private String middleName;
    private String surname;
    private String password;
    private String activationCode;

    @ElementCollection(targetClass = ERole.class)
    @JoinTable(name = "tblRoleTypes", joinColumns = @JoinColumn(name = "authId"))
    @Column(name = "roleType", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<ERole> roles;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EStatus status = EStatus.PENDING;
    


}
