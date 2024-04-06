package org.example.service;

import org.example.model.Employee;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CompanyAnalyzer {
    public static final String START_COLUMN_NAME = "Id";

    public Map<Integer, Employee> readEmployees(String filename) throws IOException {
        Map<Integer, Employee> employees = new HashMap<>();

        InputStream in = getInputStream(filename);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(START_COLUMN_NAME)) {
                    continue;
                }
                String[] values = line.split(",");
                Employee employee = new Employee();
                employee.setId(Integer.parseInt(values[0]));
                employee.setFirstName(values[1]);
                employee.setLastName(values[2]);
                employee.setSalary(BigDecimal.valueOf(Double.parseDouble(values[3])));
                if (values.length > 4 && !values[4].isEmpty()) {
                    employee.setManagerId(Integer.parseInt(values[4]));
                }
                employees.put(employee.getId(), employee);
            }
        }
        return employees;
    }

    public void checkReportingLine(Map<Integer, Employee> employees) {
        for (Employee employee : employees.values()) {
            int reportingLineLength = 0;
            Integer managerId = employee.getManagerId();
            while (managerId != null) {
                reportingLineLength++;
                managerId = employees.get(managerId).getManagerId();
            }
            if (reportingLineLength > 4) {
                System.out.println(employee.getFirstName() + " " + employee.getLastName() + " has a reporting line which is too long by " + (reportingLineLength - 4));
            }
        }
    }

    public void buildHierarchyAndCalculateAverageSalary(Map<Integer, Employee> employees) {
        for (Employee employee : employees.values()) {
            if (employee.getManagerId() != null) {
                Employee manager = employees.get(employee.getManagerId());
                manager.getSubordinates().add(employee);
            }
        }
        for (Employee manager : employees.values()) {
            if (!manager.getSubordinates().isEmpty()) {
                BigDecimal totalSalary = BigDecimal.ZERO;
                for (Employee subordinate : manager.getSubordinates()) {
                    totalSalary = totalSalary.add(subordinate.getSalary());
                }
                BigDecimal averageSalary = totalSalary.divide(new BigDecimal(manager.getSubordinates().size()), 2);
                manager.setAverageSubordinateSalary(averageSalary);
            }
        }
    }

    public void checkSalaries(Map<Integer, Employee> employees) {
        for (Employee manager : employees.values()) {
            if (manager.getAverageSubordinateSalary() != null) {
                BigDecimal minSalary = manager.getAverageSubordinateSalary().multiply(new BigDecimal("1.2"));
                BigDecimal maxSalary = manager.getAverageSubordinateSalary().multiply(new BigDecimal("1.5"));
                if (manager.getSalary().compareTo(minSalary) < 0) {
                    System.out.println(manager.getFirstName() + " " + manager.getLastName() + " earns less than they should by " + (minSalary.subtract(manager.getSalary())));
                } else if (manager.getSalary().compareTo(maxSalary) > 0) {
                    System.out.println(manager.getFirstName() + " " + manager.getLastName() + " earns more than they should by " + (manager.getSalary().subtract(maxSalary)));
                }
            }
        }
    }

    private InputStream getInputStream(String filename) throws FileNotFoundException {
        InputStream in = getClass().getResourceAsStream("/" + filename);
        if (in == null) {
            throw new FileNotFoundException("File not found: " + filename);
        }
        return in;
    }
}
