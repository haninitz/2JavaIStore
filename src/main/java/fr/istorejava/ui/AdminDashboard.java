package fr.istorejava.ui;

import fr.istorejava.data.StoreData;
import fr.istorejava.data.UserData;
import fr.istorejava.data.WhitelistData;
import fr.istorejava.logique.StoreLogique;
import fr.istorejava.logique.InventoryLogique;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboard extends JFrame {

    private final UserData admin;

    private List<UserData> users = new ArrayList<>();
    private List<StoreData> stores = new ArrayList<>();

    private final DefaultListModel<String> userListModel = new DefaultListModel<>();
    private final DefaultListModel<String> storeListModel = new DefaultListModel<>();

    private JList<String> userList;
    private JList<String> storeList;

    public AdminDashboard(UserData admin) {
        this.admin = admin;

        setTitle("Admin Dashboard - " + admin.getPseudo());
        setSize(1100, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        // ===== TOP PANEL (title + logout) =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("ADMIN DASHBOARD", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(new Color(0, 102, 102));
        topPanel.add(title, BorderLayout.CENTER);

        JButton logoutButton = createButton("Se déconnecter", e -> logout());
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(logoutButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ===== MAIN PANEL =====
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ----- USERS PANEL -----
        JPanel usersPanel = new JPanel(new BorderLayout());
        usersPanel.setBackground(Color.WHITE);
        usersPanel.setBorder(BorderFactory.createTitledBorder("Utilisateurs"));

        userList = new JList<>(userListModel);
        usersPanel.add(new JScrollPane(userList), BorderLayout.CENTER);

        JPanel userButtons = new JPanel(new GridLayout(3, 1, 8, 8));
        userButtons.setBackground(Color.WHITE);

        JButton btnRefreshUsers = createButton("Refresh Users", e -> refreshUserList());
        JButton btnDeleteUser = createButton("Delete User", e -> deleteUser());
        JButton btnWhitelist = createButton("Whitelist Email", e -> whitelistEmail());

        userButtons.add(btnRefreshUsers);
        userButtons.add(btnDeleteUser);
        userButtons.add(btnWhitelist);

        usersPanel.add(userButtons, BorderLayout.SOUTH);

        // ----- STORES PANEL -----
        JPanel storesPanel = new JPanel(new BorderLayout());
        storesPanel.setBackground(Color.WHITE);
        storesPanel.setBorder(BorderFactory.createTitledBorder("Magasins"));

        storeList = new JList<>(storeListModel);
        storesPanel.add(new JScrollPane(storeList), BorderLayout.CENTER);

        JPanel storeButtons = new JPanel(new GridLayout(6, 1, 8, 8));
        storeButtons.setBackground(Color.WHITE);

        JButton btnRefreshStores = createButton("Refresh Stores", e -> refreshStoreList());
        JButton btnCreateStore = createButton("Create Store", e -> createStore());
        JButton btnDeleteStore = createButton("Delete Store", e -> deleteStore());
        JButton btnAddEmployee = createButton("Add Employee", e -> addEmployeeToStore());
        JButton btnViewEmployees = createButton("View Employees", e -> viewStoreEmployees());
        JButton btnOpenInventory = createButton("Open Inventory", e -> openInventory());

        storeButtons.add(btnRefreshStores);
        storeButtons.add(btnCreateStore);
        storeButtons.add(btnDeleteStore);
        storeButtons.add(btnAddEmployee);
        storeButtons.add(btnViewEmployees);
        storeButtons.add(btnOpenInventory);

        storesPanel.add(storeButtons, BorderLayout.SOUTH);

        // add panels
        mainPanel.add(usersPanel);
        mainPanel.add(storesPanel);

        add(mainPanel, BorderLayout.CENTER);

        // initial load
        refreshUserList();
        refreshStoreList();
    }

    private void refreshUserList() {
        userListModel.clear();
        users = UserData.getAllUsers();
        for (UserData u : users) {
            userListModel.addElement(u.getPseudo() + " (" + u.getRole() + ")");
        }
    }

    private void refreshStoreList() {
        storeListModel.clear();
        try {
            stores = StoreLogique.listStores(admin);
            for (StoreData s : stores) {
                storeListModel.addElement(s.getName());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createStore() {
        String name = JOptionPane.showInputDialog(this, "Nom du magasin :");
        if (name == null || name.isBlank()) return;

        try {
            StoreLogique.createStore(admin, name.trim());
            refreshStoreList();
            JOptionPane.showMessageDialog(this, "Store créé ✅");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStore() {
        int idx = storeList.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un magasin.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StoreData store = stores.get(idx);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer le store " + store.getName() + " ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            StoreLogique.deleteStore(admin, store.getId());
            refreshStoreList();
            JOptionPane.showMessageDialog(this, "Store supprimé ✅");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addEmployeeToStore() {
        int storeIdx = storeList.getSelectedIndex();
        int userIdx = userList.getSelectedIndex();

        if (storeIdx < 0 || userIdx < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un store ET un utilisateur.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StoreData store = stores.get(storeIdx);
        UserData user = users.get(userIdx);

        try {
            StoreLogique.addEmployeeToStore(admin, store.getId(), user.getId());
            JOptionPane.showMessageDialog(this, "Accès ajouté ✅");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewStoreEmployees() {
        int storeIdx = storeList.getSelectedIndex();
        if (storeIdx < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un magasin.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StoreData store = stores.get(storeIdx);

        try {
            List<UserData> list = StoreLogique.listEmployeesWithAccess(admin, store.getId());
            StringBuilder sb = new StringBuilder("Employés ayant accès à ").append(store.getName()).append(" :\n");
            for (UserData u : list) {
                sb.append("- ").append(u.getPseudo()).append(" | ").append(u.getEmail()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Accès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openInventory() {
        int storeIdx = storeList.getSelectedIndex();
        if (storeIdx < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un magasin.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StoreData store = stores.get(storeIdx);
        new InventoryGUI(admin, store, true).setVisible(true);
    }

    private void deleteUser() {
        int idx = userList.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un utilisateur.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        UserData target = users.get(idx);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer " + target.getEmail() + " ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            target.delete();
            refreshUserList();
            JOptionPane.showMessageDialog(this, "Utilisateur supprimé ✅");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void whitelistEmail() {
        String email = JOptionPane.showInputDialog(this, "Email à whitelist :");
        if (email == null || email.isBlank()) return;

        try {
            WhitelistData.addEmail(email.trim());
            JOptionPane.showMessageDialog(this, "Email whitelisté ✅");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        new Login().setVisible(true);
        dispose();
    }

    // Style button
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(0, 102, 102));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        button.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { button.setBackground(new Color(0, 140, 140)); }
            @Override public void mouseExited(MouseEvent e) { button.setBackground(new Color(0, 102, 102)); }
        });

        return button;
    }

    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton button = createButton(text);
        button.addActionListener(action);
        return button;
    }
}
