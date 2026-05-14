package com.company.hrms.user.repository;

import com.company.hrms.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmployeeId(String employeeId);

    @Query("SELECT u FROM User u WHERE u.isActive = true ORDER BY u.name")
    List<User> findAllActiveUsers();

    boolean existsByEmail(String email);

    boolean existsByEmployeeId(String employeeId);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.id = :roleId AND u.isActive = true")
    List<User> findByRoleId(Long roleId);
}
