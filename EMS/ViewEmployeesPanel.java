
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewEmployeesPanel extends JPanel {

    private final EmployeeService service;
    private DefaultTableModel tableModel;
    private JTable table;

    private static final String[] COLUMN_NAMES = {
            "ID", "Full Name", "Age", "Department", "Position",
            "Type", "Basic/Hourly", "Bonus/Hours", "Monthly Salary"
    };

    public ViewEmployeesPanel(EmployeeService service) {
        this.service = service;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("All Employees");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshTable());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(refreshButton);
        add(bottom, BorderLayout.SOUTH);

        refreshTable();
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Employee> employees = service.getAllEmployees();
        for (Employee emp : employees) {
            Object[] row = buildRow(emp);
            tableModel.addRow(row);
        }
    }

    private Object[] buildRow(Employee emp) {
        String typeSpecific1 = "";
        String typeSpecific2 = "";
        if (emp instanceof FullTimeEmployee ft) {
            typeSpecific1 = String.format("%.2f", ft.getBasicSalary());
            typeSpecific2 = String.format("%.2f", ft.getBonus());
        } else if (emp instanceof PartTimeEmployee pt) {
            typeSpecific1 = String.format("%.2f", pt.getHourlyRate());
            typeSpecific2 = String.format("%.2f", pt.getHoursWorked());
        }
        return new Object[] {
                emp.getId(),
                emp.getFullName(),
                emp.getAge(),
                emp.getDepartment(),
                emp.getPosition(),
                emp.getEmployeeType(),
                typeSpecific1,
                typeSpecific2,
                String.format("%.2f", emp.calculateSalary())
        };
    }
}