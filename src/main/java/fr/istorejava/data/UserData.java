package fr.istorejava.data;

import java.security.MessageDigest;
import java.util.*;

public class UserData {

    private static int nextId = 1;

    private int id;
    private String email;
    private String pseudo;
    private String passwordHash;
    private String role;

    // Stockage de tous les utilisateurs
    private static final HashMap<Integer, UserData> usersById = new HashMap<>();
    private static final HashMap<String, UserData> usersByEmail = new HashMap<>();

    // ===== CONSTRUCTEUR =====
    private UserData(String email, String pseudo, String password, String role) {
        this.id = nextId++;
        this.email = email;
        this.pseudo = pseudo;
        this.passwordHash = hashPassword(password);
        this.role = role;
    }

    // ===== HASH PASSWORD =====
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ===== CRUD =====

    // Create
    public static UserData createUser(String email, String pseudo, String password, String role) {
        if (usersByEmail.containsKey(email))
            throw new RuntimeException("Email déjà utilisé");

        // Si c'est le premier utilisateur, le rôle est forcé à "admin"
        if (usersById.isEmpty()) {
            role = "admin";
        }

        UserData user = new UserData(email, pseudo, password, role);
        usersById.put(user.id, user);
        usersByEmail.put(email, user);
        return user;
    }

    // Read all
    public static List<UserData> getAllUsers() {
        return new ArrayList<>(usersById.values());
    }

    // Read by ID
    public static UserData getUserById(int id) {
        return usersById.get(id);
    }

    // Read by email
    public static UserData getUserByEmail(String email) {
        return usersByEmail.get(email);
    }

    // Update
    public void update(String email, String pseudo, String password, String role) {
        if (!this.email.equals(email) && usersByEmail.containsKey(email)) {
            throw new RuntimeException("Email déjà utilisé");
        }
        usersByEmail.remove(this.email);
        this.email = email;
        this.pseudo = pseudo;
        if (password != null && !password.isEmpty()) {
            this.passwordHash = hashPassword(password);
        }
        this.role = role;
        usersByEmail.put(email, this);
    }

    // Delete
    public void delete() {
        usersById.remove(this.id);
        usersByEmail.remove(this.email);
    }

    // ===== AUTH =====
    public boolean checkPassword(String password) {
        return hashPassword(password).equals(this.passwordHash);
    }

    // ===== GETTERS =====
    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getPseudo() { return pseudo; }
    public String getRole() { return role; }

}
