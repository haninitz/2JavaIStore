package fr.istorejava.data;

import fr.istorejava.db_connection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InventoryData {
    private int storeId;

    public InventoryData(int storeId) {
        this.storeId = storeId;
    }

    public int getStoreId() { return storeId; }

    // ===== Read inventory for one store =====
    public static List<ItemData> getInventoryItems(int storeId) {
        String sql = """ 
            SELECT i.id, i.name, i.price, inv.quantity
            FROM inventory inv
            JOIN items i ON i.id = inv.item_id
            WHERE inv.store_id = ?
            ORDER BY i.name
        """;
        List<ItemData> items = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, storeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new ItemData(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    // ===== Update stock (delta can be + or -). Never go below 0 =====
    public static void updateQuantity(int storeId, int itemId, int delta) throws Exception {
        // current quantity
        int current = getQuantity(storeId, itemId);
        int next = current + delta;
        if (next < 0) throw new Exception("Stock insuffisant (ne peut pas être < 0)");

        String sql = "UPDATE inventory SET quantity = ? WHERE store_id = ? AND item_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, next);
            ps.setInt(2, storeId);
            ps.setInt(3, itemId);
            int updated = ps.executeUpdate();

            // If row doesn't exist yet, create it
            if (updated == 0) {
                String insert = "INSERT INTO inventory(store_id, item_id, quantity) VALUES (?, ?, ?)";
                try (PreparedStatement ins = c.prepareStatement(insert)) {
                    ins.setInt(1, storeId);
                    ins.setInt(2, itemId);
                    ins.setInt(3, next);
                    ins.executeUpdate();
                }
            }

        } catch (Exception e) {
            throw e;
        }
    }

    private static int getQuantity(int storeId, int itemId) {
        String sql = "SELECT quantity FROM inventory WHERE store_id = ? AND item_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, storeId);
            ps.setInt(2, itemId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("quantity");
            return 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
