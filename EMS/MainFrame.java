import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {

    private EmployeeService service;
    private CardLayout cards;
    private JPanel center;

    public MainFrame() {
        service = new EmployeeService();
        service.loadData();

        setTitle("Employee Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Welcome, Administrator"), BorderLayout.WEST);
        JButton logoutBtn = new JButton("Logout");
        top.add(logoutBtn, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        JButton btnDashboard = new JButton("Dashboard");
        JButton btnAdd = new JButton("Add Employee");
        JButton btnView = new JButton("View Employees");
        JButton btnSearch = new JButton("Search & Update");
        JButton btnDelete = new JButton("Delete Employee");
        JButton btnDept = new JButton("Departments");
        nav.add(btnDashboard);
        nav.add(btnAdd);
        nav.add(btnView);
        nav.add(btnSearch);
        nav.add(btnDelete);
        nav.add(btnDept);
        add(nav, BorderLayout.WEST);

        cards = new CardLayout();
        center = new JPanel(cards);
        center.add(new DashboardPanel(), "dashboard");
        center.add(new AddEmployeePanel(service), "add");
        center.add(new ViewEmployeesPanel(service), "view");
        center.add(new SearchUpdatePanel(service), "search");
        center.add(new DeleteEmployeePanel(service), "delete");
        center.add(new DepartmentManagementPanel(service), "dept");
        add(center, BorderLayout.CENTER);

        // Buttons
        btnDashboard.addActionListener(e -> cards.show(center, "dashboard"));
        btnAdd.addActionListener(e -> cards.show(center, "add"));
        btnView.addActionListener(e -> {
            cards.show(center, "view");
            ((ViewEmployeesPanel) getPanel("view")).loadData();
        });
        btnSearch.addActionListener(e -> {
            cards.show(center, "search");
            ((SearchUpdatePanel) getPanel("search")).loadData();
        });
        btnDelete.addActionListener(e -> {
            cards.show(center, "delete");
            ((DeleteEmployeePanel) getPanel("delete")).loadData();
        });
        btnDept.addActionListener(e -> {
            cards.show(center, "dept");
            ((DepartmentManagementPanel) getPanel("dept")).loadData();
        });

        logoutBtn.addActionListener(e -> {
            service.saveData();
            new LoginFrame().setVisible(true);
            dispose();
        });

        cards.show(center, "dashboard");

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                service.saveData();
                System.exit(0);
            }
        });
    }

    private JPanel getPanel(String name) {
        for (Component c : center.getComponents()) {
            if (c instanceof JPanel) {
                return (JPanel) c;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}