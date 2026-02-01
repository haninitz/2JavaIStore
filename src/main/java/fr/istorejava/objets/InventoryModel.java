package fr.istorejava.objets;

public class InventoryModel {

    private final int itemId;
    private final String itemName;
    private final double price;
    private final int quantity;

    public InventoryModel(int itemId, String itemName, double price, int quantity) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

    public int getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    @Override
    public String toString() {
        return itemName + " - " + price + "€ - Stock: " + quantity;
    }
}
