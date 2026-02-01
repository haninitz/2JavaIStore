package fr.istorejava.db_connection;

import fr.istorejava.data.StoreData;
import fr.istorejava.data.UserData;

public class Session {

    private static UserData currentUser;
    private static StoreData currentStore;

    private Session() {}

    public static UserData getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(UserData user) {
        currentUser = user;
    }

    public static StoreData getCurrentStore() {
        return currentStore;
    }

    public static void setCurrentStore(StoreData store) {
        currentStore = store;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static void clear() {
        currentUser = null;
        currentStore = null;
    }
}
