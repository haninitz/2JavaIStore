package fr.istorejava.ui;

import fr.istorejava.data.ItemData;
import fr.istorejava.data.StoreData;
import fr.istorejava.data.UserData;
import fr.istorejava.logique.InventoryLogique;
import fr.istorejava.db_connection.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class InventoryGUI extends JFrame {

    private final UserData currentUser;
    private final StoreData store;
    private final boolean adminMode;

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> inventoryList = new JList<>(listModel);

    private List<ItemData> items = new ArrayList<>();

    public InventoryGUI(UserData currentUser, StoreData store, boolean adminMode) {
        this.currentUser = currentUser;
        this.store = store;
        this.adminMode = adminMode;

        setTitle("Inventaire - " + store.getName());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Inventaire : " + store.getName(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        inventoryList.setFont(new Font("Arial", Font.PLAIN, 16));
        add(new JScrollPane(inventoryList), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnMinus = new JButton("-1");
        JButton btnPlus = new JButton("+1");
        JButton btnRefresh = new JButton("Refresh");

        btnMinus.addActionListener(e -> changeStock(-1));
        btnPlus.addActionListener(e -> changeStock(+1));
        btnRefresh.addActionListener(e -> refreshInventory());

        buttons.add(btnMinus);
        buttons.add(btnPlus);
        buttons.add(btnRefresh);

        if (adminMode) {
            JButton btnCreate = new JButton("Create Item");
            JButton btnDelete = new JButton("Delete Item");
            btnCreate.addActionListener(e -> createItem());
            btnDelete.addActionListener(e -> deleteItem());
            buttons.add(btnCreate);
            buttons.add(btnDelete);
        }

        add(buttons, BorderLayout.SOUTH);

        refreshInventory();
    }

    private void refreshInventory() {
        listModel.clear();
        try {
            items = InventoryLogique.getInventory(currentUser, store.getId());
            for (ItemData i : items) {
                listModel.addElement(i.getName() + " | " + i.getPrice() + "€ | Stock: " + i.getStock());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changeStock(int delta) {
        int idx = inventoryList.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un item.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ItemData item = items.get(idx);

        try {
            InventoryLogique.changeStock(currentUser, store.getId(), item.getId(), delta);
            refreshInventory();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createItem() {
        String name = JOptionPane.showInputDialog(this, "Nom de l'item :");
        if (name == null || name.isBlank()) return;

        String priceStr = JOptionPane.showInputDialog(this, "Prix :");
        if (priceStr == null || priceStr.isBlank()) return;

        try {
            double price = Double.parseDouble(priceStr.trim());

            // 1) créer l'item dans la table item
            ItemData created = InventoryLogique.createItem(currentUser, name.trim(), price);

            // 2) IMPORTANT : rattacher l'item au store dans inventory (quantity=0)
            ensureInventoryRow(store.getId(), created.getId());

            // 3) refresh pour voir l'item immédiatement
            refreshInventory();

            JOptionPane.showMessageDialog(this, "Item créé ✅", "OK", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Prix invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Crée la ligne inventory si elle n'existe pas encore.
     * Requête compatible MySQL si (store_id, item_id) est UNIQUE/PK.
     */
    private void ensureInventoryRow(int storeId, int itemId) throws Exception {
        String sql =
                "INSERT INTO inventory (store_id, item_id, quantity) " +
                        "VALUES (?, ?, 0) " +
                        "ON DUPLICATE KEY UPDATE quantity = quantity";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, storeId);
            ps.setInt(2, itemId);
            ps.executeUpdate();
        }
    }

    private void deleteItem() {
        int idx = inventoryList.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un item.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ItemData item = items.get(idx);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer l'item " + item.getName() + " ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            InventoryLogique.deleteItem(currentUser, item.getId());
            refreshInventory();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
