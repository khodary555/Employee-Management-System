
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeService {

    private List<Employee> employees;
    private List<Department> departments;

    public EmployeeService() {
        employees = new ArrayList<>();
        departments = new ArrayList<>();
        seedDefaultDepartments();
    }

    private void seedDefaultDepartments() {
        addDepartment(new Department(1, "IT"));
        addDepartment(new Department(2, "Human Resources"));
        addDepartment(new Department(3, "Finance"));
        addDepartment(new Department(4, "Marketing"));
        addDepartment(new Department(5, "Sales"));
    }

    // ---------- Employee operations ----------

    public void addEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }
        if (employees.stream().anyMatch(e -> e.getId() == employee.getId())) {
            throw new IllegalArgumentException("Employee ID " + employee.getId() + " already exists.");
        }
        employees.add(employee);
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    public Employee findEmployeeById(int id) {
        for (Employee emp : employees) {
            if (emp.getId() == id) {
                return emp;
            }
        }
        return null;
    }

    public List<Employee> searchByDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String target = department.trim().toLowerCase();
        return employees.stream()
                .filter(e -> e.getDepartment() != null
                        && e.getDepartment().toLowerCase().equals(target))
                .collect(Collectors.toList());
    }

    public void updateEmployee(int originalId, Employee updatedEmployee) {
        if (updatedEmployee == null) {
            throw new IllegalArgumentException("Updated employee cannot be null.");
        }
        int index = -1;
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == originalId) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new IllegalArgumentException("Employee with ID " + originalId + " not found.");
        }
        if (updatedEmployee.getId() != originalId) {
            for (Employee emp : employees) {
                if (emp.getId() == updatedEmployee.getId() && emp != employees.get(index)) {
                    throw new IllegalArgumentException("Employee ID " + updatedEmployee.getId() + " already exists.");
                }
            }
        }
        employees.set(index, updatedEmployee);
    }

    public boolean deleteEmployee(int id) {
        return employees.removeIf(emp -> emp.getId() == id);
    }

    public int getEmployeeCount() {
        return employees.size();
    }

    // ---------- Department operations ----------

    public List<Department> getAllDepartments() {
        return new ArrayList<>(departments);
    }

    public List<String> getDepartmentNames() {
        return departments.stream().map(Department::getName).collect(Collectors.toList());
    }

    public Department findDepartmentById(int id) {
        for (Department d : departments) {
            if (d.getId() == id)
                return d;
        }
        return null;
    }

    public Department findDepartmentByName(String name) {
        if (name == null)
            return null;
        for (Department d : departments) {
            if (d.getName().equalsIgnoreCase(name))
                return d;
        }
        return null;
    }

    public void addDepartment(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("Department cannot be null.");
        }
        if (departments.stream().anyMatch(d -> d.getId() == department.getId())) {
            throw new IllegalArgumentException("Department ID " + department.getId() + " already exists.");
        }
        if (departments.stream().anyMatch(d -> d.getName().equalsIgnoreCase(department.getName()))) {
            throw new IllegalArgumentException("Department name '" + department.getName() + "' already exists.");
        }
        departments.add(department);
    }

    public boolean deleteDepartment(int id) {
        return departments.removeIf(d -> d.getId() == id);
    }

    public void loadData() {
        try {
            var emps = FileHandler.loadEmployees();
            var depts = FileHandler.loadDepartments();

            employees.clear();
            departments.clear();

            for (Department d : depts) {
                departments.add(d);
            }
            for (Employee e : emps) {
                employees.add(e);
            }
        } catch (Exception e) {
            // First launch or missing file — start empty
            employees.clear();
            departments.clear();
        }
    }

    public void saveData() {
        try {
            FileHandler.saveEmployees(new ArrayList<>(employees));
            FileHandler.saveDepartments(new ArrayList<>(departments));
        } catch (Exception e) {

        }
    }
}