package fr.istorejava.ui;

import javax.swing.*;
import java.awt.*;
import fr.istorejava.data.UserData;
import fr.istorejava.data.StoreData;
import fr.istorejava.ui.AdminDashboard;


public class Home extends JFrame {

    public Home(UserData user, StoreData store) {
        setTitle("Home");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // ===== LEFT PANEL =====
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(0, 102, 102));
        leftPanel.setLayout(new BorderLayout());

        JLabel companyLabel = new JLabel("SupIStore", SwingConstants.CENTER);
        companyLabel.setForeground(Color.WHITE);
        companyLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel copyright = new JLabel(
                "copyright © SupIStore All rights reserved",
                SwingConstants.CENTER
        );
        copyright.setForeground(Color.LIGHT_GRAY);
        copyright.setFont(new Font("Arial", Font.PLAIN, 10));

        leftPanel.add(companyLabel, BorderLayout.CENTER);
        leftPanel.add(copyright, BorderLayout.SOUTH);

        // ===== RIGHT PANEL =====
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(null);

        JLabel title = new JLabel("HOME");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(60, 40, 200, 30);
        rightPanel.add(title);

        JLabel welcomeLabel = new JLabel("Bienvenue, " + user.getPseudo());
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        welcomeLabel.setBounds(60, 90, 300, 30);
        rightPanel.add(welcomeLabel);

        JButton dashboardButton = new JButton("Ouvrir le Dashboard");
        dashboardButton.setBounds(60, 150, 200, 35);
        dashboardButton.setBackground(new Color(0, 102, 102));
        dashboardButton.setForeground(Color.WHITE);
        dashboardButton.setFocusPainted(false);
        rightPanel.add(dashboardButton);

        dashboardButton.addActionListener(e -> {
            if (user.getRole().equals("admin")) {
                new AdminDashboard(user).setVisible(true);
            } else {
                // Passer le store valide à EmployeeDashboard
                new EmployeeDashboard(user, store).setVisible(true);
            }
        });

        add(leftPanel);
        add(rightPanel);
    }
}

