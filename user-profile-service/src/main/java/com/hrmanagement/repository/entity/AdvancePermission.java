package com.hrmanagement.repository.entity;
import com.hrmanagement.repository.entity.enums.EAdvanceStatus;
import com.hrmanagement.repository.entity.enums.EStatus;
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
public class AdvancePermission extends Base {
    @Id
    private String advancedPermissionId;
    private String description;
    private Double advanceRequest;
    private String userId;
    private String companyId;
    @Builder.Default
    private Long requestDate=System.currentTimeMillis();
    @Builder.Default
    private EAdvanceStatus status = EAdvanceStatus.PENDING;
    @Builder.Default
    private String currency = "TRY";

}
