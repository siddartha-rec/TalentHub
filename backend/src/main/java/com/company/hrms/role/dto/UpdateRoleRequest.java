package com.company.hrms.role.dto;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRoleRequest {

    private String roleName;
    private String description;
    private Boolean isActive;
    private Set<Long> permissionIds;
}
