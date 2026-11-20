
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DepartmentManagementPanel extends JPanel {

    private final EmployeeService service;
    private DefaultTableModel tableModel;
    private JTable table;

    private JTextField idField;
    private JTextField nameField;

    private static final String[] COLUMN_NAMES = { "Department ID", "Department Name" };

    public DepartmentManagementPanel(EmployeeService service) {
        this.service = service;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        initUI();
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Department> departments = service.getAllDepartments();
        for (Department d : departments) {
            tableModel.addRow(new Object[] { d.getId(), d.getName() });
        }
    }

    private void initUI() {
        JLabel title = new JLabel("Manage Departments");
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
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    idField.setText(String.valueOf(tableModel.getValueAt(row, 0)));
                    nameField.setText((String) tableModel.getValueAt(row, 1));
                }
            }
        });
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add / Update Department"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Department ID:"), gbc);
        idField = new JTextField(10);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Department Name:"), gbc);
        nameField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton addButton = new JButton("Add Department");
        JButton deleteButton = new JButton("Delete Selected");
        JButton clearButton = new JButton("Clear");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addDepartment());
        deleteButton.addActionListener(e -> deleteDepartment());
        clearButton.addActionListener(e -> {
            idField.setText("");
            nameField.setText("");
            table.clearSelection();
        });

        refreshTable();
    }

    private void addDepartment() {
        try {
            if (idField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter both department ID and name.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            service.addDepartment(new Department(id, name));
            FileHandler.saveDepartments(service.getAllDepartments());
            refreshTable();
            idField.setText("");
            nameField.setText("");
            JOptionPane.showMessageDialog(this,
                    "Department added successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid integer ID.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDepartment() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a department to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (Integer) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete department '" + name + "' (ID: " + id + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean removed = service.deleteDepartment(id);
            if (removed) {
                try {
                    FileHandler.saveDepartments(service.getAllDepartments());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Deleted but failed to save: " + ex.getMessage(),
                            "Save Error", JOptionPane.WARNING_MESSAGE);
                }
                refreshTable();
                idField.setText("");
                nameField.setText("");
                JOptionPane.showMessageDialog(this,
                        "Department deleted successfully.",
                        "Deleted", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete department.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}