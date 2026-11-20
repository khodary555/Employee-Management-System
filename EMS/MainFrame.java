
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class MainFrame extends JFrame {

    private EmployeeService service;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JLabel welcomeLabel;

    public MainFrame() {
        service = new EmployeeService();
        loadData();

        setTitle("Employee Management System - Dashboard");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveDataAndExit();
            }
        });

        initUI();
        cardLayout.show(contentPanel, "Dashboard");
    }

    private void initUI() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(70, 130, 180));
        topBar.setPreferredSize(new Dimension(getWidth(), 50));

        welcomeLabel = new JLabel("Welcome, Administrator");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        topBar.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(e -> logout());
        topBar.add(logoutButton, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(240, 240, 240));
        navPanel.setPreferredSize(new Dimension(200, getHeight()));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel navTitle = new JLabel("Navigation");
        navTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        navTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        navPanel.add(navTitle);
        navPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnDashboard = createNavButton("Dashboard");
        JButton btnAdd = createNavButton("Add Employee");
        JButton btnView = createNavButton("View Employees");
        JButton btnSearchUpdate = createNavButton("Search & Update");
        JButton btnDelete = createNavButton("Delete Employee");
        JButton btnDept = createNavButton("Departments");

        navPanel.add(btnDashboard);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(btnAdd);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(btnView);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(btnSearchUpdate);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(btnDelete);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(btnDept);
        navPanel.add(Box.createVerticalGlue());

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(new DashboardPanel(), "Dashboard");
        contentPanel.add(new AddEmployeePanel(service), "AddEmployee");
        contentPanel.add(new ViewEmployeesPanel(service), "ViewEmployees");
        contentPanel.add(new SearchUpdatePanel(service), "SearchUpdate");
        contentPanel.add(new DeleteEmployeePanel(service), "DeleteEmployee");
        contentPanel.add(new DepartmentManagementPanel(service), "Departments");

        btnDashboard.addActionListener(e -> cardLayout.show(contentPanel, "Dashboard"));
        btnAdd.addActionListener(e -> {
            cardLayout.show(contentPanel, "AddEmployee");
            ((AddEmployeePanel) contentPanel.getComponent(1)).refreshForm();
        });
        btnView.addActionListener(e -> {
            cardLayout.show(contentPanel, "ViewEmployees");
            ((ViewEmployeesPanel) contentPanel.getComponent(2)).refreshTable();
        });
        btnSearchUpdate.addActionListener(e -> {
            cardLayout.show(contentPanel, "SearchUpdate");
            ((SearchUpdatePanel) contentPanel.getComponent(3)).refreshTable();
        });
        btnDelete.addActionListener(e -> {
            cardLayout.show(contentPanel, "DeleteEmployee");
            ((DeleteEmployeePanel) contentPanel.getComponent(4)).refreshTable();
        });
        btnDept.addActionListener(e -> {
            cardLayout.show(contentPanel, "Departments");
            ((DepartmentManagementPanel) contentPanel.getComponent(5)).refreshTable();
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navPanel, contentPanel);
        splitPane.setDividerLocation(220);
        splitPane.setResizeWeight(0.0);
        splitPane.setContinuousLayout(true);

        add(splitPane, BorderLayout.CENTER);
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(180, 35));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return button;
    }

    private void loadData() {
        try {
            var emps = FileHandler.loadEmployees();
            var depts = FileHandler.loadDepartments();

            EmployeeService newService = new EmployeeService();
            newService.getAllDepartments().clear();
            for (Department d : depts) {
                try {
                    newService.addDepartment(d);
                } catch (IllegalArgumentException ignored) {
                }
            }
            for (Employee e : emps) {
                try {
                    newService.addEmployee(e);
                } catch (IllegalArgumentException ignored) {
                }
            }
            service = newService;
        } catch (Exception ex) {
            System.out.println("Note: " + ex.getMessage());
        }
    }

    private void saveDataAndExit() {
        try {
            FileHandler.saveEmployees(service.getAllEmployees());
            FileHandler.saveDepartments(service.getAllDepartments());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Warning: Could not save data before exit.\n" + ex.getMessage(),
                    "Save Error", JOptionPane.WARNING_MESSAGE);
        }
        System.exit(0);
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?", "Logout",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            saveDataAndExit();
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
            this.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}