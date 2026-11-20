import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class SearchUpdatePanel extends JPanel {
    private EmployeeService service;
    private JTable table;
    private DefaultTableModel model;
    private int selectedId = -1;

    private JTextField idField = new JTextField(10);
    private JTextField nameField = new JTextField(15);
    private JTextField ageField = new JTextField(5);
    private JComboBox<String> deptCombo = new JComboBox<>();
    private JTextField posField = new JTextField(15);
    private JComboBox<String> typeCombo = new JComboBox<>(new String[] { "Full-Time", "Part-Time" });
    private JTextField f1Field = new JTextField(10);
    private JTextField f2Field = new JTextField(10);

    public SearchUpdatePanel(EmployeeService service) {
        this.service = service;
        setLayout(new BorderLayout());

        // Search area
        JPanel search = new JPanel();
        JTextField searchBox = new JTextField(15);
        JComboBox<String> searchBy = new JComboBox<>(new String[] { "ID", "Department" });
        JButton searchBtn = new JButton("Search");
        JButton showAllBtn = new JButton("Show All");
        search.add(new JLabel("Search by:"));
        search.add(searchBy);
        search.add(new JLabel("Value:"));
        search.add(searchBox);
        search.add(searchBtn);
        search.add(showAllBtn);
        add(search, BorderLayout.NORTH);

        // Table
        String[] cols = { "ID", "Name", "Age", "Dept", "Position", "Type", "Salary" };
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0)
                    loadRow(row);
            }
        });
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Edit form
        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.add(new JLabel("ID:"));
        form.add(idField);
        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Age:"));
        form.add(ageField);
        form.add(new JLabel("Dept:"));
        form.add(deptCombo);
        form.add(new JLabel("Position:"));
        form.add(posField);
        form.add(new JLabel("Type:"));
        form.add(typeCombo);
        form.add(new JLabel("Field 1:"));
        form.add(f1Field);
        form.add(new JLabel("Field 2:"));
        form.add(f2Field);
        add(form, BorderLayout.SOUTH);

        JButton updateBtn = new JButton("Update");
        JButton clearBtn = new JButton("Clear");
        JPanel btns = new JPanel();
        btns.add(updateBtn);
        btns.add(clearBtn);
        add(btns, BorderLayout.PAGE_END);

        loadDepartments();
        loadData();

        searchBtn.addActionListener(e -> doSearch(searchBox.getText(), (String) searchBy.getSelectedItem()));
        showAllBtn.addActionListener(e -> loadData());
        updateBtn.addActionListener(e -> updateEmployee());
        clearBtn.addActionListener(e -> clearSelection());

        typeCombo.addActionListener(e -> {
            String t = (String) typeCombo.getSelectedItem();
            if ("Full-Time".equals(t)) {
                // You can relabel f1/f2 here if desired
            }
        });
    }

    private void loadDepartments() {
        deptCombo.removeAllItems();
        for (String d : service.getDepartmentNames())
            deptCombo.addItem(d);
    }

    public void loadData() {
        model.setRowCount(0);
        for (Employee e : service.getAllEmployees()) {
            model.addRow(new Object[] {
                    e.getId(), e.getFullName(), e.getAge(),
                    e.getDepartment(), e.getPosition(),
                    e.getEmployeeType(), String.format("%.2f", e.calculateSalary())
            });
        }
    }

    private void doSearch(String val, String by) {
        model.setRowCount(0);
        if ("ID".equals(by)) {
            try {
                Employee e = service.findEmployeeById(Integer.parseInt(val));
                if (e != null)
                    model.addRow(new Object[] {
                            e.getId(), e.getFullName(), e.getAge(),
                            e.getDepartment(), e.getPosition(),
                            e.getEmployeeType(), String.format("%.2f", e.calculateSalary())
                    });
            } catch (Exception ex) {
            }
        } else {
            List<Employee> list = service.searchByDepartment(val);
            for (Employee e : list) {
                model.addRow(new Object[] {
                        e.getId(), e.getFullName(), e.getAge(),
                        e.getDepartment(), e.getPosition(),
                        e.getEmployeeType(), String.format("%.2f", e.calculateSalary())
                });
            }
        }
    }

    private void loadRow(int row) {
        selectedId = (Integer) model.getValueAt(row, 0);
        Employee e = service.findEmployeeById(selectedId);
        if (e == null)
            return;

        idField.setText(String.valueOf(e.getId()));
        nameField.setText(e.getFullName());
        ageField.setText(String.valueOf(e.getAge()));
        deptCombo.setSelectedItem(e.getDepartment());
        posField.setText(e.getPosition());
        typeCombo.setSelectedItem(e.getEmployeeType());

        if (e instanceof FullTimeEmployee) {
            f1Field.setText(String.valueOf(((FullTimeEmployee) e).getBasicSalary()));
            f2Field.setText(String.valueOf(((FullTimeEmployee) e).getBonus()));
        } else if (e instanceof PartTimeEmployee) {
            f1Field.setText(String.valueOf(((PartTimeEmployee) e).getHourlyRate()));
            f2Field.setText(String.valueOf(((PartTimeEmployee) e).getHoursWorked()));
        }
    }

    private void updateEmployee() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String dept = (String) deptCombo.getSelectedItem();
            String pos = posField.getText();
            String type = (String) typeCombo.getSelectedItem();

            Employee emp;
            if ("Full-Time".equals(type)) {
                emp = new FullTimeEmployee(id, name, age, dept, pos,
                        Double.parseDouble(f1Field.getText()),
                        Double.parseDouble(f2Field.getText()));
            } else {
                emp = new PartTimeEmployee(id, name, age, dept, pos,
                        Double.parseDouble(f1Field.getText()),
                        Double.parseDouble(f2Field.getText()));
            }

            service.updateEmployee(selectedId, emp);
            service.saveData();
            loadData();
            clearSelection();
            JOptionPane.showMessageDialog(this, "Updated!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearSelection() {
        selectedId = -1;
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        posField.setText("");
        f1Field.setText("");
        f2Field.setText("");
        table.clearSelection();
    }
}