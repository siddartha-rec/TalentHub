package com.company.hrms.permission.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePermissionRequest {

    private String permissionName;
    private String description;
    private Boolean isActive;
}
