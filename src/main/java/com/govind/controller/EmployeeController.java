package com.govind.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.govind.model.Employee;
import com.govind.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

   
    @PostMapping("/save")
    public ResponseEntity<?> createEmployee(@Validated @RequestBody Employee employee) {
        try {
            Employee savedEmployee = employeeService.saveEmployee(employee);
            return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{employeeId}/tax")
    public ResponseEntity<?> getEmployeeTax(@PathVariable Long employeeId) {
        try {
            Employee employee = employeeService.getEmployee(employeeId);
            double tax = employeeService.calculateTax(employee);

            Map<String, Object> response = new HashMap<>();
            response.put("employeeCode", employee.getEmployeeId());
            response.put("firstName", employee.getFirstName());
            response.put("lastName", employee.getLastName());
            response.put("yearlySalary", employee.getSalary() * 12);
            response.put("taxAmount", tax);
            response.put("cessAmount", (employee.getSalary() * 12) > 2500000 ? ((employee.getSalary() * 12) - 2500000) * 0.02 : 0);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/Hello")
    public String Hello() {
    	System.out.println("Hello User!!");
    	 return "Hello User!!";
    }
}