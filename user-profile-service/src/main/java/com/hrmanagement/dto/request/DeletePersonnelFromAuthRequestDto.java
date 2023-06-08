package com.hrmanagement.dto.request;

import com.hrmanagement.repository.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class DeletePersonnelFromAuthRequestDto {
    private Long authId;
    private EStatus status;
}
