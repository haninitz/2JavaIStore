package fr.istorejava.data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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