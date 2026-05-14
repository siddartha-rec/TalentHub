package com.company.hrms.role.mapper;

import com.company.hrms.permission.entity.Permission;
import com.company.hrms.role.dto.*;
import com.company.hrms.role.entity.Role;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {

    default RoleDTO toDto(Role role) {
        if (role == null) {
            return null;
        }
        
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setRoleCode(role.getRoleCode());
        dto.setRoleName(role.getRoleName());
        dto.setDescription(role.getDescription());
        dto.setIsActive(role.getIsActive());
        dto.setCreatedAt(role.getCreatedAt());
        dto.setUpdatedAt(role.getUpdatedAt());
        
        if (role.getPermissions() != null) {
            Set<RoleDTO.PermissionSummary> permissionSummaries = role.getPermissions().stream()
                    .map(p -> RoleDTO.PermissionSummary.builder()
                            .id(p.getId())
                            .permissionCode(p.getPermissionCode())
                            .permissionName(p.getPermissionName())
                            .module(p.getModule())
                            .build())
                    .collect(Collectors.toSet());
            dto.setPermissions(permissionSummaries);
        }
        
        return dto;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "users", ignore = true)
    Role toEntity(CreateRoleRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roleCode", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "users", ignore = true)
    void updateEntityFromRequest(UpdateRoleRequest request, @MappedTarget Role role);
}
