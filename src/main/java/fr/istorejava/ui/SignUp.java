package fr.istorejava.ui;

import fr.istorejava.data.UserData;
import fr.istorejava.logique.Authentication;

import javax.swing.*;
import java.awt.*;

public class SignUp extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    public SignUp() {

        setTitle("Sign Up");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // ===== LEFT PANEL =====
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(0, 102, 102));
        leftPanel.setLayout(new BorderLayout());

        JLabel companyLabel = new JLabel("COMPANY NAME", SwingConstants.CENTER);
        companyLabel.setForeground(Color.WHITE);
        companyLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel copyright = new JLabel(
                "copyright © company name All rights reserved",
                SwingConstants.CENTER
        );
        copyright.setForeground(Color.LIGHT_GRAY);
        copyright.setFont(new Font("Arial", Font.PLAIN, 10));

        leftPanel.add(companyLabel, BorderLayout.CENTER);
        leftPanel.add(copyright, BorderLayout.SOUTH);

        // ===== RIGHT PANEL =====
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(null);

        JLabel title = new JLabel("SIGN UP");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(60, 40, 200, 30);
        rightPanel.add(title);

        JLabel nameLabel = new JLabel("Full name");
        nameLabel.setBounds(60, 90, 200, 20);
        rightPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(60, 115, 250, 30);
        rightPanel.add(nameField);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(60, 160, 200, 20);
        rightPanel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(60, 185, 250, 30);
        rightPanel.add(emailField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(60, 230, 200, 20);
        rightPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(60, 255, 250, 30);
        rightPanel.add(passwordField);

        JButton signupButton = new JButton("Sign Up");
        signupButton.setBounds(60, 310, 120, 35);
        signupButton.setBackground(new Color(0, 102, 102));
        signupButton.setForeground(Color.WHITE);
        signupButton.setFocusPainted(false);
        rightPanel.add(signupButton);

        JLabel loginLabel = new JLabel("I've an account");
        loginLabel.setBounds(60, 360, 100, 20);
        rightPanel.add(loginLabel);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(165, 355, 80, 30);
        rightPanel.add(loginButton);

        // ===== ACTIONS =====

        // Création du compte
        signupButton.addActionListener(e -> {
            String pseudo = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (pseudo.isBlank() || email.isBlank() || password.isBlank()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            try {
                // ✅ Signup via LOGIQUE (whitelist + hash + insert DB)
                Authentication.signUp(pseudo, email, password);

                JOptionPane.showMessageDialog(this,
                        "Compte créé ✅\nVous pouvez maintenant vous connecter.",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);

                new Login().setVisible(true);
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Aller vers Login
        loginButton.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });

        // ===== ADD PANELS =====
        add(leftPanel);
        add(rightPanel);
    }

    // ===== LOGIQUE SIGN UP =====
    private void createAccount() {

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Vérification des champs
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez remplir tous les champs",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Création du compte via UserData
            UserData.createUser(email, name, password, "user"); // role par défaut "user"

            JOptionPane.showMessageDialog(this,
                    "Compte créé avec succès !",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);

            // Aller vers Login après création
            new Login().setVisible(true);
            dispose();

        } catch (RuntimeException ex) {
            // Si email déjà utilisé
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===== MAIN POUR TEST =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignUp().setVisible(true));
    }
}
