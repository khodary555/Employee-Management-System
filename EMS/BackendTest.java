
import java.util.List;

public class BackendTest {
    public static void main(String[] args) throws Exception {
        EmployeeService service = new EmployeeService();

        // Add employees
        service.addEmployee(new FullTimeEmployee(1, "Alice", 30, "IT", "Developer", 15000, 2000));
        service.addEmployee(new PartTimeEmployee(2, "Bob", 25, "Finance", "Clerk", 100, 80));

        // View all
        System.out.println("All employees:");
        for (Employee e : service.getAllEmployees()) {
            System.out.println(e);
        }

        // Search by department
        System.out.println("\nIT Department:");
        for (Employee e : service.searchByDepartment("IT")) {
            System.out.println(e);
        }

        // Update
        Employee updated = new FullTimeEmployee(1, "Alice Smith", 30, "IT", "Senior Developer", 16000, 3000);
        service.updateEmployee(1, updated);
        System.out.println("\nAfter update:");
        System.out.println(service.findEmployeeById(1));

        // Delete
        service.deleteEmployee(2);
        System.out.println("\nAfter delete - count: " + service.getEmployeeCount());

        // File persistence
        FileHandler.saveEmployees(service.getAllEmployees());
        FileHandler.saveDepartments(service.getAllDepartments());

        List<Employee> loaded = FileHandler.loadEmployees();
        System.out.println("\nLoaded from file: " + loaded.size() + " employees");
        for (Employee e : loaded) {
            System.out.println(e);
        }

        System.out.println("\nBackend test passed!");
    }
}