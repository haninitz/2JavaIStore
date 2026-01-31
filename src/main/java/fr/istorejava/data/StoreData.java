package fr.istorejava.data;

public class StoreData {
    int id;
    String name;
    InventoryData inventory;
    java.util.List<UserData> employees = new java.util.ArrayList<>();

    public StoreData(int id, String name) {
        this.id = id;
        this.name = name;
        this.inventory = new InventoryData(id); // un inventaire par store
    }

    public void addEmployee(UserData employee) {
        employees.add(employee);
    }

    public void removeEmployee(UserData employee) {
        employees.remove(employee);
    }

    // ===== GETTER =====
    public String getName() {
        return name;
    }
}
