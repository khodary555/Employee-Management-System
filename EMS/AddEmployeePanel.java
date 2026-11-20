
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;

public class AddEmployeePanel extends JPanel {

    private final EmployeeService service;

    private JTextField idField;
    private JTextField nameField;
    private JTextField ageField;
    private JComboBox<String> departmentCombo;
    private JTextField positionField;
    private JComboBox<String> typeCombo;

    private JTextField basicSalaryField;
    private JTextField bonusField;
    private JTextField hourlyRateField;
    private JTextField hoursWorkedField;

    private JPanel typeSpecificPanel;

    public AddEmployeePanel(EmployeeService service) {
        this.service = service;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        initUI();
    }

    public void refreshForm() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        positionField.setText("");
        typeCombo.setSelectedIndex(0);
        updateTypeFields();
        basicSalaryField.setText("");
        bonusField.setText("");
        hourlyRateField.setText("");
        hoursWorkedField.setText("");
    }

    private void initUI() {
        JLabel title = new JLabel("Add New Employee");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Employee ID:"), gbc);
        idField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Full Name:"), gbc);
        nameField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Age:"), gbc);
        ageField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Department:"), gbc);
        departmentCombo = new JComboBox<>();
        refreshDepartments();
        gbc.gridx = 1;
        formPanel.add(departmentCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Position:"), gbc);
        positionField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(positionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Employee Type:"), gbc);
        typeCombo = new JComboBox<>(new String[] { "Full-Time", "Part-Time" });
        typeCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                updateTypeFields();
            }
        });
        gbc.gridx = 1;
        formPanel.add(typeCombo, gbc);

        typeSpecificPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        typeSpecificPanel.setBorder(BorderFactory.createTitledBorder("Salary Information"));

        basicSalaryField = new JTextField(10);
        bonusField = new JTextField(10);
        hourlyRateField = new JTextField(10);
        hoursWorkedField = new JTextField(10);

        typeSpecificPanel.add(new JLabel("Basic Monthly Salary:"));
        typeSpecificPanel.add(basicSalaryField);
        typeSpecificPanel.add(new JLabel("Monthly Bonus:"));
        typeSpecificPanel.add(bonusField);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(typeSpecificPanel, gbc);

        updateTypeFields();

        gbc.gridy = 7;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton addButton = new JButton("Add Employee");
        JButton clearButton = new JButton("Clear");
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.CENTER);

        addButton.addActionListener(e -> addEmployee());
        clearButton.addActionListener(e -> refreshForm());
    }

    private void refreshDepartments() {
        departmentCombo.removeAllItems();
        List<String> names = service.getDepartmentNames();
        for (String name : names) {
            departmentCombo.addItem(name);
        }
    }

    private void updateTypeFields() {
        typeSpecificPanel.removeAll();
        String type = (String) typeCombo.getSelectedItem();

        if ("Full-Time".equals(type)) {
            typeSpecificPanel.add(new JLabel("Basic Monthly Salary:"));
            typeSpecificPanel.add(basicSalaryField);
            typeSpecificPanel.add(new JLabel("Monthly Bonus:"));
            typeSpecificPanel.add(bonusField);
        } else if ("Part-Time".equals(type)) {
            typeSpecificPanel.add(new JLabel("Hourly Rate:"));
            typeSpecificPanel.add(hourlyRateField);
            typeSpecificPanel.add(new JLabel("Hours Worked / Month:"));
            typeSpecificPanel.add(hoursWorkedField);
        }
        typeSpecificPanel.revalidate();
        typeSpecificPanel.repaint();
    }

    private void addEmployee() {
        try {
            if (idField.getText().trim().isEmpty()
                    || nameField.getText().trim().isEmpty()
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

            if (age <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Age must be a positive integer.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String type = (String) typeCombo.getSelectedItem();
            Employee emp;

            if ("Full-Time".equals(type)) {
                if (basicSalaryField.getText().trim().isEmpty()
                        || bonusField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Please enter basic salary and bonus.",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double basic = Double.parseDouble(basicSalaryField.getText().trim());
                double bonus = Double.parseDouble(bonusField.getText().trim());
                if (basic < 0 || bonus < 0) {
                    JOptionPane.showMessageDialog(this,
                            "Salary and bonus must be non-negative.",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                emp = new FullTimeEmployee(id, name, age, department, position, basic, bonus);
            } else {
                if (hourlyRateField.getText().trim().isEmpty()
                        || hoursWorkedField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Please enter hourly rate and hours worked.",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double rate = Double.parseDouble(hourlyRateField.getText().trim());
                double hours = Double.parseDouble(hoursWorkedField.getText().trim());
                if (rate < 0 || hours < 0) {
                    JOptionPane.showMessageDialog(this,
                            "Hourly rate and hours must be non-negative.",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                emp = new PartTimeEmployee(id, name, age, department, position, rate, hours);
            }

            service.addEmployee(emp);
            FileHandler.saveEmployees(service.getAllEmployees());

            JOptionPane.showMessageDialog(this,
                    "Employee added successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshForm();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numeric values for ID, age, and salary fields.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error adding employee: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}