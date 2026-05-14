package com.company.hrms.user.dto;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {

    private String name;
    private String email;
    private String password;
    private String employeeId;
    private String department;
    private String team;
    private String phone;
    private Set<Long> roleIds;
}
