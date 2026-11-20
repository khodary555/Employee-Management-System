import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DeleteEmployeePanel extends JPanel {
    private EmployeeService service;
    private JTable table;
    private DefaultTableModel model;

    public DeleteEmployeePanel(EmployeeService service) {
        this.service = service;
        setLayout(new BorderLayout());

        String[] cols = { "ID", "Name", "Age", "Dept", "Position", "Type", "Salary" };
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btns = new JPanel();
        JButton refresh = new JButton("Refresh");
        JButton delete = new JButton("Delete Selected");
        delete.setBackground(Color.RED);
        delete.setForeground(Color.WHITE);
        btns.add(refresh);
        btns.add(delete);
        add(btns, BorderLayout.SOUTH);

        loadData();

        refresh.addActionListener(e -> loadData());
        delete.addActionListener(e -> deleteSelected());
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

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an employee first!");
            return;
        }

        int id = (Integer) model.getValueAt(row, 0);
        String name = (String) model.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete " + name + " (ID: " + id + ")?", "Confirm", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteEmployee(id);
            service.saveData();
            loadData();
        }
    }
}