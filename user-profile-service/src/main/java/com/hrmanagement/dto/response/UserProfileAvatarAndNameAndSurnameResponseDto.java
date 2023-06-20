package com.hrmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UserProfileAvatarAndNameAndSurnameResponseDto {
    private String name;
    private String middleName;
    private String surname;
    private String avatar;
    private String email;

}
