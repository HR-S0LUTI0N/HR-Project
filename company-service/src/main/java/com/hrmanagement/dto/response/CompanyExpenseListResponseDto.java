package com.hrmanagement.dto.response;

import com.hrmanagement.repository.entity.enums.EExpenseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class CompanyExpenseListResponseDto {
    private String expenseId;
    private String avatar;
    private String name;
    private String surname;
    private String expenseType;
    private String currency;
    private String billDate;
    private Double amount;
    private String description;
    private String recipePhoto;
    private EExpenseStatus eExpenseStatus;
}
