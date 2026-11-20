
import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Employee Management System");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Dashboard");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcome = new JLabel("Welcome, Administrator");
        welcome.setFont(new Font("SansSerif", Font.PLAIN, 16));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel info = new JLabel(
                "<html><body style='text-align:center;'>" +
                        "Use the navigation panel on the left to:<br>" +
                        "&bull; Add new employees<br>" +
                        "&bull; View all registered employees<br>" +
                        "&bull; Search and update employee records<br>" +
                        "&bull; Delete employees<br>" +
                        "&bull; Manage departments<br><br>" +
                        "All data is automatically saved to files and loaded on startup.</body></html>");
        info.setFont(new Font("SansSerif", Font.PLAIN, 14));
        info.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(title);
        center.add(Box.createRigidArea(new Dimension(0, 15)));
        center.add(subtitle);
        center.add(Box.createRigidArea(new Dimension(0, 10)));
        center.add(welcome);
        center.add(Box.createRigidArea(new Dimension(0, 30)));
        center.add(info);

        add(center, BorderLayout.CENTER);
    }
}