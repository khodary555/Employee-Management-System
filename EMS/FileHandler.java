package EMS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles reading and writing employee and department data to CSV files.
 * Demonstrates separation of concerns: file handling is isolated from GUI and
 * business logic.
 */
public class FileHandler {

    private static final String EMPLOYEE_FILE = "employees.csv";
    private static final String DEPARTMENT_FILE = "departments.csv";

    // ---------- Employee file operations ----------

    public static void saveEmployees(List<Employee> employees) throws IOException {
        File file = new File(EMPLOYEE_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,fullName,age,department,position,employeeType,field1,field2");
            writer.newLine();
            for (Employee emp : employees) {
                writer.write(emp.toCsvString());
                writer.newLine();
            }
        }
    }

    public static List<Employee> loadEmployees() throws IOException {
        List<Employee> employees = new ArrayList<>();
        File file = new File(EMPLOYEE_FILE);
        if (!file.exists()) {
            return employees; // first launch, no data yet
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                Employee emp = parseEmployeeLine(line);
                if (emp != null) {
                    employees.add(emp);
                }
            }
        }
        return employees;
    }

    private static Employee parseEmployeeLine(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length < 8) {
            return null;
        }
        try {
            int id = Integer.parseInt(parts[0].trim());
            String fullName = parts[1].trim();
            int age = Integer.parseInt(parts[2].trim());
            String department = parts[3].trim();
            String position = parts[4].trim();
            String type = parts[5].trim();

            switch (type) {
                case "Full-Time":
                    double basicSalary = Double.parseDouble(parts[6].trim());
                    double bonus = Double.parseDouble(parts[7].trim());
                    return new FullTimeEmployee(id, fullName, age, department, position, basicSalary, bonus);
                case "Part-Time":
                    double hourlyRate = Double.parseDouble(parts[6].trim());
                    double hoursWorked = Double.parseDouble(parts[7].trim());
                    return new PartTimeEmployee(id, fullName, age, department, position, hourlyRate, hoursWorked);
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // ---------- Department file operations ----------

    public static void saveDepartments(List<Department> departments) throws IOException {
        File file = new File(DEPARTMENT_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,name");
            writer.newLine();
            for (Department dept : departments) {
                writer.write(dept.getId() + "," + dept.getName());
                writer.newLine();
            }
        }
    }

    public static List<Department> loadDepartments() throws IOException {
        List<Department> departments = new ArrayList<>();
        File file = new File(DEPARTMENT_FILE);
        if (!file.exists()) {
            return departments;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",", -1);
                if (parts.length >= 2) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        departments.add(new Department(id, name));
                    } catch (NumberFormatException e) {
                        // skip malformed line
                    }
                }
            }
        }
        return departments;
    }

    public static String getEmployeeFileName() {
        return EMPLOYEE_FILE;
    }

    public static String getDepartmentFileName() {
        return DEPARTMENT_FILE;
    }
}