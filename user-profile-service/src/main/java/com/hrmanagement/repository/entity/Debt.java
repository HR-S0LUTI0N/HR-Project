package com.hrmanagement.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Debt extends Base {
    @Id
    private String debtId;
    private String userId;
    private double debt;
    @Builder.Default
    private Long debtDate = System.currentTimeMillis();

}
