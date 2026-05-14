package com.company.hrms.permission.service;

import com.company.hrms.permission.dto.CreatePermissionRequest;
import com.company.hrms.permission.dto.PermissionDTO;
import com.company.hrms.permission.dto.UpdatePermissionRequest;
import com.company.hrms.permission.entity.Permission;
import com.company.hrms.permission.mapper.PermissionMapper;
import com.company.hrms.permission.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAllActivePermissions().stream()
                .map(permissionMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PermissionDTO> getPermissionsByModule(String module) {
        return permissionRepository.findByModule(module).stream()
                .map(permissionMapper::toDto)
                .collect(Collectors.toList());
    }

    public PermissionDTO getPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
        return permissionMapper.toDto(permission);
    }

    public PermissionDTO getPermissionByCode(String permissionCode) {
        Permission permission = permissionRepository.findByPermissionCode(permissionCode)
                .orElseThrow(() -> new RuntimeException("Permission not found with code: " + permissionCode));
        return permissionMapper.toDto(permission);
    }

    @Transactional
    public PermissionDTO createPermission(CreatePermissionRequest request) {
        if (permissionRepository.existsByPermissionCode(request.getPermissionCode())) {
            throw new RuntimeException("Permission with code '" + request.getPermissionCode() + "' already exists");
        }
        
        Permission permission = permissionMapper.toEntity(request);
        Permission savedPermission = permissionRepository.save(permission);
        return permissionMapper.toDto(savedPermission);
    }

    @Transactional
    public PermissionDTO updatePermission(Long id, UpdatePermissionRequest request) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
        
        permissionMapper.updateEntityFromRequest(request, permission);
        Permission updatedPermission = permissionRepository.save(permission);
        return permissionMapper.toDto(updatedPermission);
    }

    @Transactional
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
        
        permission.setIsActive(false);
        permissionRepository.save(permission);
    }

    @Transactional
    public void hardDeletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
        
        permissionRepository.delete(permission);
    }
}
