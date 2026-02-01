package fr.istorejava.logique;

import fr.istorejava.data.InventoryData;
import fr.istorejava.data.ItemData;
import fr.istorejava.data.StoreData;
import fr.istorejava.data.UserData;

import java.util.List;

public class InventoryLogique {

    private InventoryLogique() {}

    /**
     * Voir l'inventaire d'un store
     * Admin : OK
     * Employee : seulement si accès au store
     */
    public static List<ItemData> getInventory(UserData currentUser, int storeId) throws Exception {
        requireLoggedIn(currentUser);
        if (!isAdmin(currentUser) && !hasAccessToStore(currentUser, storeId)) {
            throw new Exception("Accès refusé : vous n'avez pas accès à ce store");
        }
        return InventoryData.getInventoryItems(storeId);
    }

    /**
     * Modifier le stock (+/-)
     * Admin : OK
     * Employee : seulement si accès au store
     * Règle : stock ne descend jamais sous 0 (déjà géré dans InventoryData)
     */
    public static void changeStock(UserData currentUser, int storeId, int itemId, int delta) throws Exception {
        requireLoggedIn(currentUser);

        if (delta == 0) return;
        if (delta != 1 && delta != -1) {
            throw new Exception("Delta invalide (utiliser +1 ou -1)");
        }

        if (!isAdmin(currentUser) && !hasAccessToStore(currentUser, storeId)) {
            throw new Exception("Accès refusé : vous n'avez pas accès à ce store");
        }

        InventoryData.updateQuantity(storeId, itemId, delta);
    }

    // --- Bonus utile pour l'admin (items) ---
    public static ItemData createItem(UserData currentUser, String name, double price) throws Exception {
        requireLoggedIn(currentUser);
        requireAdmin(currentUser);
        if (name == null || name.isBlank()) throw new Exception("Nom item obligatoire");
        if (price < 0) throw new Exception("Prix invalide");
        return ItemData.createItem(name.trim(), price);
    }

    public static void deleteItem(UserData currentUser, int itemId) throws Exception {
        requireLoggedIn(currentUser);
        requireAdmin(currentUser);
        ItemData.deleteItem(itemId);
    }

    public static List<ItemData> listItems(UserData currentUser) throws Exception {
        requireLoggedIn(currentUser);
        requireAdmin(currentUser);
        return ItemData.getAllItems();
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

    private static boolean hasAccessToStore(UserData user, int storeId) {
        List<StoreData> stores = StoreData.getStoresForEmployee(user.getId());
        for (StoreData s : stores) {
            if (s.getId() == storeId) return true;
        }
        return false;
    }
}
