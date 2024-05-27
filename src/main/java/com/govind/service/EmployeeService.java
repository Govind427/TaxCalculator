package com.govind.service;

import java.time.LocalDate;
import java.time.YearMonth;
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
        LocalDate finalDate =  doj.isBefore(financialYearStart) ? financialYearStart : doj;
        int monthsWorked =(int) ChronoUnit.MONTHS.between(finalDate,now) + 1;
   	 double yearlySalary =0;
	 yearlySalary =calculateLopAndTotalSalary(finalDate, employee.getSalary())+ (employee.getSalary() * (monthsWorked-1));
	
        
       //  yearlySalary = employee.getSalary() * monthsWorked;
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
    public static double calculateLopAndTotalSalary(LocalDate dateOfJoining, double salary) {
     
        // Get the total number of days in the joining month
        YearMonth yearMonth = YearMonth.from(dateOfJoining);
        int totalDaysInMonth = yearMonth.lengthOfMonth();
        
        // Calculate the number of days worked
        int workedDays = totalDaysInMonth - dateOfJoining.getDayOfMonth() + 1;
       
        // Calculate daily salary
        double dailySalary = salary / 30; // As per requirement, considering 30 days for LOP calculation
        
        // Calculate LOP amount
        int lopDays = totalDaysInMonth - workedDays;
        double lopAmount = lopDays * dailySalary;
        
        // Calculate the total salary for the month
        double totalSalary = workedDays * dailySalary;

        return totalSalary;
    }
}