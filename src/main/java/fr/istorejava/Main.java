package fr.istorejava;

import javax.swing.SwingUtilities;
import fr.istorejava.ui.Login;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}