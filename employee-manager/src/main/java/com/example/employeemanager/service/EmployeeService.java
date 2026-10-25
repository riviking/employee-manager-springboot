package com.example.employeemanager.service;

import com.example.employeemanager.model.Employee;
import com.example.employeemanager.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Read all
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Read one
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    // Create and Update (save inserts if id is null, updates if id exists)
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Delete
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    // Duplicate check for create
    public boolean emailExists(String email) {
        return employeeRepository.existsByEmail(email);
    }

    // Duplicate check for update (ignore the employee being edited)
    public boolean emailExistsForOtherEmployee(String email, Long id) {
        return employeeRepository.existsByEmailAndIdNot(email, id);
    }

    // Search by name (if keyword is empty, return all employees)
    public List<Employee> searchEmployees(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return employeeRepository.findAll();
        }
        return employeeRepository.findByFullNameContainingIgnoreCase(keyword.trim());
    }
}