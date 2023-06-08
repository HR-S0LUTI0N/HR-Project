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
public class UpdateManagerStatusRequestDto {
    private EStatus status;
    private Long authId;
}
