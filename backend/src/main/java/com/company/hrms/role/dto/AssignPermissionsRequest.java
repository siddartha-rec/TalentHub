package com.company.hrms.role.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignPermissionsRequest {

    private Set<Long> permissionIds;
}
