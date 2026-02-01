package fr.istorejava.security;

import org.mindrot.jbcrypt.BCrypt;

public class Password {

    private Password() {}

    /** Hash un mot de passe en BCrypt (avec salt). */
    public static String hash(String plainPassword) {
        if (plainPassword == null || plainPassword.isBlank()) {
            throw new IllegalArgumentException("Mot de passe vide");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    /** Vérifie un mot de passe en clair contre un hash BCrypt stocké en DB. */
    public static boolean verify(String plainPassword, String hash) {
        if (plainPassword == null || hash == null || hash.isBlank()) return false;
        try {
            return BCrypt.checkpw(plainPassword, hash);
        } catch (Exception e) {
            // hash invalide / format inattendu
            return false;
        }
    }
}
