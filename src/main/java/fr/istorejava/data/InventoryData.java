package fr.istorejava.data;

public class InventoryData {
    int id;
    java.util.List<ItemData> items = new java.util.ArrayList<>();

    public InventoryData(int id) {
        this.id = id;
    }

    public void addItem(ItemData item) {
        items.add(item);
    }

    public void removeItem(ItemData item) {
        items.remove(item);
    }
}
