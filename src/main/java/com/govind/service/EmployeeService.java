package com.govind.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.govind.model.Employee;
import com.govind.repository.EmployeeRepository;
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public double calculateTax(Employee employee) {
        LocalDate now = LocalDate.now();
        LocalDate financialYearStart = now.getMonthValue() >= 4 ? LocalDate.of(now.getYear(), 4, 1) : LocalDate.of(now.getYear() - 1, 4, 1);
        LocalDate doj = employee.getDoj();

        int monthsWorked = (int) ChronoUnit.MONTHS.between(doj.isBefore(financialYearStart) ? financialYearStart : doj, now) + 1;

        double yearlySalary = employee.getSalary() * monthsWorked;
        double tax = 0.0;

        if (yearlySalary <= 250000) {
            tax = 0.0;
        } else if (yearlySalary <= 500000) {
            tax = (yearlySalary - 250000) * 0.05;
        } else if (yearlySalary <= 1000000) {
            tax = 250000 * 0.05 + (yearlySalary - 500000) * 0.1;
        } else {
            tax = 250000 * 0.05 + 500000 * 0.1 + (yearlySalary - 1000000) * 0.2;
        }

        if (yearlySalary > 2500000) {
            tax += (yearlySalary - 2500000) * 0.02;
        }

        return tax;
    }
}