package service;

import org.example.model.Employee;
import org.example.service.CompanyAnalyzer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CompanyAnalyzerTest {

    static final String FILENAME = "test.csv";

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void testReadEmployees() throws IOException {
        CompanyAnalyzer analyzer = new CompanyAnalyzer();

        // Act
        Map<Integer, Employee> employees = analyzer.readEmployees(FILENAME);

        // Assert
        assertEquals(5, employees.size());
        assertEquals("Joe", employees.get(123).getFirstName());
        assertEquals("Doe", employees.get(123).getLastName());
        assertEquals(60000, employees.get(123).getSalary().doubleValue());
        assertNull(employees.get(123).getManagerId());
        assertEquals(123, employees.get(124).getManagerId());
    }

    @Test
    void testBuildHierarchyAndCalculateAverageSalary() {
        // Arrange
        Employee employee1 = new Employee();
        employee1.setId(1);
        employee1.setSalary(BigDecimal.valueOf(60000));

        Employee employee2 = new Employee();
        employee2.setId(2);
        employee2.setSalary(BigDecimal.valueOf(45000));
        employee2.setManagerId(1);

        Employee employee3 = new Employee();
        employee3.setId(3);
        employee3.setSalary(BigDecimal.valueOf(47000));
        employee3.setManagerId(1);

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, employee1);
        employees.put(2, employee2);
        employees.put(3, employee3);

        CompanyAnalyzer analyzer = new CompanyAnalyzer();

        // Act
        analyzer.buildHierarchyAndCalculateAverageSalary(employees);

        // Assert
        assertEquals(2, employees.get(1).getSubordinates().size());
        assertEquals(BigDecimal.valueOf(46000), employees.get(1).getAverageSubordinateSalary());
        assertEquals(0, employees.get(2).getSubordinates().size());
        assertNull(employees.get(2).getAverageSubordinateSalary());
        assertEquals(0, employees.get(3).getSubordinates().size());
        assertNull(employees.get(3).getAverageSubordinateSalary());
    }

    @Test
    void testBuildHierarchyAndCalculateAverageSalary_NoManager() {
        // Arrange
        Employee employee1 = new Employee();
        employee1.setId(1);
        employee1.setSalary(BigDecimal.valueOf(60000));

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, employee1);

        CompanyAnalyzer analyzer = new CompanyAnalyzer();

        // Act
        analyzer.buildHierarchyAndCalculateAverageSalary(employees);

        // Assert
        assertEquals(0, employees.get(1).getSubordinates().size());
        assertNull(employees.get(1).getAverageSubordinateSalary());
    }

    @Test
    void testBuildHierarchyAndCalculateAverageSalary_NoSubordinates() {
        // Arrange
        Employee employee1 = new Employee();
        employee1.setId(1);
        employee1.setSalary(BigDecimal.valueOf(60000));

        Employee employee2 = new Employee();
        employee2.setId(2);
        employee2.setSalary(BigDecimal.valueOf(45000));

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, employee1);
        employees.put(2, employee2);

        CompanyAnalyzer analyzer = new CompanyAnalyzer();

        // Act
        analyzer.buildHierarchyAndCalculateAverageSalary(employees);

        // Assert
        assertEquals(0, employees.get(1).getSubordinates().size());
        assertNull(employees.get(1).getAverageSubordinateSalary());
        assertEquals(0, employees.get(2).getSubordinates().size());
        assertNull(employees.get(2).getAverageSubordinateSalary());
    }

    @Test
    void testCheckSalaries_EarnsLessThanShould() {
        // Arrange
        Employee employee1 = new Employee();
        employee1.setId(1);
        employee1.setFirstName("Manager");
        employee1.setLastName("One");
        employee1.setSalary(BigDecimal.valueOf(6000));
        employee1.setAverageSubordinateSalary(BigDecimal.valueOf(50000));

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, employee1);

        CompanyAnalyzer analyzer = new CompanyAnalyzer();

        // Act
        analyzer.checkSalaries(employees);

        // Assert
        assertEquals("Manager One earns less than they should by 54000.0", outputStreamCaptor.toString().trim());
    }

    @Test
    void testCheckSalaries_EarnsMoreThanShould() {
        // Arrange
        Employee employee1 = new Employee();
        employee1.setId(1);
        employee1.setFirstName("Manager");
        employee1.setLastName("One");
        employee1.setSalary(BigDecimal.valueOf(800000));
        employee1.setAverageSubordinateSalary(BigDecimal.valueOf(50000));

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, employee1);

        CompanyAnalyzer analyzer = new CompanyAnalyzer();

        // Act
        analyzer.checkSalaries(employees);

        // Assert
        assertEquals("Manager One earns more than they should by 725000.0", outputStreamCaptor.toString().trim());
    }

    @Test
    void testCheckSalaries_NoAverageSubordinateSalary() {
        // Arrange
        Employee employee1 = new Employee();
        employee1.setId(1);
        employee1.setFirstName("Manager");
        employee1.setLastName("One");
        employee1.setSalary(BigDecimal.valueOf(60000));

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, employee1);

        CompanyAnalyzer analyzer = new CompanyAnalyzer();

        // Act
        analyzer.checkSalaries(employees);

        // Assert
        assertEquals("", outputStreamCaptor.toString().trim());
    }

    @Test
    void testCheckReportingLine() {
        // Arrange
        Employee employee1 = new Employee();
        employee1.setId(1);
        employee1.setFirstName("Employee");
        employee1.setLastName("One");
        employee1.setSalary(BigDecimal.valueOf(60000));

        Employee employee2 = new Employee();
        employee2.setId(2);
        employee2.setFirstName("Employee");
        employee2.setLastName("Two");
        employee2.setSalary(BigDecimal.valueOf(45000));
        employee2.setManagerId(1);

        Employee employee3 = new Employee();
        employee3.setId(3);
        employee3.setFirstName("Employee");
        employee3.setLastName("Three");
        employee3.setSalary(BigDecimal.valueOf(47000));
        employee3.setManagerId(2);

        Employee employee4 = new Employee();
        employee4.setId(4);
        employee4.setFirstName("Employee");
        employee4.setLastName("Four");
        employee4.setSalary(BigDecimal.valueOf(50000));
        employee4.setManagerId(3);

        Employee employee5 = new Employee();
        employee5.setId(5);
        employee5.setFirstName("Employee");
        employee5.setLastName("Five");
        employee5.setSalary(BigDecimal.valueOf(34000));
        employee5.setManagerId(4);

        Employee employee6 = new Employee();
        employee6.setId(6);
        employee6.setFirstName("Employee");
        employee6.setLastName("Six");
        employee6.setSalary(BigDecimal.valueOf(34000));
        employee6.setManagerId(5);

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, employee1);
        employees.put(2, employee2);
        employees.put(3, employee3);
        employees.put(4, employee4);
        employees.put(5, employee5);
        employees.put(6, employee6);

        CompanyAnalyzer analyzer = new CompanyAnalyzer();

        // Act
        analyzer.checkReportingLine(employees);

        // Assert
        assertEquals("Employee Six has a reporting line which is too long by 1", outputStreamCaptor.toString().trim());
    }
}
