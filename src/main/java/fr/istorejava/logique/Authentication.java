package fr.istorejava.logique;

import fr.istorejava.data.UserData;
import fr.istorejava.objets.UserModel;

public class Authentication {

    private Authentication() {}

    public static UserModel login(String email, String password) throws Exception {
        if (email == null || email.isBlank()) throw new Exception("Email obligatoire");
        if (password == null || password.isBlank()) throw new Exception("Mot de passe obligatoire");

        UserData user = UserData.getUserByEmail(email.trim());
        if (user == null) throw new Exception("Compte inexistant");
        if (!user.checkPassword(password)) throw new Exception("Mot de passe incorrect");

        // expose a safe model (no password)
        return new UserModel(user.getId(), user.getEmail(), user.getPseudo(), user.getRole());
    }

    public static void signUp(String name, String email, String password) throws Exception {
        if (name == null || name.isBlank()) throw new Exception("Pseudo obligatoire");
        if (email == null || email.isBlank()) throw new Exception("Email obligatoire");
        if (password == null || password.isBlank()) throw new Exception("Mot de passe obligatoire");

        // role "user" for compatibility
        UserData.createUser(email.trim(), name.trim(), password, "user");
    }
}
