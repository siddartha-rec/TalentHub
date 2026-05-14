package com.company.hrms.role.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDTO {

    private Long id;
    private String roleCode;
    private String roleName;
    private String description;
    private Boolean isActive;
    private Set<PermissionSummary> permissions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PermissionSummary {
        private Long id;
        private String permissionCode;
        private String permissionName;
        private String module;
    }
}
