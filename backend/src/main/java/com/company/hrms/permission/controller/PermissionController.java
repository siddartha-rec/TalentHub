package com.company.hrms.permission.controller;

import com.company.hrms.permission.dto.CreatePermissionRequest;
import com.company.hrms.permission.dto.PermissionDTO;
import com.company.hrms.permission.dto.UpdatePermissionRequest;
import com.company.hrms.permission.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        List<PermissionDTO> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/module/{module}")
    public ResponseEntity<List<PermissionDTO>> getPermissionsByModule(@PathVariable String module) {
        List<PermissionDTO> permissions = permissionService.getPermissionsByModule(module);
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionDTO> getPermissionById(@PathVariable Long id) {
        PermissionDTO permission = permissionService.getPermissionById(id);
        return ResponseEntity.ok(permission);
    }

    @GetMapping("/code/{permissionCode}")
    public ResponseEntity<PermissionDTO> getPermissionByCode(@PathVariable String permissionCode) {
        PermissionDTO permission = permissionService.getPermissionByCode(permissionCode);
        return ResponseEntity.ok(permission);
    }

    @PostMapping
    public ResponseEntity<PermissionDTO> createPermission(@RequestBody CreatePermissionRequest request) {
        PermissionDTO createdPermission = permissionService.createPermission(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPermission);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissionDTO> updatePermission(
            @PathVariable Long id,
            @RequestBody UpdatePermissionRequest request) {
        PermissionDTO updatedPermission = permissionService.updatePermission(id, request);
        return ResponseEntity.ok(updatedPermission);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeletePermission(@PathVariable Long id) {
        permissionService.hardDeletePermission(id);
        return ResponseEntity.noContent().build();
    }
}
