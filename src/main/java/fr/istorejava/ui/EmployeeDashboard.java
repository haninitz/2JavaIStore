package fr.istorejava.ui;

import fr.istorejava.data.ItemData;
import fr.istorejava.data.StoreData;
import fr.istorejava.data.UserData;
import fr.istorejava.logique.InventoryLogique;
import fr.istorejava.logique.StoreLogique;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDashboard extends JFrame {

    private final UserData employee;
    private final StoreData store;

    private CardLayout cardLayout;
    private JPanel contentPanel;

    // Models pour pouvoir refresh
    private final DefaultTableModel inventoryTableModel =
            new DefaultTableModel(new String[]{"ID", "Nom", "Prix (€)", "Quantité"}, 0);

    private final DefaultTableModel stockTableModel =
            new DefaultTableModel(new String[]{"ID", "Nom", "Quantité"}, 0);

    private final DefaultListModel<String> employeesListModel = new DefaultListModel<>();

    // Tables/list pour accéder aux sélections
    private JTable inventoryTable;
    private JTable stockTable;
    private JList<String> employeesList;

    // Data en mémoire
    private List<ItemData> inventoryItems = new ArrayList<>();
    private List<ItemData> stockItems = new ArrayList<>();

    public EmployeeDashboard(UserData employee, StoreData store) {
        this.employee = employee;
        this.store = store;

        setTitle("Employee Dashboard - " + employee.getPseudo());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== TOP BAR =====
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(0, 102, 102));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("EMPLOYEE DASHBOARD - Store: " + store.getName());
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));

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

        // ===== ACTIONS =====
        btnInventory.addActionListener(e -> {
            refreshInventory();
            cardLayout.show(contentPanel, "INVENTORY");
        });

        btnStock.addActionListener(e -> {
            refreshStock();
            cardLayout.show(contentPanel, "STOCK");
        });

        btnEmployees.addActionListener(e -> {
            refreshEmployees();
            cardLayout.show(contentPanel, "EMPLOYEES");
        });

        // Default view
        refreshInventory();
        cardLayout.show(contentPanel, "INVENTORY");

        setVisible(true);
    }

    // ===== INVENTORY VIEW =====
    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Inventaire du store");
        label.setFont(new Font("Arial", Font.BOLD, 18));

        inventoryTable = new JTable(inventoryTableModel);
        inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshInventory());

        JPanel top = new JPanel(new BorderLayout());
        top.add(label, BorderLayout.WEST);
        top.add(refreshBtn, BorderLayout.EAST);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);

        return panel;
    }

    private void refreshInventory() {
        inventoryTableModel.setRowCount(0);
        try {
            inventoryItems = InventoryLogique.getInventory(employee, store.getId());
            for (ItemData i : inventoryItems) {
                inventoryTableModel.addRow(new Object[]{
                        i.getId(),
                        i.getName(),
                        i.getPrice(),
                        i.getStock()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===== STOCK MANAGEMENT =====
    private JPanel createStockPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Modifier le stock");
        label.setFont(new Font("Arial", Font.BOLD, 18));

        stockTable = new JTable(stockTableModel);
        stockTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel buttons = new JPanel();
        JButton btnPlus = new JButton("+1");
        JButton btnMinus = new JButton("-1");
        JButton btnRefresh = new JButton("Refresh");

        btnPlus.addActionListener(e -> changeSelectedStock(+1));
        btnMinus.addActionListener(e -> changeSelectedStock(-1));
        btnRefresh.addActionListener(e -> refreshStock());

        buttons.add(btnMinus);
        buttons.add(btnPlus);
        buttons.add(btnRefresh);

        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(stockTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshStock() {
        stockTableModel.setRowCount(0);
        try {
            stockItems = InventoryLogique.getInventory(employee, store.getId());
            for (ItemData i : stockItems) {
                stockTableModel.addRow(new Object[]{
                        i.getId(),
                        i.getName(),
                        i.getStock()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changeSelectedStock(int delta) {
        int row = stockTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un produit.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int itemId = (int) stockTableModel.getValueAt(row, 0);

        try {
            InventoryLogique.changeStock(employee, store.getId(), itemId, delta);
            refreshStock();
            refreshInventory();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===== EMPLOYEES LIST =====
    private JPanel createEmployeesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Employés ayant accès au store");
        label.setFont(new Font("Arial", Font.BOLD, 18));

        employeesList = new JList<>(employeesListModel);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshEmployees());

        JPanel top = new JPanel(new BorderLayout());
        top.add(label, BorderLayout.WEST);
        top.add(refreshBtn, BorderLayout.EAST);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(employeesList), BorderLayout.CENTER);

        return panel;
    }

    private void refreshEmployees() {
        employeesListModel.clear();
        try {
            List<UserData> list = StoreLogique.listEmployeesWithAccess(employee, store.getId());
            for (UserData u : list) {
                employeesListModel.addElement(u.getPseudo() + " (" + u.getRole() + ")");
            }
        } catch (Exception ex) {
            // Si tu veux afficher l'erreur au lieu du fallback, remplace par JOptionPane
            employeesListModel.addElement("Accès refusé (réservé à l'admin)");
        }
    }

    // ===== UI BUTTON STYLE =====
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

    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton button = createButton(text);
        button.addActionListener(action);
        return button;
    }

    // ===== LOGOUT =====
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
