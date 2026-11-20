
import java.io.Serializable;

import java.io.Serializable;

public abstract class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String fullName;
    private int age;
    private String department;
    private String position;

    public Employee(int id, String fullName, int age, String department, String position) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.department = department;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public abstract double calculateSalary();

    public abstract String getEmployeeType();

    public abstract String toCsvString();

    @Override
    public String toString() {
        return "Employee[id=" + id + ", name=" + fullName + ", age=" + age
                + ", department=" + department + ", position=" + position
                + ", type=" + getEmployeeType() + ", salary=" + calculateSalary() + "]";
    }
}