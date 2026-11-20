
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class SearchUpdatePanel extends JPanel {

    private final EmployeeService service;

    private JTextField searchField;
    private JComboBox<String> searchByCombo;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField idField;
    private JTextField nameField;
    private JTextField ageField;
    private JComboBox<String> departmentCombo;
    private JTextField positionField;
    private JComboBox<String> typeCombo;
    private JTextField field1Field;
    private JTextField field2Field;

    private int selectedEmployeeId = -1;

    public SearchUpdatePanel(EmployeeService service) {
        this.service = service;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        initUI();
    }

    public void refreshTable() {
        loadEmployees(service.getAllEmployees());
    }

    private void initUI() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Employees"));

        searchByCombo = new JComboBox<>(new String[] { "Employee ID", "Department" });
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All");

        searchPanel.add(new JLabel("Search by:"));
        searchPanel.add(searchByCombo);
        searchPanel.add(new JLabel("Value:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        add(searchPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[] { "ID", "Full Name", "Age", "Department", "Position", "Type", "Monthly Salary" }, 0) {
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
                    loadSelectedEmployee(row);
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel editPanel = new JPanel(new GridBagLayout());
        editPanel.setBorder(BorderFactory.createTitledBorder("Update Selected Employee"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        editPanel.add(new JLabel("Employee ID:"), gbc);
        idField = new JTextField(12);
        idField.setEditable(false);
        gbc.gridx = 1;
        editPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        editPanel.add(new JLabel("Full Name:"), gbc);
        nameField = new JTextField(12);
        gbc.gridx = 1;
        editPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        editPanel.add(new JLabel("Age:"), gbc);
        ageField = new JTextField(12);
        gbc.gridx = 1;
        editPanel.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        editPanel.add(new JLabel("Department:"), gbc);
        departmentCombo = new JComboBox<>();
        refreshDepartments();
        gbc.gridx = 1;
        editPanel.add(departmentCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        editPanel.add(new JLabel("Position:"), gbc);
        positionField = new JTextField(12);
        gbc.gridx = 1;
        editPanel.add(positionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        editPanel.add(new JLabel("Employee Type:"), gbc);
        typeCombo = new JComboBox<>(new String[] { "Full-Time", "Part-Time" });
        typeCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                updateTypeFieldLabels();
            }
        });
        gbc.gridx = 1;
        editPanel.add(typeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        editPanel.add(new JLabel("Field 1:"), gbc);
        field1Field = new JTextField(12);
        gbc.gridx = 1;
        editPanel.add(field1Field, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        editPanel.add(new JLabel("Field 2:"), gbc);
        field2Field = new JTextField(12);
        gbc.gridx = 1;
        editPanel.add(field2Field, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton updateButton = new JButton("Update Employee");
        JButton clearButton = new JButton("Clear Selection");
        buttonPanel.add(updateButton);
        buttonPanel.add(clearButton);
        editPanel.add(buttonPanel, gbc);

        add(editPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> performSearch());
        showAllButton.addActionListener(e -> {
            searchField.setText("");
            refreshTable();
        });
        updateButton.addActionListener(e -> updateEmployee());
        clearButton.addActionListener(e -> clearSelection());

        refreshTable();
        updateTypeFieldLabels();
    }

    private void refreshDepartments() {
        departmentCombo.removeAllItems();
        List<String> names = service.getDepartmentNames();
        for (String name : names) {
            departmentCombo.addItem(name);
        }
    }

    private void loadEmployees(List<Employee> employees) {
        tableModel.setRowCount(0);
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

    private void performSearch() {
        String value = searchField.getText().trim();
        if (value.isEmpty()) {
            refreshTable();
            return;
        }
        if ("Employee ID".equals(searchByCombo.getSelectedItem())) {
            try {
                int id = Integer.parseInt(value);
                Employee emp = service.findEmployeeById(id);
                if (emp != null) {
                    List<Employee> list = new java.util.ArrayList<>();
                    list.add(emp);
                    loadEmployees(list);
                } else {
                    loadEmployees(java.util.Collections.emptyList());
                    JOptionPane.showMessageDialog(this,
                            "No employee found with ID " + id,
                            "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid integer ID.",
                        "Search Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            List<Employee> results = service.searchByDepartment(value);
            loadEmployees(results);
            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No employees found in department '" + value + "'.",
                        "Search Result", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void loadSelectedEmployee(int row) {
        int id = (Integer) tableModel.getValueAt(row, 0);
        Employee emp = service.findEmployeeById(id);
        if (emp == null)
            return;

        selectedEmployeeId = emp.getId();
        idField.setText(String.valueOf(emp.getId()));
        nameField.setText(emp.getFullName());
        ageField.setText(String.valueOf(emp.getAge()));
        departmentCombo.setSelectedItem(emp.getDepartment());
        positionField.setText(emp.getPosition());

        if (emp instanceof FullTimeEmployee) {
            typeCombo.setSelectedItem("Full-Time");
            field1Field.setText(String.format("%.2f", ((FullTimeEmployee) emp).getBasicSalary()));
            field2Field.setText(String.format("%.2f", ((FullTimeEmployee) emp).getBonus()));
        } else if (emp instanceof PartTimeEmployee) {
            typeCombo.setSelectedItem("Part-Time");
            field1Field.setText(String.format("%.2f", ((PartTimeEmployee) emp).getHourlyRate()));
            field2Field.setText(String.format("%.2f", ((PartTimeEmployee) emp).getHoursWorked()));
        }
        updateTypeFieldLabels();
    }

    private void updateTypeFieldLabels() {
        JPanel parent = (JPanel) field1Field.getParent();
        String type = (String) typeCombo.getSelectedItem();
        for (Component c : parent.getComponents()) {
            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                String text = label.getText();
                if ("Field 1:".equals(text)) {
                    label.setText("Full-Time".equals(type) ? "Basic Salary:" : "Hourly Rate:");
                } else if ("Field 2:".equals(text)) {
                    label.setText("Full-Time".equals(type) ? "Bonus:" : "Hours Worked:");
                }
            }
        }
    }

    private void updateEmployee() {
        if (selectedEmployeeId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an employee from the table first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            if (nameField.getText().trim().isEmpty()
                    || ageField.getText().trim().isEmpty()
                    || positionField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill in all required fields.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            String department = (String) departmentCombo.getSelectedItem();
            String position = positionField.getText().trim();
            String type = (String) typeCombo.getSelectedItem();

            if (age <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Age must be a positive integer.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Employee updated;
            if ("Full-Time".equals(type)) {
                if (field1Field.getText().trim().isEmpty()
                        || field2Field.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Please enter basic salary and bonus.",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double basic = Double.parseDouble(field1Field.getText().trim());
                double bonus = Double.parseDouble(field2Field.getText().trim());
                if (basic < 0 || bonus < 0) {
                    JOptionPane.showMessageDialog(this,
                            "Salary and bonus must be non-negative.",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                updated = new FullTimeEmployee(id, name, age, department, position, basic, bonus);
            } else {
                if (field1Field.getText().trim().isEmpty()
                        || field2Field.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Please enter hourly rate and hours worked.",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double rate = Double.parseDouble(field1Field.getText().trim());
                double hours = Double.parseDouble(field2Field.getText().trim());
                if (rate < 0 || hours < 0) {
                    JOptionPane.showMessageDialog(this,
                            "Hourly rate and hours must be non-negative.",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                updated = new PartTimeEmployee(id, name, age, department, position, rate, hours);
            }

            service.updateEmployee(selectedEmployeeId, updated);
            FileHandler.saveEmployees(service.getAllEmployees());

            JOptionPane.showMessageDialog(this,
                    "Employee updated successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
            clearSelection();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numeric values.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error updating employee: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearSelection() {
        selectedEmployeeId = -1;
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        positionField.setText("");
        field1Field.setText("");
        field2Field.setText("");
        table.clearSelection();
    }
}