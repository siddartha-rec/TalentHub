package com.company.hrms.role.dto;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoleRequest {

    private String roleCode;
    private String roleName;
    private String description;
    private Set<Long> permissionIds;
}
