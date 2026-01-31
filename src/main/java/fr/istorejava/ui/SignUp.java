package fr.istorejava.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SignupWindow extends JFrame {

    private JTextField emailField;
    private JTextField pseudoField;
    private JPasswordField passwordField;
    private JButton createButton;
    private JLabel messageLabel;

    public SignupWindow() {
        setTitle("Créer un compte - iStore");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 1));

        emailField = new JTextField();
        pseudoField = new JTextField();
        passwordField = new JPasswordField();
        createButton = new JButton("Créer");
        messageLabel = new JLabel("", SwingConstants.CENTER);

        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Pseudo:"));
        add(pseudoField);
        add(new JLabel("Mot de passe:"));
        add(passwordField);
        add(createButton);
        add(messageLabel);

        createButton.addActionListener((ActionEvent e) -> {
            String email = emailField.getText();
            String pseudo = pseudoField.getText();
            String password = new String(passwordField.getPassword());

            // TODO: Vérifier si email whitelisté dans MySQL
            // TODO: Hash mot de passe et insérer dans la table users
            boolean success = true; // remplacer par vérification

            if (success) {
                messageLabel.setText("Compte créé avec succès !");
            } else {
                messageLabel.setText("Erreur : email non whitelisté ou déjà utilisé");
            }
        });
    }
}
