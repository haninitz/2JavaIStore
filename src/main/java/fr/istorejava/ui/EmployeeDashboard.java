package fr.istorejava.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import fr.istorejava.data.UserData;
import fr.istorejava.data.StoreData;


class EmployeeDashboard extends JFrame {

    public EmployeeDashboard(UserData employee, StoreData store) {
        setTitle("Employee Dashboard - " + employee.getPseudo());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        // ===== TITLE =====
        JLabel title = new JLabel("EMPLOYEE DASHBOARD", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(0, 102, 102));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // ===== STORE LABEL =====
        JLabel storeLabel = new JLabel("Magasin : " + store.getName(), SwingConstants.CENTER);
        storeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        storeLabel.setForeground(new Color(80, 80, 80));
        storeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        add(storeLabel, BorderLayout.CENTER);

        // ===== BUTTONS PANEL =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 20));

        JButton btnViewInventory = createButton("Voir l'inventaire");
        JButton btnUpdateStock = createButton("Modifier le stock");

        buttonPanel.add(btnViewInventory);
        buttonPanel.add(btnUpdateStock);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.add(buttonPanel);

        add(centerPanel, BorderLayout.CENTER);
    }

    // Même méthode pour créer un bouton esthétique
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(0, 102, 102));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Hover effect
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
}