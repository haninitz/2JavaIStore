package fr.istorejava.logique;

import fr.istorejava.data.UserData;
import fr.istorejava.objets.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserLogique {

    private UserLogique() {}

    /** Lecture autorisée pour tout le monde (sans mot de passe évidemment). */
    public static List<UserModel> listUsers() {
        List<UserModel> out = new ArrayList<>();
        for (UserData u : UserData.getAllUsers()) {
            out.add(toModel(u));
        }
        return out;
    }

    public static UserModel getUserByEmail(String email) {
        UserData u = UserData.getUserByEmail(email);
        return u == null ? null : toModel(u);
    }

    /** Admin peut modifier n’importe qui. Non-admin peut modifier seulement lui-même. */
    public static void updateUser(UserModel currentUser,
                                  int targetUserId,
                                  String newEmail,
                                  String newPseudo,
                                  String newPasswordOrEmpty,
                                  String newRoleOrNull) throws Exception {

        if (currentUser == null) throw new Exception("Utilisateur non connecté");

        boolean isSelf = currentUser.getId() == targetUserId;
        boolean isAdmin = currentUser.isAdmin();

        if (!isAdmin && !isSelf) {
            throw new Exception("Accès refusé : vous ne pouvez modifier que votre compte");
        }

        UserData target = UserData.getUserById(targetUserId);
        if (target == null) throw new Exception("Utilisateur introuvable");

        // Règle : seul admin peut changer le rôle
        String roleToApply = target.getRole(); // reste pareil par défaut
        if (isAdmin && newRoleOrNull != null && !newRoleOrNull.isBlank()) {
            roleToApply = newRoleOrNull;
        }

        // Pour éviter de forcer un changement de mot de passe si champ vide
        String pwd = (newPasswordOrEmpty == null) ? "" : newPasswordOrEmpty;

        target.update(
                safe(newEmail, target.getEmail()),
                safe(newPseudo, target.getPseudo()),
                pwd,
                roleToApply
        );
    }

    /** Admin peut supprimer n’importe qui. Non-admin peut supprimer seulement lui-même. */
    public static void deleteUser(UserModel currentUser, int targetUserId) throws Exception {
        if (currentUser == null) throw new Exception("Utilisateur non connecté");

        boolean isSelf = currentUser.getId() == targetUserId;
        boolean isAdmin = currentUser.isAdmin();

        if (!isAdmin && !isSelf) {
            throw new Exception("Accès refusé : vous ne pouvez supprimer que votre compte");
        }

        UserData target = UserData.getUserById(targetUserId);
        if (target == null) throw new Exception("Utilisateur introuvable");

        // Option (recommandé) : empêcher l’admin de se supprimer lui-même
        if (isAdmin && isSelf) {
            throw new Exception("Action refusée : un admin ne peut pas supprimer son propre compte");
        }

        target.delete();
    }

    // ----------------- helpers -----------------

    private static String safe(String v, String fallback) {
        if (v == null) return fallback;
        String t = v.trim();
        return t.isEmpty() ? fallback : t;
    }

    private static UserModel toModel(UserData u) {
        // UserModel attend un rôle en minuscule pour l’UI (admin/user)
        // UserData.getRole() renvoie déjà en minuscule dans le patch.
        return new UserModel(u.getId(), u.getEmail(), u.getPseudo(), u.getRole());
    }
}
