package com.hrmanagement.dto.request;

import com.hrmanagement.repository.entity.enums.ECommentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindPendingCommentWithCompanyName {
    private String commentId;
    private String comment;
    private String avatar;
    private String name;
    private String surname;
    private String companyName;
    private ECommentStatus eCommentStatus;
}
