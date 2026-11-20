
import java.io.Serializable;

public class FullTimeEmployee extends Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private double basicSalary;
    private double bonus;

    public FullTimeEmployee(int id, String fullName, int age, String department,
            String position, double basicSalary, double bonus) {
        super(id, fullName, age, department, position);
        this.basicSalary = basicSalary;
        this.bonus = bonus;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    @Override
    public double calculateSalary() {
        return basicSalary + bonus;
    }

    @Override
    public String getEmployeeType() {
        return "Full-Time";
    }

    @Override
    public String toCsvString() {
        return getId() + "," + getFullName() + "," + getAge() + "," + getDepartment()
                + "," + getPosition() + "," + getEmployeeType() + ","
                + basicSalary + "," + bonus;
    }

    @Override
    public String toString() {
        return "FullTimeEmployee[id=" + getId() + ", name=" + getFullName()
                + ", basicSalary=" + basicSalary + ", bonus=" + bonus
                + ", monthlySalary=" + calculateSalary() + "]";
    }
}