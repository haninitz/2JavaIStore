package fr.istorejava.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import fr.istorejava.data.UserData;
import fr.istorejava.data.StoreData;


class EmployeeDashboard extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;

    public EmployeeDashboard(UserData employee, StoreData store) {
        setTitle("Employee Dashboard - " + employee.getPseudo());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== TOP BAR =====
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(0, 102, 102));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("EMPLOYEE DASHBOARD");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        // Logout Button
        JButton logoutButton = createButton("Se déconnecter", e -> logout());

        topBar.add(title, BorderLayout.WEST);
        topBar.add(logoutButton, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // ===== SIDE MENU =====
        JPanel sideMenu = new JPanel();
        sideMenu.setLayout(new GridLayout(3, 1, 0, 15));
        sideMenu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sideMenu.setBackground(new Color(240, 240, 240));

        JButton btnInventory = new JButton("Inventaire");
        JButton btnStock = new JButton("Gestion du stock");
        JButton btnEmployees = new JButton("Employés du store");

        sideMenu.add(btnInventory);
        sideMenu.add(btnStock);
        sideMenu.add(btnEmployees);

        add(sideMenu, BorderLayout.WEST);

        // ===== CONTENT =====
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(createInventoryPanel(), "INVENTORY");
        contentPanel.add(createStockPanel(), "STOCK");
        contentPanel.add(createEmployeesPanel(), "EMPLOYEES");

        add(contentPanel, BorderLayout.CENTER);

        // ===== ACTIONS (UI ONLY) =====
        btnInventory.addActionListener(e -> cardLayout.show(contentPanel, "INVENTORY"));
        btnStock.addActionListener(e -> cardLayout.show(contentPanel, "STOCK"));
        btnEmployees.addActionListener(e -> cardLayout.show(contentPanel, "EMPLOYEES"));

        setVisible(true);
    }

    // ===== INVENTORY VIEW =====
    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Inventaire du store");
        label.setFont(new Font("Arial", Font.BOLD, 18));

        JTable table = new JTable(new DefaultTableModel(
                new Object[][]{
                        {1, "iPhone 15", 999.99, 12},
                        {2, "AirPods Pro", 279.99, 30}
                },
                new String[]{"ID", "Nom", "Prix (€)", "Quantité"}
        ));

        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    // ===== STOCK MANAGEMENT =====
    private JPanel createStockPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Modifier le stock");
        label.setFont(new Font("Arial", Font.BOLD, 18));

        JTable table = new JTable(new DefaultTableModel(
                new Object[][]{
                        {1, "iPhone 15", 12},
                        {2, "AirPods Pro", 30}
                },
                new String[]{"ID", "Nom", "Quantité"}
        ));

        JPanel buttons = new JPanel();
        JButton btnPlus = new JButton("+");
        JButton btnMinus = new JButton("-");

        buttons.add(btnPlus);
        buttons.add(btnMinus);

        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    // ===== EMPLOYEES LIST =====
    private JPanel createEmployeesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Employés ayant accès au store");
        label.setFont(new Font("Arial", Font.BOLD, 18));

        JList<String> list = new JList<>(new String[]{
                "Alice (employee)",
                "Bob (employee)",
                "Admin (admin)"
        });

        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        return panel;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(0, 102, 102));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 140, 140));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 102, 102));
            }
        });

        return button;
    }


    // ===== Surcharge : bouton avec ActionListener =====
    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton button = createButton(text); // style identique
        button.addActionListener(action);     // ajoute l'action
        return button;
    }

    // ===== Déconnexion =====
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment vous déconnecter ?", "Déconnexion", JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION) {
            dispose(); // ferme le dashboard
            // ici tu peux rouvrir la fenêtre de login si tu en as une
            new Login().setVisible(true);
        }
    }
}
