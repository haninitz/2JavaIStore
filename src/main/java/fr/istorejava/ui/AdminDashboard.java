package fr.istorejava.ui;

import fr.istorejava.data.StoreData;
import fr.istorejava.data.UserData;
import fr.istorejava.data.WhitelistData;
import fr.istorejava.logique.StoreLogique;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboard extends JFrame {

    // ====== DATA ======
    private final UserData admin;

    private List<UserData> users = new ArrayList<>();
    private List<StoreData> stores = new ArrayList<>();

    // ====== UI MODELS ======
    private final DefaultListModel<String> userListModel = new DefaultListModel<>();
    private final DefaultListModel<String> storeListModel = new DefaultListModel<>();
    private final DefaultListModel<String> inventoryListModel = new DefaultListModel<>();

    // ====== UI COMPONENTS ======
    private JList<String> userList;
    private JList<String> storeList;
    private JList<String> inventoryList;

    public AdminDashboard(UserData admin) {
        this.admin = admin;

        setTitle("Admin Dashboard - " + admin.getPseudo());
        setSize(1100, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        // ===== TOP PANEL : TITLE + LOGOUT =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton logoutButton = createButton("Se déconnecter", e -> logout());
        topPanel.add(logoutButton, BorderLayout.EAST);

        JLabel title = new JLabel("ADMIN DASHBOARD", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(new Color(0, 102, 102));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        topPanel.add(title, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // ===== MAIN PANEL =====
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setLayout(new GridLayout(1, 3, 20, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // =======================
        // SECTION 1 : USERS
        // =======================
        JPanel usersPanel = new JPanel(new BorderLayout());
        usersPanel.setBackground(Color.WHITE);
        usersPanel.setBorder(BorderFactory.createTitledBorder("Utilisateurs"));

        userList = new JList<>(userListModel);
        usersPanel.add(new JScrollPane(userList), BorderLayout.CENTER);

        JPanel userButtonPanel = new JPanel(new GridLayout(3, 1, 8, 8));
        userButtonPanel.setBackground(Color.WHITE);

        JButton btnUpdateUser = createButton("Update User", e -> updateUser());
        JButton btnDeleteUser = createButton("Delete User", e -> deleteUser());
        JButton btnWhitelist = createButton("Whitelist Email", e -> whitelistEmail());

        userButtonPanel.add(btnUpdateUser);
        userButtonPanel.add(btnDeleteUser);
        userButtonPanel.add(btnWhitelist);
        usersPanel.add(userButtonPanel, BorderLayout.SOUTH);

        // =======================
        // SECTION 2 : STORES
        // =======================
        JPanel storesPanel = new JPanel(new BorderLayout());
        storesPanel.setBackground(Color.WHITE);
        storesPanel.setBorder(BorderFactory.createTitledBorder("Magasins"));

        storeList = new JList<>(storeListModel);
        storesPanel.add(new JScrollPane(storeList), BorderLayout.CENTER);

        JPanel storeButtonPanel = new JPanel(new GridLayout(4, 1, 8, 8));
        storeButtonPanel.setBackground(Color.WHITE);

        JButton btnCreateStore = createButton("Create Store", e -> createStore());
        JButton btnDeleteStore = createButton("Delete Store", e -> deleteStore());
        JButton btnAddEmployee = createButton("Add Employee", e -> addEmployeeToStore());
        JButton btnViewEmployees = createButton("View Employees", e -> viewStoreEmployees());

        storeButtonPanel.add(btnCreateStore);
        storeButtonPanel.add(btnDeleteStore);
        storeButtonPanel.add(btnAddEmployee);
        storeButtonPanel.add(btnViewEmployees);

        storesPanel.add(storeButtonPanel, BorderLayout.SOUTH);

        // =======================
        // SECTION 3 : INVENTORY (placeholder branchage ensuite)
        // =======================
        JPanel inventoryPanel = new JPanel(new BorderLayout());
        inventoryPanel.setBackground(Color.WHITE);
        inventoryPanel.setBorder(BorderFactory.createTitledBorder("Inventaire"));

        inventoryList = new JList<>(inventoryListModel);
        inventoryPanel.add(new JScrollPane(inventoryList), BorderLayout.CENTER);

        JPanel inventoryButtonPanel = new JPanel(new GridLayout(4, 1, 8, 8));
        inventoryButtonPanel.setBackground(Color.WHITE);

        JButton btnCreateItem = createButton("Create Item", e -> notReady("Create Item"));
        JButton btnDeleteItem = createButton("Delete Item", e -> notReady("Delete Item"));
        JButton btnIncreaseStock = createButton("Increase Stock", e -> notReady("Increase Stock"));
        JButton btnDecreaseStock = createButton("Decrease Stock", e -> notReady("Decrease Stock"));

        inventoryButtonPanel.add(btnCreateItem);
        inventoryButtonPanel.add(btnDeleteItem);
        inventoryButtonPanel.add(btnIncreaseStock);
        inventoryButtonPanel.add(btnDecreaseStock);

        inventoryPanel.add(inventoryButtonPanel, BorderLayout.SOUTH);

        // ===== ADD 3 PANELS =====
        mainPanel.add(usersPanel);
        mainPanel.add(storesPanel);
        mainPanel.add(inventoryPanel);

        add(mainPanel, BorderLayout.CENTER);

        // ===== INITIAL LOAD =====
        refreshUserList();
        refreshStoreList();
    }

    // =========================
    // C4 : USERS LIST
    // =========================
    private void refreshUserList() {
        userListModel.clear();
        users = UserData.getAllUsers();
        for (UserData u : users) {
            userListModel.addElement(u.getPseudo() + " (" + u.getRole() + ")");
        }
    }

    // =========================
    // C5 : STORES LIST
    // =========================
    private void refreshStoreList() {
        storeListModel.clear();
        try {
            stores = StoreLogique.listStores(admin); // admin: all stores
            for (StoreData s : stores) {
                storeListModel.addElement(s.getName());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================
    // USERS ACTIONS
    // =========================
    private void updateUser() {
        int idx = userList.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un utilisateur.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        UserData target = users.get(idx);

        String newPseudo = JOptionPane.showInputDialog(this, "Nouveau pseudo :", target.getPseudo());
        if (newPseudo == null || newPseudo.isBlank()) return;

        try {
            // update via UserData (simple). Si tu veux les règles admin/employee -> on branchera UserService ensuite.
            target.update(target.getEmail(), newPseudo.trim(), "", target.getRole());
            JOptionPane.showMessageDialog(this, "Utilisateur mis à jour ✅");
            refreshUserList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
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
            JOptionPane.showMessageDialog(this, "Utilisateur supprimé ✅");
            refreshUserList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================
    // STORES ACTIONS
    // =========================
    private void createStore() {
        String name = JOptionPane.showInputDialog(this, "Nom du magasin :");
        if (name == null || name.isBlank()) return;

        try {
            StoreLogique.createStore(admin, name.trim());
            JOptionPane.showMessageDialog(this, "Store créé ✅");
            refreshStoreList();
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

        StoreData s = stores.get(idx);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer le store " + s.getName() + " ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            StoreLogique.deleteStore(admin, s.getId());
            JOptionPane.showMessageDialog(this, "Store supprimé ✅");
            refreshStoreList();
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

        StoreData s = stores.get(storeIdx);
        UserData u = users.get(userIdx);

        try {
            StoreLogique.addEmployeeToStore(admin, s.getId(), u.getId());
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

        StoreData s = stores.get(storeIdx);

        try {
            List<UserData> list = StoreLogique.listEmployeesWithAccess(admin, s.getId());

            StringBuilder sb = new StringBuilder("Employés ayant accès à ")
                    .append(s.getName()).append(" :\n");

            for (UserData u : list) {
                sb.append("- ").append(u.getPseudo())
                        .append(" | ").append(u.getEmail())
                        .append("\n");
            }

            JOptionPane.showMessageDialog(this, sb.toString(), "Accès store", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================
    // WHITELIST
    // =========================
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

    // =========================
    // PLACEHOLDER INVENTORY
    // =========================
    private void notReady(String feature) {
        JOptionPane.showMessageDialog(this,
                feature + " : on le branche juste après (InventoryData).",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // =========================
    // UI HELPERS
    // =========================
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

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment vous déconnecter ?",
                "Déconnexion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new Login().setVisible(true);
        }
    }
}
