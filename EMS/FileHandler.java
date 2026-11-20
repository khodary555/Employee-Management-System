import java.io.*;
import java.util.*;

public class FileHandler {
    private static final String EMP_FILE = "employees.csv";
    private static final String DEPT_FILE = "departments.csv";

    public static void saveEmployees(List<Employee> employees) throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter(EMP_FILE));
        writer.println("id,fullName,age,department,position,employeeType,field1,field2");
        for (Employee e : employees) {
            writer.println(e.toCsvString());
        }
        writer.close();
    }

    public static List<Employee> loadEmployees() throws Exception {
        List<Employee> list = new ArrayList<>();
        File f = new File(EMP_FILE);
        if (!f.exists()) return list;

        Scanner sc = new Scanner(f);
        sc.nextLine(); // skip header
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.trim().isEmpty()) continue;
            String[] p = line.split(",", -1);
            if (p.length < 8) continue;

            int id = Integer.parseInt(p[0].trim());
            String name = p[1].trim();
            int age = Integer.parseInt(p[2].trim());
            String dept = p[3].trim();
            String pos = p[4].trim();
            String type = p[5].trim();

            if (type.equals("Full-Time")) {
                double b = Double.parseDouble(p[6].trim());
                double bo = Double.parseDouble(p[7].trim());
                list.add(new FullTimeEmployee(id, name, age, dept, pos, b, bo));
            } else if (type.equals("Part-Time")) {
                double r = Double.parseDouble(p[6].trim());
                double h = Double.parseDouble(p[7].trim());
                list.add(new PartTimeEmployee(id, name, age, dept, pos, r, h));
            }
        }
        sc.close();
        return list;
    }

    public static void saveDepartments(List<Department> departments) throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter(DEPT_FILE));
        writer.println("id,name");
        for (Department d : departments) {
            writer.println(d.getId() + "," + d.getName());
        }
        writer.close();
    }

    public static List<Department> loadDepartments() throws Exception {
        List<Department> list = new ArrayList<>();
        File f = new File(DEPT_FILE);
        if (!f.exists()) return list;

        Scanner sc = new Scanner(f);
        sc.nextLine(); // skip header
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.trim().isEmpty()) continue;
            String[] p = line.split(",", -1);
            if (p.length >= 2) {
                int id = Integer.parseInt(p[0].trim());
                String name = p[1].trim();
                list.add(new Department(id, name));
            }
        }
        sc.close();
        return list;
    }
}
