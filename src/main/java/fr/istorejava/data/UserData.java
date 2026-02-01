package fr.istorejava.data;

import fr.istorejava.security.Password;
import fr.istorejava.db_connection.DBConnection;


import java.sql.*;

import java.util.*;

public class UserData {

    private static String normalizeRole(String role) {
        if (role == null || role.isBlank()) return "EMPLOYEE";
        String r = role.trim().toUpperCase();
        if (r.equals("USER")) return "EMPLOYEE";
        if (r.equals("EMPLOYEE") || r.equals("ADMIN")) return r;
        // compatibility with old UI passing 'user'/'admin'
        if (r.equals("ADMIN")) return "ADMIN";
        if (r.equals("USER")) return "EMPLOYEE";
        return "EMPLOYEE";
    }


    private int id;
    private String email;
    private String pseudo;
    private String passwordHash;
    private String role;

    // ===== CONSTRUCTEUR (public pour mapping DB) =====
    public UserData(int id, String email, String pseudo, String passwordHash, String role) {
        this.id = id;
        this.email = email;
        this.pseudo = pseudo;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    // ===== BCrypt =====
    public static String hashPassword(String password) {
        return Password.hash(password);
    }

    // ===== CRUD (DB) =====

    // Create (par défaut EMPLOYEE)
    public static UserData createUser(String email, String pseudo, String password, String role) {
        if (getUserByEmail(email) != null) {
            throw new RuntimeException("Email déjà utilisé");
        }


        if (!WhitelistData.isWhitelisted(email)) {
            throw new RuntimeException("Email non autorisé (whitelist)");
        }
// Option rapide : si c'est le premier user en DB -> ADMIN
        // (reproduit ton ancien comportement)
        if (countUsers() == 0) {
            role = "ADMIN";
        }

        String hash = hashPassword(password);

        String sql = "INSERT INTO users(email, pseudo, password_hash, role) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, email);
            ps.setString(2, pseudo);
            ps.setString(3, hash);
            ps.setString(4, normalizeRole(role));

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            int newId = -1;
            if (keys.next()) newId = keys.getInt(1);

            return new UserData(newId, email, pseudo, hash, normalizeRole(role));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Read all
    public static List<UserData> getAllUsers() {
        String sql = "SELECT id, email, pseudo, password_hash, role FROM users";
        List<UserData> list = new ArrayList<>();

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new UserData(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("pseudo"),
                        rs.getString("password_hash"),
                        rs.getString("role")
                ));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Read by ID
    public static UserData getUserById(int id) {
        String sql = "SELECT id, email, pseudo, password_hash, role FROM users WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new UserData(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("pseudo"),
                        rs.getString("password_hash"),
                        rs.getString("role")
                );
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Read by email
    public static UserData getUserByEmail(String email) {
        String sql = "SELECT id, email, pseudo, password_hash, role FROM users WHERE email = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new UserData(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("pseudo"),
                        rs.getString("password_hash"),
                        rs.getString("role")
                );
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Update
    public void update(String email, String pseudo, String password, String role) {
        // si l'email change, vérifier l'unicité
        if (!this.email.equals(email) && getUserByEmail(email) != null) {
            throw new RuntimeException("Email déjà utilisé");
        }

        String newHash = this.passwordHash;
        if (password != null && !password.isEmpty()) {
            newHash = hashPassword(password);
        }

        String sql = "UPDATE users SET email=?, pseudo=?, password_hash=?, role=? WHERE id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, pseudo);
            ps.setString(3, newHash);
            ps.setString(4, role == null ? this.role : normalizeRole(role));
            ps.setInt(5, this.id);

            ps.executeUpdate();

            // sync objet
            this.email = email;
            this.pseudo = pseudo;
            this.passwordHash = newHash;
            this.role = (role == null ? this.role : normalizeRole(role));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Delete
    public void delete() {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, this.id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static int countUsers() {
        String sql = "SELECT COUNT(*) FROM users";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ===== AUTH =====
    public boolean checkPassword(String password) {
        return Password.verify(password, this.passwordHash);
    }

    // ===== GETTERS =====
    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getPseudo() { return (pseudo == null || pseudo.isBlank()) ? email : pseudo; }
    public String getRole() { return role == null ? null : role.toLowerCase(); }
}
