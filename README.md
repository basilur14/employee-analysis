# Organizational Structure Analyzer

## Overview

The Organizational Structure Analyzer is a Java program designed to analyze the organizational structure of a company based on the information provided in a CSV file. It identifies potential improvements such as managers earning less or more than they should and employees with reporting lines that are too long.

## Features

- Reads employee data from a CSV file.
- Analyzes the organizational structure to identify:
    - Managers earning less than they should.
    - Managers earning more than they should.
    - Employees with reporting lines that are too long.
- Outputs analysis results to the console.

## Usage

### Input

The program expects a CSV file containing employee data with the following format:
Id,firstName,lastName,salary,managerId


- `Id`: Unique identifier for each employee.
- `firstName`: First name of the employee.
- `lastName`: Last name of the employee.
- `salary`: Salary of the employee.
- `managerId`: ID of the manager of the employee. CEO has no manager specified.

### Output

The program prints analysis results to the console, including:

- Managers earning less than they should, and by how much.
- Managers earning more than they should, and by how much.
- Employees with reporting lines that are too long, and by how much.

### Running the Program

1. Ensure you have Java SE installed on your system.
2. Clone the project repository or download the provided source code.
3. Navigate to the project directory.
4. Compile the source code using Maven: `mvn compile`.
5. Run the program with the CSV file as input: `mvn exec:java -Dexec.args="employees.csv"` (replace `employees.csv` with the path to your CSV file).

## Assumptions

- The CSV file is well-formed and does not contain any errors in formatting.
- All managers listed in the CSV file actually have subordinates.
- The CSV file contains valid integer values for ID, salary, and manager ID fields.
- The organizational structure forms a tree, and there are no cycles.
- The CEO is always present in the CSV file, and their ID is not specified as the manager ID for any other employee.

## Dependencies

- Java SE (Standard Edition)
- JUnit (for testing)
- Maven (for project structure and build)

## Testing

Unit tests have been provided to ensure the correctness and reliability of the program. These tests cover various scenarios and edge cases to validate the functionality of the code.

## Contributing

Contributions to this project are welcome. If you find any issues or have suggestions for improvements, please open an issue or submit a pull request on the project's repository.
