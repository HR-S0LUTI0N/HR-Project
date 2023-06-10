package com.hrmanagement.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@SuperBuilder
@Data
@NoArgsConstructor
public class Comment extends Base {
    @Id
    private String commentId;
    private String comment;
    private String userId;
    private String name;
    private String surname;
    private String companyId;
}
