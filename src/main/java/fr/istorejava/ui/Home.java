package fr.istorejava.ui;

import fr.istorejava.data.StoreData;
import fr.istorejava.data.UserData;
import fr.istorejava.logique.StoreLogique;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Home extends JFrame {

    public Home(UserData user, StoreData store) {
        setTitle("Home");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // If employee and store is null -> ask to choose one of their stores
        if (store == null && user != null && !"admin".equalsIgnoreCase(user.getRole())) {
            store = askStoreSelection(user);
        }

        final StoreData selectedStore = store;

        // ===== LEFT PANEL =====
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(0, 102, 102));
        leftPanel.setLayout(new BorderLayout());

        JLabel companyLabel = new JLabel("COMPANY NAME", SwingConstants.CENTER);
        companyLabel.setForeground(Color.WHITE);
        companyLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel copyright = new JLabel(
                "copyright © company name All rights reserved",
                SwingConstants.CENTER
        );
        copyright.setForeground(Color.LIGHT_GRAY);
        copyright.setFont(new Font("Arial", Font.PLAIN, 10));

        leftPanel.add(companyLabel, BorderLayout.CENTER);
        leftPanel.add(copyright, BorderLayout.SOUTH);

        // ===== RIGHT PANEL =====
        JPanel rightPanel = new JPanel(null);
        rightPanel.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Bienvenue, " + user.getPseudo(), SwingConstants.CENTER);
        welcomeLabel.setBounds(50, 50, 350, 30);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        rightPanel.add(welcomeLabel);

        if (selectedStore != null) {
            JLabel storeLabel = new JLabel("Store : " + selectedStore.getName(), SwingConstants.CENTER);
            storeLabel.setBounds(50, 90, 350, 25);
            storeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            rightPanel.add(storeLabel);
        }

        JButton dashboardButton = new JButton("Ouvrir le Dashboard");
        dashboardButton.setBounds(60, 150, 250, 35);
        dashboardButton.setBackground(new Color(0, 102, 102));
        dashboardButton.setForeground(Color.WHITE);
        dashboardButton.setFocusPainted(false);
        rightPanel.add(dashboardButton);

        StoreData finalStore = selectedStore;
        dashboardButton.addActionListener(e -> {
            if ("admin".equalsIgnoreCase(user.getRole())) {
                new AdminDashboard(user).setVisible(true);
            } else {
                if (finalStore == null) {
                    JOptionPane.showMessageDialog(this,
                            "Aucun store disponible pour cet utilisateur.",
                            "Info",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                new EmployeeDashboard(user, finalStore).setVisible(true);
            }
        });

        add(leftPanel);
        add(rightPanel);
    }

    private StoreData askStoreSelection(UserData user) {
        try {
            List<StoreData> stores = StoreLogique.listStores(user);
            if (stores == null || stores.isEmpty()) return null;

            String[] names = stores.stream().map(StoreData::getName).toArray(String[]::new);
            String choice = (String) JOptionPane.showInputDialog(
                    this,
                    "Choisissez un magasin :",
                    "Sélection du store",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    names,
                    names[0]
            );

            if (choice == null) return null;

            for (StoreData s : stores) {
                if (s.getName().equals(choice)) return s;
            }
            return stores.get(0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
