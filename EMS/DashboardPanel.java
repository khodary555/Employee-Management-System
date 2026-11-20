import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("Employee Management System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel msg = new JLabel("Use the left menu to manage employees.", SwingConstants.CENTER);

        add(title, BorderLayout.NORTH);
        add(msg, BorderLayout.CENTER);
    }
}