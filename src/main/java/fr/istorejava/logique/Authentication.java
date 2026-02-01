package fr.istorejava.logique;

import fr.istorejava.data.UserData;
import fr.istorejava.objets.UserModel;
import fr.istorejava.db_connection.Session;

public class Authentication {

    private Authentication() {}

    public static UserModel login(String email, String password) throws Exception {

        // 1. Récupérer l'utilisateur depuis la DB
        UserData userData = UserData.getUserByEmail(email);

        if (userData == null) {
            throw new Exception("Utilisateur introuvable");
        }

        // 2. Vérifier le mot de passe
        if (!userData.checkPassword(password)) {
            throw new Exception("Mot de passe incorrect");
        }

        // 3. Sauvegarder l'utilisateur dans la session
        Session.setCurrentUser(userData);   // ✅ ICI c’est bon

        // 4. Retourner un UserModel pour l'UI
        return new UserModel(
                userData.getId(),
                userData.getEmail(),
                userData.getPseudo(),
                userData.getRole()
        );
    }
    public static void signUp(String name, String email, String password) throws Exception {
        if (name == null || name.isBlank()) throw new Exception("Pseudo obligatoire");
        if (email == null || email.isBlank()) throw new Exception("Email obligatoire");
        if (password == null || password.isBlank()) throw new Exception("Mot de passe obligatoire");

        // role "user" for compatibility
        UserData.createUser(email.trim(), name.trim(), password, "user");
    }
}
