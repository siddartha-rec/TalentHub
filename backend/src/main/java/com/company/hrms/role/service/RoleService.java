package com.company.hrms.role.service;

import com.company.hrms.permission.entity.Permission;
import com.company.hrms.permission.repository.PermissionRepository;
import com.company.hrms.role.dto.*;
import com.company.hrms.role.entity.Role;
import com.company.hrms.role.mapper.RoleMapper;
import com.company.hrms.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAllActiveRoles().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
    }

    public RoleDTO getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        return roleMapper.toDto(role);
    }

    public RoleDTO getRoleByCode(String roleCode) {
        Role role = roleRepository.findByRoleCode(roleCode)
                .orElseThrow(() -> new RuntimeException("Role not found with code: " + roleCode));
        return roleMapper.toDto(role);
    }

    @Transactional
    public RoleDTO createRole(CreateRoleRequest request) {
        if (roleRepository.existsByRoleCode(request.getRoleCode())) {
            throw new RuntimeException("Role with code '" + request.getRoleCode() + "' already exists");
        }
        
        Role role = roleMapper.toEntity(request);
        
        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(request.getPermissionIds()));
            role.setPermissions(permissions);
        }
        
        Role savedRole = roleRepository.save(role);
        return roleMapper.toDto(savedRole);
    }

    @Transactional
    public RoleDTO updateRole(Long id, UpdateRoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        
        roleMapper.updateEntityFromRequest(request, role);
        
        if (request.getPermissionIds() != null) {
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(request.getPermissionIds()));
            role.setPermissions(permissions);
        }
        
        Role updatedRole = roleRepository.save(role);
        return roleMapper.toDto(updatedRole);
    }

    @Transactional
    public RoleDTO assignPermissions(Long id, AssignPermissionsRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        
        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(request.getPermissionIds()));
        role.setPermissions(permissions);
        
        Role updatedRole = roleRepository.save(role);
        return roleMapper.toDto(updatedRole);
    }

    @Transactional
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        
        role.setIsActive(false);
        roleRepository.save(role);
    }

    @Transactional
    public void hardDeleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        
        roleRepository.delete(role);
    }

    @Transactional
    public RoleDTO cloneRole(Long id, String newRoleCode, String newRoleName) {
        Role originalRole = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        
        Role clonedRole = Role.builder()
                .roleCode(newRoleCode)
                .roleName(newRoleName)
                .description(originalRole.getDescription() + " (Cloned)")
                .permissions(new HashSet<>(originalRole.getPermissions()))
                .isActive(true)
                .build();
        
        Role savedClonedRole = roleRepository.save(clonedRole);
        return roleMapper.toDto(savedClonedRole);
    }
}
