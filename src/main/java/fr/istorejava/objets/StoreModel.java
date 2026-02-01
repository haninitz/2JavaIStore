package fr.istorejava.objets;

public class StoreModel {

    private final int id;
    private final String name;

    public StoreModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return name;
    }
}
