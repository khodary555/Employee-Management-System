import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DepartmentManagementPanel extends JPanel {
    private EmployeeService service;
    private JTable table;
    private DefaultTableModel model;
    private JTextField idField = new JTextField(5);
    private JTextField nameField = new JTextField(15);

    public DepartmentManagementPanel(EmployeeService service) {
        this.service = service;
        setLayout(new BorderLayout());

        String[] cols = { "ID", "Name" };
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel();
        form.add(new JLabel("ID:"));
        form.add(idField);
        form.add(new JLabel("Name:"));
        form.add(nameField);
        JButton addBtn = new JButton("Add");
        JButton delBtn = new JButton("Delete Selected");
        JButton clearBtn = new JButton("Clear");
        form.add(addBtn);
        form.add(delBtn);
        form.add(clearBtn);
        add(form, BorderLayout.SOUTH);

        loadData();

        addBtn.addActionListener(e -> addDept());
        delBtn.addActionListener(e -> deleteDept());
        clearBtn.addActionListener(e -> {
            idField.setText("");
            nameField.setText("");
            table.clearSelection();
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = table.getSelectedRow();
                if (r >= 0) {
                    idField.setText(model.getValueAt(r, 0).toString());
                    nameField.setText(model.getValueAt(r, 1).toString());
                }
            }
        });
    }

    public void loadData() {
        model.setRowCount(0);
        List<Department> list = service.getAllDepartments();
        for (Department d : list) {
            model.addRow(new Object[] { d.getId(), d.getName() });
        }
    }

    private void addDept() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            service.addDepartment(new Department(id, name));
            service.saveData();
            loadData();
            idField.setText("");
            nameField.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteDept() {
        int row = table.getSelectedRow();
        if (row < 0)
            return;
        int id = (Integer) model.getValueAt(row, 0);
        service.deleteDepartment(id);
        service.saveData();
        loadData();
    }
}