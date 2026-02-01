package fr.istorejava.data;

import fr.istorejava.db_connection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StoreData {
    private int id;
    private String name;

    public StoreData(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    // ===== Admin CRUD =====
    public static StoreData createStore(String name) {
        String sql = "INSERT INTO stores(name) VALUES (?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            int newId = -1;
            if (keys.next()) newId = keys.getInt(1);

            return new StoreData(newId, name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteStore(int storeId) {
        String sql = "DELETE FROM stores WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, storeId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<StoreData> getAllStores() {
        String sql = "SELECT id, name FROM stores ORDER BY name";
        List<StoreData> stores = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) stores.add(new StoreData(rs.getInt("id"), rs.getString("name")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return stores;
    }

    // ===== Access management =====
    public static void addEmployeeToStore(int storeId, int userId) {
        String sql = "INSERT INTO store_employees(store_id, user_id) VALUES (?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, storeId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeEmployeeFromStore(int storeId, int userId) {
        String sql = "DELETE FROM store_employees WHERE store_id = ? AND user_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, storeId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<StoreData> getStoresForEmployee(int userId) {
        String sql = """ 
            SELECT s.id, s.name
            FROM store_employees se
            JOIN stores s ON s.id = se.store_id
            WHERE se.user_id = ?
            ORDER BY s.name
        """;
        List<StoreData> stores = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) stores.add(new StoreData(rs.getInt("id"), rs.getString("name")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return stores;
    }

    public static List<UserData> getEmployeesWithAccess(int storeId) {
        String sql = """ 
            SELECT u.id, u.email, u.pseudo, u.password_hash, u.role
            FROM store_employees se
            JOIN users u ON u.id = se.user_id
            WHERE se.store_id = ?
            ORDER BY u.email
        """;
        List<UserData> users = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, storeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(new UserData(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("pseudo"),
                        rs.getString("password_hash"),
                        rs.getString("role")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return users;
    }
}
