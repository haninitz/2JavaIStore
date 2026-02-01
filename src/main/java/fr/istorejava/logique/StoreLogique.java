package fr.istorejava.logique;

import fr.istorejava.data.StoreData;
import fr.istorejava.data.UserData;

import java.util.List;

public class StoreLogique {

    private StoreLogique() {}

    // ----------------- Public API (appelée par UI plus tard) -----------------

    /** Admin : créer un store */
    public static StoreData createStore(UserData currentUser, String name) throws Exception {
        requireLoggedIn(currentUser);
        requireAdmin(currentUser);
        if (name == null || name.isBlank()) throw new Exception("Nom du store obligatoire");
        return StoreData.createStore(name.trim());
    }

    /** Admin : supprimer un store */
    public static void deleteStore(UserData currentUser, int storeId) throws Exception {
        requireLoggedIn(currentUser);
        requireAdmin(currentUser);
        StoreData.deleteStore(storeId);
    }

    /**
     * Admin : voit tous les stores
     * Employee : voit seulement ses stores
     */
    public static List<StoreData> listStores(UserData currentUser) throws Exception {
        requireLoggedIn(currentUser);
        if (isAdmin(currentUser)) return StoreData.getAllStores();
        return StoreData.getStoresForEmployee(currentUser.getId());
    }

    /** Admin : assigner un employé à un store */
    public static void addEmployeeToStore(UserData currentUser, int storeId, int employeeUserId) throws Exception {
        requireLoggedIn(currentUser);
        requireAdmin(currentUser);
        StoreData.addEmployeeToStore(storeId, employeeUserId);
    }

    /** Admin : enlever un employé d’un store */
    public static void removeEmployeeFromStore(UserData currentUser, int storeId, int employeeUserId) throws Exception {
        requireLoggedIn(currentUser);
        requireAdmin(currentUser);
        StoreData.removeEmployeeFromStore(storeId, employeeUserId);
    }

    /**
     * Admin : peut voir les employés qui ont accès au store
     * Employee : peut voir la liste SEULEMENT s’il a accès à ce store
     */
    public static List<UserData> listEmployeesWithAccess(UserData currentUser, int storeId) throws Exception {
        requireLoggedIn(currentUser);
        if (!isAdmin(currentUser) && !hasAccessToStore(currentUser, storeId)) {
            throw new Exception("Accès refusé : vous n'avez pas accès à ce store");
        }
        return StoreData.getEmployeesWithAccess(storeId);
    }

    // ----------------- Helpers -----------------

    private static void requireLoggedIn(UserData user) throws Exception {
        if (user == null) throw new Exception("Utilisateur non connecté");
    }

    private static void requireAdmin(UserData user) throws Exception {
        if (!isAdmin(user)) throw new Exception("Accès refusé : admin requis");
    }

    private static boolean isAdmin(UserData user) {
        return user != null && "admin".equalsIgnoreCase(user.getRole());
    }

    /** vérifie si l'employé fait partie du store (simple, suffisant pour le projet) */
    private static boolean hasAccessToStore(UserData user, int storeId) {
        List<StoreData> stores = StoreData.getStoresForEmployee(user.getId());
        for (StoreData s : stores) {
            if (s.getId() == storeId) return true;
        }
        return false;
    }
}
