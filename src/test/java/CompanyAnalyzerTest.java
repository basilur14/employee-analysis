import org.example.model.Employee;
import org.example.service.CompanyAnalyzer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CompanyAnalyzerTest {

    public static final String FILENAME = "test.csv";

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
}
