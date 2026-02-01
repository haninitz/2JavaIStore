package fr.istorejava.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import fr.istorejava.data.UserData;

public class AdminDashboard extends JFrame {

    public AdminDashboard(UserData admin) {
        setTitle("Admin Dashboard - " + admin.getPseudo());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        // ===== TOP PANEL : LOGOUT BUTTON + TITLE =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Logout Button
        JButton logoutButton = createButton("Se déconnecter", e -> logout());
        topPanel.add(logoutButton, BorderLayout.WEST);

        // ===== TITLE =====
        JLabel title = new JLabel("ADMIN DASHBOARD", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(new Color(0, 102, 102));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Ajouter le topPanel à la fenêtre
        add(topPanel, BorderLayout.NORTH);

        // ===== MAIN PANEL =====
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setLayout(new GridLayout(1, 3, 20, 0)); // 3 colonnes : Users | Stores | Inventory
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ----- SECTION 1 : USERS -----
        JPanel usersPanel = new JPanel();
        usersPanel.setBackground(Color.WHITE);
        usersPanel.setLayout(new BorderLayout());
        usersPanel.setBorder(BorderFactory.createTitledBorder("Utilisateurs"));

        // Liste des utilisateurs
        DefaultListModel<String> userListModel = new DefaultListModel<>();
        for(UserData u : UserData.getAllUsers()) {
            userListModel.addElement(u.getPseudo() + " (" + u.getRole() + ")");
        }
        JList<String> userList = new JList<>(userListModel);
        usersPanel.add(new JScrollPane(userList), BorderLayout.CENTER);

        // Boutons utilisateurs
        JPanel userButtonPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        userButtonPanel.setBackground(Color.WHITE);
        userButtonPanel.add(createButton("Update User"));
        userButtonPanel.add(createButton("Delete User"));
        userButtonPanel.add(createButton("Whitelist Email"));
        usersPanel.add(userButtonPanel, BorderLayout.SOUTH);

        // ----- SECTION 2 : STORES -----
        JPanel storesPanel = new JPanel();
        storesPanel.setBackground(Color.WHITE);
        storesPanel.setLayout(new BorderLayout());
        storesPanel.setBorder(BorderFactory.createTitledBorder("Magasins"));

        // Liste des stores (placeholder)
        DefaultListModel<String> storeListModel = new DefaultListModel<>();
        storeListModel.addElement("Magasin Test 1");
        storeListModel.addElement("Magasin Test 2");
        JList<String> storeList = new JList<>(storeListModel);
        storesPanel.add(new JScrollPane(storeList), BorderLayout.CENTER);

        // Boutons magasins
        JPanel storeButtonPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        storeButtonPanel.setBackground(Color.WHITE);
        storeButtonPanel.add(createButton("Create Store"));
        storeButtonPanel.add(createButton("Delete Store"));
        storeButtonPanel.add(createButton("Add Employee"));
        storeButtonPanel.add(createButton("View Employees"));
        storesPanel.add(storeButtonPanel, BorderLayout.SOUTH);

        // ----- SECTION 3 : INVENTORY -----
        JPanel inventoryPanel = new JPanel();
        inventoryPanel.setBackground(Color.WHITE);
        inventoryPanel.setLayout(new BorderLayout());
        inventoryPanel.setBorder(BorderFactory.createTitledBorder("Inventaire"));

        // Liste des items (placeholder)
        DefaultListModel<String> inventoryListModel = new DefaultListModel<>();
        inventoryListModel.addElement("Item A - Prix: 10 - Stock: 50");
        inventoryListModel.addElement("Item B - Prix: 20 - Stock: 30");
        JList<String> inventoryList = new JList<>(inventoryListModel);
        inventoryPanel.add(new JScrollPane(inventoryList), BorderLayout.CENTER);

        // Boutons inventaire
        JPanel inventoryButtonPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        inventoryButtonPanel.setBackground(Color.WHITE);
        inventoryButtonPanel.add(createButton("Create Item"));
        inventoryButtonPanel.add(createButton("Delete Item"));
        inventoryButtonPanel.add(createButton("Increase Stock"));
        inventoryButtonPanel.add(createButton("Decrease Stock"));
        inventoryPanel.add(inventoryButtonPanel, BorderLayout.SOUTH);

        // ----- ADD SECTIONS TO MAIN PANEL -----
        mainPanel.add(usersPanel);
        mainPanel.add(storesPanel);
        mainPanel.add(inventoryPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    // Méthode pour créer un bouton esthétique
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(0, 102, 102));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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