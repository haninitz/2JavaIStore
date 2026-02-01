package fr.istorejava;

import fr.istorejava.data.UserData;
import fr.istorejava.data.WhitelistData;
import fr.istorejava.data.StoreData;
import fr.istorejava.data.ItemData;

import fr.istorejava.logique.Authentication;
import fr.istorejava.logique.UserLogique;
import fr.istorejava.logique.StoreLogique;
import fr.istorejava.logique.InventoryLogique;

import fr.istorejava.objets.UserModel;

import java.util.List;

public class TestLogic {

    public static void main(String[] args) {
        System.out.println("=== TEST LOGIQUE iStore ===");

        try {
            // 1️⃣ Whitelist
            safeWhitelist("admin@istore.com");
            safeWhitelist("emp@istore.com");

            // 2️⃣ Signup + login admin
            safeSignup("Admin", "admin@istore.com", "test123");
            UserModel admin = Authentication.login("admin@istore.com", "test123");
            System.out.println("[OK] Admin connecté : " + admin.getEmail());

            // 3️⃣ Signup + login employee
            safeSignup("Employee", "emp@istore.com", "test2");
            UserModel employee = Authentication.login("emp@istore.com", "test2");
            System.out.println("[OK] Employee connecté : " + employee.getEmail());

            // 4️⃣ Users CRUD
            System.out.println("[INFO] Nombre d'utilisateurs : " + UserLogique.listUsers().size());

            // Admin modifie employee
            UserModel empFromList = UserLogique.getUserByEmail("emp@istore.com");
            UserLogique.updateUser(admin, empFromList.getId(), null, "EmpRenamed", "", null);
            System.out.println("[OK] Admin a modifié employee");

            // Employee essaie de modifier admin (doit échouer)
            try {
                UserModel adminFromList = UserLogique.getUserByEmail("admin@istore.com");
                UserLogique.updateUser(employee, adminFromList.getId(), null, "HACK", "", null);
                System.out.println("[ERREUR] L'employee a modifié admin ❌");
            } catch (Exception e) {
                System.out.println("[OK] Employee bloqué (normal)");
            }

            // 5️⃣ Stores
            StoreData store = StoreLogique.createStore(UserData.getUserByEmail("admin@istore.com"), "Store Test");
            System.out.println("[OK] Store créé : " + store.getName());

            StoreLogique.addEmployeeToStore(
                    UserData.getUserByEmail("admin@istore.com"),
                    store.getId(),
                    UserData.getUserByEmail("emp@istore.com").getId()
            );
            System.out.println("[OK] Employee assigné au store");

            List<StoreData> empStores = StoreLogique.listStores(UserData.getUserByEmail("emp@istore.com"));
            System.out.println("[OK] Stores visibles par employee : " + empStores.size());

            // 6️⃣ Items + Inventory
            ItemData item = InventoryLogique.createItem(
                    UserData.getUserByEmail("admin@istore.com"),
                    "Item Test",
                    10.0
            );
            System.out.println("[OK] Item créé : " + item.getName());

            InventoryLogique.changeStock(
                    UserData.getUserByEmail("emp@istore.com"),
                    store.getId(),
                    item.getId(),
                    +1
            );
            System.out.println("[OK] Stock +1");

            InventoryLogique.changeStock(
                    UserData.getUserByEmail("emp@istore.com"),
                    store.getId(),
                    item.getId(),
                    -1
            );
            System.out.println("[OK] Stock -1");

            // Tentative stock négatif
            try {
                InventoryLogique.changeStock(
                        UserData.getUserByEmail("emp@istore.com"),
                        store.getId(),
                        item.getId(),
                        -1
                );
                System.out.println("[ERREUR] Stock négatif autorisé ❌");
            } catch (Exception e) {
                System.out.println("[OK] Stock négatif bloqué");
            }

            System.out.println("=== FIN DES TESTS : OK ===");

        } catch (Exception e) {
            System.out.println("=== ERREUR TEST ===");
            e.printStackTrace();
        }
    }

    // ---------------- HELPERS ----------------

    private static void safeWhitelist(String email) {
        try {
            WhitelistData.addEmail(email);
            System.out.println("[OK] Whitelist : " + email);
        } catch (Exception ignored) {
            System.out.println("[INFO] Déjà whitelisté : " + email);
        }
    }

    private static void safeSignup(String name, String email, String password) {
        try {
            Authentication.signUp(name, email, password);
            System.out.println("[OK] Signup : " + email);
        } catch (Exception e) {
            System.out.println("[INFO] Signup ignoré : " + email);
        }
    }
}
