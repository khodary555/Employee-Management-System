import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
    private List<Employee> employees = new ArrayList<>();
    private List<Department> departments = new ArrayList<>();

    public EmployeeService() {
        departments.add(new Department(1, "IT"));
        departments.add(new Department(2, "Human Resources"));
        departments.add(new Department(3, "Finance"));
        departments.add(new Department(4, "Marketing"));
        departments.add(new Department(5, "Sales"));
    }

    // ---------- Employees ----------

    public boolean addEmployee(Employee e) {
        for (Employee emp : employees) {
            if (emp.getId() == e.getId()) return false; // duplicate ID
        }
        employees.add(e);
        return true;
    }

    public List<Employee> getAllEmployees() {
        return employees;
    }

    public Employee findEmployeeById(int id) {
        for (Employee e : employees) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    public List<Employee> searchByDepartment(String dept) {
        List<Employee> result = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getDepartment().equalsIgnoreCase(dept)) {
                result.add(e);
            }
        }
        return result;
    }

    public boolean updateEmployee(int oldId, Employee updated) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == oldId) {
                if (updated.getId() != oldId) {
                    for (Employee e : employees) {
                        if (e.getId() == updated.getId()) return false;
                    }
                }
                employees.set(i, updated);
                return true;
            }
        }
        return false; // not found
    }

    public boolean deleteEmployee(int id) {
        return employees.removeIf(e -> e.getId() == id);
    }

    public int getEmployeeCount() {
        return employees.size();
    }

    // ---------- Departments ----------

    public List<Department> getAllDepartments() {
        return departments;
    }

    public List<String> getDepartmentNames() {
        List<String> names = new ArrayList<>();
        for (Department d : departments) names.add(d.getName());
        return names;
    }

    public boolean addDepartment(Department d) {
        for (Department dept : departments) {
            if (dept.getId() == d.getId()) return false;
            if (dept.getName().equalsIgnoreCase(d.getName())) return false;
        }
        departments.add(d);
        return true;
    }

    public boolean deleteDepartment(int id) {
        return departments.removeIf(d -> d.getId() == id);
    }

    // ---------- File handling ----------

    public void loadData() {
        try {
            var emps = FileHandler.loadEmployees();
            var depts = FileHandler.loadDepartments();
            employees.clear();
            departments.clear();
            for (Department d : depts) departments.add(d);
            for (Employee e : emps) employees.add(e);
        } catch (Exception ex) {
            // first launch or missing file — start empty
        }
    }

    public void saveData() {
        try {
            FileHandler.saveEmployees(employees);
            FileHandler.saveDepartments(departments);
        } catch (Exception ex) {
            // ignore
        }
    }
}
