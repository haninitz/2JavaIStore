package fr.istorejava.ui;

import javax.swing.SwingUtilities;

public class LoginAndSignUp {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });

    }
}

