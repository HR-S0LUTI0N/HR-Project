package com.hrmanagement.repository.entity;

import com.hrmanagement.repository.entity.enums.ECommentStatus;
import com.hrmanagement.repository.entity.enums.EExpenseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@SuperBuilder
@Data
@NoArgsConstructor
@Document
public class Expense extends Base{

    @Id
    private String expenseId;
    private String userId;
    private String name;
    private String surname;
    private String companyId;
    private String expenseType;
    private String currency;
    private String demandDate;
    private String billDate;
    private String paymentMethod;
    private Double amount;
    private Double netAmount;
    private Double tax;
    private String taxZone;
    private String description;
    private String billPhoto;
    @Builder.Default
    private EExpenseStatus eExpenseStatus = EExpenseStatus.PENDING;

}
