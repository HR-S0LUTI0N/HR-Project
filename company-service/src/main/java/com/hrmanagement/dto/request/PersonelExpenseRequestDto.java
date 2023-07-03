package com.hrmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class PersonelExpenseRequestDto {



    private String expenseType;
    private String currency;
    private String demandDate;
    private String billDate;
    private String paymentMethod;
    private Double amount;
    private Double netAmount;
    private Double Tax;
    private String taxZone;
    private String description;
    private String billPhoto;
}
