import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewEmployeesPanel extends JPanel {
    private EmployeeService service;
    private JTable table;
    private DefaultTableModel model;

    public ViewEmployeesPanel(EmployeeService service) {
        this.service = service;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = { "ID", "Name", "Age", "Dept", "Position", "Type", "Salary" };
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> loadData());
        add(refresh, BorderLayout.SOUTH);

        loadData();
    }

    public void loadData() {
        model.setRowCount(0);
        List<Employee> list = service.getAllEmployees();
        for (Employee e : list) {
            model.addRow(new Object[] {
                    e.getId(), e.getFullName(), e.getAge(),
                    e.getDepartment(), e.getPosition(),
                    e.getEmployeeType(), String.format("%.2f", e.calculateSalary())
            });
        }
    }
}