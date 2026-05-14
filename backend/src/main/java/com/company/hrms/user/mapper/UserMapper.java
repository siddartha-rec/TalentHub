package com.company.hrms.user.mapper;

import com.company.hrms.role.entity.Role;
import com.company.hrms.user.dto.*;
import com.company.hrms.user.entity.User;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    default UserDTO toDto(User user) {
        if (user == null) {
            return null;
        }
        
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setEmployeeId(user.getEmployeeId());
        dto.setDepartment(user.getDepartment());
        dto.setTeam(user.getTeam());
        dto.setPhone(user.getPhone());
        dto.setIsActive(user.getIsActive());
        dto.setLastLoginAt(user.getLastLoginAt());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        
        if (user.getRoles() != null) {
            Set<UserDTO.RoleSummary> roleSummaries = user.getRoles().stream()
                    .map(r -> UserDTO.RoleSummary.builder()
                            .id(r.getId())
                            .roleCode(r.getRoleCode())
                            .roleName(r.getRoleName())
                            .build())
                    .collect(Collectors.toSet());
            dto.setRoles(roleSummaries);
        }
        
        return dto;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    User toEntity(CreateUserRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "employeeId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    void updateEntityFromRequest(UpdateUserRequest request, @MappedTarget User user);
}
