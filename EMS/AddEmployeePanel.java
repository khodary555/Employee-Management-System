import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AddEmployeePanel extends JPanel {
    private EmployeeService service;

    private JTextField idField = new JTextField(10);
    private JTextField nameField = new JTextField(15);
    private JTextField ageField = new JTextField(5);
    private JComboBox<String> deptCombo = new JComboBox<>();
    private JTextField posField = new JTextField(15);
    private JComboBox<String> typeCombo = new JComboBox<>(new String[] { "Full-Time", "Part-Time" });

    private JTextField basicField = new JTextField(10);
    private JTextField bonusField = new JTextField(10);
    private JTextField rateField = new JTextField(10);
    private JTextField hoursField = new JTextField(10);

    private JPanel salaryPanel = new JPanel(new GridLayout(2, 2, 5, 5));

    public AddEmployeePanel(EmployeeService service) {
        this.service = service;
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setLayout(new BorderLayout());

        add(new JLabel("Add New Employee", SwingConstants.CENTER), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(7, 2, 5, 5));
        form.add(new JLabel("ID:"));
        form.add(idField);
        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Age:"));
        form.add(ageField);
        form.add(new JLabel("Department:"));
        form.add(deptCombo);
        form.add(new JLabel("Position:"));
        form.add(posField);
        form.add(new JLabel("Type:"));
        form.add(typeCombo);
        form.add(new JLabel("Salary Info:"));
        form.add(salaryPanel);

        add(form, BorderLayout.CENTER);

        JPanel btns = new JPanel();
        JButton saveBtn = new JButton("Save");
        JButton clearBtn = new JButton("Clear");
        btns.add(saveBtn);
        btns.add(clearBtn);
        add(btns, BorderLayout.SOUTH);

        // Show Full-Time fields by default
        updateSalaryFields();

        typeCombo.addActionListener(e -> updateSalaryFields());

        saveBtn.addActionListener(e -> saveEmployee());
        clearBtn.addActionListener(e -> clearForm());

        loadDepartments();
    }

    private void loadDepartments() {
        deptCombo.removeAllItems();
        for (String d : service.getDepartmentNames()) {
            deptCombo.addItem(d);
        }
    }

    private void updateSalaryFields() {
        salaryPanel.removeAll();
        String type = (String) typeCombo.getSelectedItem();
        if ("Full-Time".equals(type)) {
            salaryPanel.add(new JLabel("Basic Salary:"));
            salaryPanel.add(basicField);
            salaryPanel.add(new JLabel("Bonus:"));
            salaryPanel.add(bonusField);
        } else {
            salaryPanel.add(new JLabel("Hourly Rate:"));
            salaryPanel.add(rateField);
            salaryPanel.add(new JLabel("Hours/Month:"));
            salaryPanel.add(hoursField);
        }
        salaryPanel.revalidate();
        salaryPanel.repaint();
    }

    private void saveEmployee() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            String dept = (String) deptCombo.getSelectedItem();
            String pos = posField.getText().trim();

            if (name.isEmpty() || pos.isEmpty() || age <= 0) {
                JOptionPane.showMessageDialog(this, "Fill all fields correctly!");
                return;
            }

            Employee emp;
            String type = (String) typeCombo.getSelectedItem();

            if ("Full-Time".equals(type)) {
                double basic = Double.parseDouble(basicField.getText());
                double bonus = Double.parseDouble(bonusField.getText());
                emp = new FullTimeEmployee(id, name, age, dept, pos, basic, bonus);
            } else {
                double rate = Double.parseDouble(rateField.getText());
                double hours = Double.parseDouble(hoursField.getText());
                emp = new PartTimeEmployee(id, name, age, dept, pos, rate, hours);
            }

            service.addEmployee(emp);
            service.saveData();
            JOptionPane.showMessageDialog(this, "Employee added!");
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        posField.setText("");
        basicField.setText("");
        bonusField.setText("");
        rateField.setText("");
        hoursField.setText("");
    }
}