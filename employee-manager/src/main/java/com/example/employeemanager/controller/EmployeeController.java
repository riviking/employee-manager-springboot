package com.example.employeemanager.controller;

import com.example.employeemanager.model.Employee;
import com.example.employeemanager.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Opening http://localhost:8080 goes to the employees page
    @GetMapping("/")
    public String home() {
        return "redirect:/employees";
    }

    // READ: GET /employees -> show the form and the table
    @GetMapping("/employees")
    public String listEmployees(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employees";
    }

    // CREATE: POST /employees -> save a new employee
    @PostMapping("/employees")
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (employee.getEmail() != null && employeeService.emailExists(employee.getEmail())) {
            result.rejectValue("email", "duplicate", "This email is already registered");
        }

        if (result.hasErrors()) {
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "employees";
        }

        employeeService.saveEmployee(employee);
        redirectAttributes.addFlashAttribute("successMessage", "Employee saved successfully!");
        return "redirect:/employees";
    }

    // EDIT: GET /employees/{id}/edit -> show the edit form filled with data
    @GetMapping("/employees/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model,
                               RedirectAttributes redirectAttributes) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Employee not found");
            return "redirect:/employees";
        }
        model.addAttribute("employee", employee.get());
        return "editemployee";
    }

    // UPDATE: POST /employees/{id}/update -> save the changes
    @PostMapping("/employees/{id}/update")
    public String updateEmployee(@PathVariable Long id,
                                 @Valid @ModelAttribute("employee") Employee employee,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {

        employee.setId(id);

        if (employee.getEmail() != null
                && employeeService.emailExistsForOtherEmployee(employee.getEmail(), id)) {
            result.rejectValue("email", "duplicate", "This email is already registered");
        }

        if (result.hasErrors()) {
            return "editemployee";
        }

        employeeService.saveEmployee(employee);
        redirectAttributes.addFlashAttribute("successMessage", "Employee updated successfully!");
        return "redirect:/employees";
    }

    // DELETE: POST /employees/{id}/delete -> remove the employee
    @PostMapping("/employees/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        employeeService.deleteEmployee(id);
        redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully!");
        return "redirect:/employees";
    }
}

