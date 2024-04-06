package org.example.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Employee {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public List<Employee> getSubordinates() {
        return subordinates;
    }

    public BigDecimal getAverageSubordinateSalary() {
        return averageSubordinateSalary;
    }

    public void setAverageSubordinateSalary(BigDecimal averageSubordinateSalary) {
        this.averageSubordinateSalary = averageSubordinateSalary;
    }

    private int id;
    private String firstName;
    private String lastName;
    private BigDecimal salary;
    private Integer managerId;
    private final List<Employee> subordinates = new ArrayList<>();
    private BigDecimal averageSubordinateSalary;
}
