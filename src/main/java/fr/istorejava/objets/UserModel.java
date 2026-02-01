package fr.istorejava.objets;

public class UserModel {

    private final int id;
    private final String email;
    private final String pseudo;
    private final String role; // "admin" ou "employee"

    public UserModel(int id, String email, String pseudo, String role) {
        this.id = id;
        this.email = email;
        this.pseudo = pseudo;
        this.role = role;
    }

    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getPseudo() { return pseudo == null || pseudo.isBlank() ? email : pseudo; }
    public String getRole() { return role; }

    public boolean isAdmin() {
        return role != null && role.equalsIgnoreCase("admin");
    }

    @Override
    public String toString() {
        return getPseudo() + " (" + role + ")";
    }
}
