package fr.istorejava.objets;

public class ItemModel {

    private final int id;
    private final String name;
    private final double price;

    public ItemModel(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return name + " (" + price + "€)";
    }
}
