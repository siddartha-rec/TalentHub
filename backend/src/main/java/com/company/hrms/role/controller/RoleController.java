package com.company.hrms.role.controller;

import com.company.hrms.role.dto.*;
import com.company.hrms.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) {
        RoleDTO role = roleService.getRoleById(id);
        return ResponseEntity.ok(role);
    }

    @GetMapping("/code/{roleCode}")
    public ResponseEntity<RoleDTO> getRoleByCode(@PathVariable String roleCode) {
        RoleDTO role = roleService.getRoleByCode(roleCode);
        return ResponseEntity.ok(role);
    }

    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@RequestBody CreateRoleRequest request) {
        RoleDTO createdRole = roleService.createRole(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(
            @PathVariable Long id,
            @RequestBody UpdateRoleRequest request) {
        RoleDTO updatedRole = roleService.updateRole(id, request);
        return ResponseEntity.ok(updatedRole);
    }

    @PostMapping("/{id}/permissions")
    public ResponseEntity<RoleDTO> assignPermissions(
            @PathVariable Long id,
            @RequestBody AssignPermissionsRequest request) {
        RoleDTO updatedRole = roleService.assignPermissions(id, request);
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteRole(@PathVariable Long id) {
        roleService.hardDeleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/clone")
    public ResponseEntity<RoleDTO> cloneRole(
            @PathVariable Long id,
            @RequestParam String newRoleCode,
            @RequestParam String newRoleName) {
        RoleDTO clonedRole = roleService.cloneRole(id, newRoleCode, newRoleName);
        return ResponseEntity.status(HttpStatus.CREATED).body(clonedRole);
    }
}
