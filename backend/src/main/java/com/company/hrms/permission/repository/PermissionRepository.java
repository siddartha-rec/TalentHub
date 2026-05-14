package com.company.hrms.permission.repository;

import com.company.hrms.permission.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByPermissionCode(String permissionCode);

    List<Permission> findByModule(String module);

    @Query("SELECT p FROM Permission p WHERE p.isActive = true ORDER BY p.module, p.permissionName")
    List<Permission> findAllActivePermissions();

    boolean existsByPermissionCode(String permissionCode);
}
