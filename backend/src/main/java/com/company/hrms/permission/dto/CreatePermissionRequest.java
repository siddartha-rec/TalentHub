package com.company.hrms.permission.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePermissionRequest {

    private String permissionCode;
    private String permissionName;
    private String module;
    private String description;
}
