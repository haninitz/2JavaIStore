package fr.istorejava.ui;

import fr.istorejava.data.ItemData;
import fr.istorejava.data.StoreData;
import fr.istorejava.data.UserData;
import fr.istorejava.logique.InventoryLogique;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryGUI extends JFrame {

    private final UserData currentUser;
    private final StoreData store;
    private final boolean adminMode;

    private final DefaultListModel<String> itemListModel = new DefaultListModel<>();
    private final JList<String> itemList = new JList<>(itemListModel);

    private List<ItemData> items = new ArrayList<>();

    public InventoryGUI(UserData user, StoreData store, boolean adminMode) {
        this.currentUser = user;
        this.store = store;
        this.adminMode = adminMode;

        setTitle("Inventaire - " + store.getName());
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== LIST =====
        add(new JScrollPane(itemList), BorderLayout.CENTER);

        // ===== BUTTONS =====
        JPanel buttonPanel = new JPanel(new GridLayout(1, adminMode ? 5 : 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (adminMode) {
            JButton btnCreate = new JButton("Create Item");
            JButton btnDelete = new JButton("Delete Item");
            btnCreate.addActionListener(e -> createItem());
            btnDelete.addActionListener(e -> deleteItem());
            buttonPanel.add(btnCreate);
            buttonPanel.add(btnDelete);
        }

        JButton btnPlus = new JButton("+1 Stock");
        JButton btnMinus = new JButton("-1 Stock");
        btnPlus.addActionListener(e -> changeStock(1));
        btnMinus.addActionListener(e -> changeStock(-1));

        buttonPanel.add(btnPlus);
        buttonPanel.add(btnMinus);

        add(buttonPanel, BorderLayout.SOUTH);

        refreshInventory();
    }

    // =====================
    // LOAD INVENTORY
    // =====================
    private void refreshInventory() {
        itemListModel.clear();
        try {
            items = InventoryLogique.getInventory(currentUser, store.getId());
            for (ItemData i : items) {
                itemListModel.addElement(i.getName() + " | Prix : " + i.getPrice() + "€");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =====================
    // STOCK +/-1
    // =====================
    private void changeStock(int delta) {
        int idx = itemList.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un item.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ItemData item = items.get(idx);

        try {
            InventoryLogique.changeStock(currentUser, store.getId(), item.getId(), delta);
            JOptionPane.showMessageDialog(this, "Stock modifié ✅");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =====================
    // ADMIN ONLY
    // =====================
    private void createItem() {
        String name = JOptionPane.showInputDialog(this, "Nom de l'item :");
        if (name == null || name.isBlank()) return;

        String priceStr = JOptionPane.showInputDialog(this, "Prix :");
        if (priceStr == null) return;

        try {
            double price = Double.parseDouble(priceStr);
            InventoryLogique.createItem(currentUser, name.trim(), price);
            refreshInventory();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteItem() {
        int idx = itemList.getSelectedIndex();
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
