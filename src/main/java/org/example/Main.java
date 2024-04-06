package org.example;

import org.example.model.Employee;
import org.example.service.CompanyAnalyzer;

import java.io.IOException;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello employee-analysis!");

        CompanyAnalyzer analyzer = new CompanyAnalyzer();
        try {
            Map<Integer, Employee> employees = analyzer.readEmployees("employees.csv");
            analyzer.buildHierarchyAndCalculateAverageSalary(employees);
            analyzer.checkSalaries(employees);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}