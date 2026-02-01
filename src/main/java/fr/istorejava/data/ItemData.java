package fr.istorejava.data;

import fr.istorejava.db_connection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ItemData {

    private int id;
    private String name;
    private double price;
    private int stock; // used when joined with inventory

    public ItemData(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    // ===== Admin CRUD ===
    public static ItemData createItem(String name, double price) {
        String sql = "INSERT INTO items(name, price) VALUES (?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            int newId = -1;
            if (keys.next()) newId = keys.getInt(1);

            return new ItemData(newId, name, price, 0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteItem(int itemId) {
        String sql = "DELETE FROM items WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<ItemData> getAllItems() {
        String sql = "SELECT id, name, price FROM items ORDER BY name";
        List<ItemData> items = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new ItemData(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), 0));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return items;
    }
}
