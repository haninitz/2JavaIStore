package fr.istorejava.data;

import fr.istorejava.db_connection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WhitelistData {

    private WhitelistData() {}

    public static boolean isWhitelisted(String email) {
        String sql = "SELECT 1 FROM email_whitelist WHERE email = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void addEmail(String email) {
        String sql = "INSERT INTO email_whitelist(email) VALUES (?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeEmail(String email) {
        String sql = "DELETE FROM email_whitelist WHERE email = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getAllEmails() {
        String sql = "SELECT email FROM email_whitelist ORDER BY email";
        List<String> emails = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) emails.add(rs.getString("email"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return emails;
    }
}
