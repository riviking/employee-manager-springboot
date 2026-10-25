package com.example.employeemanager.repository;

import com.example.employeemanager.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // true if any employee already has this email (used when creating)
    boolean existsByEmail(String email);

    // true if ANOTHER employee (different id) has this email (used when updating)
    boolean existsByEmailAndIdNot(String email, Long id);

    List<Employee> findByFullNameContainingIgnoreCase(String keyword);
}