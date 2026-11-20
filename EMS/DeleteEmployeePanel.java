
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DeleteEmployeePanel extends JPanel {

    private final EmployeeService service;
    private DefaultTableModel tableModel;
    private JTable table;

    private static final String[] COLUMN_NAMES = {
            "ID", "Full Name", "Age", "Department", "Position", "Type", "Monthly Salary"
    };

    public DeleteEmployeePanel(EmployeeService service) {
        this.service = service;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Delete Employee");
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

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh");
        JButton deleteButton = new JButton("Delete Selected Employee");
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);

        refreshButton.addActionListener(e -> refreshTable());
        deleteButton.addActionListener(e -> deleteSelected());

        bottom.add(refreshButton);
        bottom.add(deleteButton);
        add(bottom, BorderLayout.SOUTH);

        refreshTable();
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Employee> employees = service.getAllEmployees();
        for (Employee emp : employees) {
            tableModel.addRow(new Object[] {
                    emp.getId(),
                    emp.getFullName(),
                    emp.getAge(),
                    emp.getDepartment(),
                    emp.getPosition(),
                    emp.getEmployeeType(),
                    String.format("%.2f", emp.calculateSalary())
            });
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an employee from the table to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (Integer) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete employee:\nID: " + id + "\nName: " + name + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean removed = service.deleteEmployee(id);
            if (removed) {
                try {
                    FileHandler.saveEmployees(service.getAllEmployees());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Employee deleted but failed to save: " + ex.getMessage(),
                            "Save Error", JOptionPane.WARNING_MESSAGE);
                }
                JOptionPane.showMessageDialog(this,
                        "Employee deleted successfully.",
                        "Deleted", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete employee.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}