package com.hrmanagement.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerChangeStatusModel implements Serializable {
    private String email;
    private String name;
    private String status;
}
