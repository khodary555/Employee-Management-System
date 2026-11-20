
import java.io.Serializable;

public class PartTimeEmployee extends Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private double hourlyRate;
    private double hoursWorked;

    public PartTimeEmployee(int id, String fullName, int age, String department,
            String position, double hourlyRate, double hoursWorked) {
        super(id, fullName, age, department, position);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    @Override
    public double calculateSalary() {
        return hourlyRate * hoursWorked;
    }

    @Override
    public String getEmployeeType() {
        return "Part-Time";
    }

    @Override
    public String toCsvString() {
        return getId() + "," + getFullName() + "," + getAge() + "," + getDepartment()
                + "," + getPosition() + "," + getEmployeeType() + ","
                + hourlyRate + "," + hoursWorked;
    }

    @Override
    public String toString() {
        return "PartTimeEmployee[id=" + getId() + ", name=" + getFullName()
                + ", hourlyRate=" + hourlyRate + ", hoursWorked=" + hoursWorked
                + ", monthlySalary=" + calculateSalary() + "]";
    }
}