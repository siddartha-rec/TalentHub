package com.company.hrms.user.dto;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

    private String name;
    private String department;
    private String team;
    private String phone;
    private Boolean isActive;
    private Set<Long> roleIds;
}
