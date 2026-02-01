package fr.istorejava.ui;

import fr.istorejava.data.UserData;


import javax.swing.*;
import java.awt.*;
import fr.istorejava.data.UserData;
import fr.istorejava.logique.Authentication;
import fr.istorejava.objets.UserModel;
import fr.istorejava.db_connection.Session;


public class Login extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    public Login() {

        setTitle("Login");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // ===== LEFT PANEL =====
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(0, 102, 102));
        leftPanel.setLayout(new BorderLayout());

        JLabel companyLabel = new JLabel("SupIStore", SwingConstants.CENTER);
        companyLabel.setForeground(Color.WHITE);
        companyLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel copyright = new JLabel(
                "copyright © SupIStore All rights reserved",
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

        JLabel title = new JLabel("LOGIN");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(60, 40, 200, 30);
        rightPanel.add(title);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(60, 110, 200, 20);
        rightPanel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(60, 135, 250, 30);
        rightPanel.add(emailField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(60, 180, 200, 20);
        rightPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(60, 205, 250, 30);
        rightPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(60, 260, 120, 35);
        loginButton.setBackground(new Color(0, 102, 102));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        rightPanel.add(loginButton);

        JLabel signupLabel = new JLabel("Don't have an account?");
        signupLabel.setBounds(60, 315, 160, 20);
        rightPanel.add(signupLabel);

        JButton signupButton = new JButton("Sign Up");
        signupButton.setBounds(225, 310, 85, 30);
        rightPanel.add(signupButton);

        // ===== ACTIONS =====

        // Connexion
        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (email.isBlank() || password.isBlank()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir email et mot de passe.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            try {
                // ✅ Login via LOGIQUE (vérifie whitelist + password hash)
                UserModel userModel = Authentication.login(email, password);

                // ✅ On récupère le UserData (car ton UI utilise UserData)
                UserData userData = UserData.getUserByEmail(userModel.getEmail());

                // (Optionnel) session
                try { Session.setCurrentUser(userData); } catch (Exception ignored) {}

                // ✅ Aller vers Home
                new Home(userData, null).setVisible(true);
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });


        // Aller vers SignUp
        signupButton.addActionListener(e -> {
            new SignUp().setVisible(true);
            dispose();
        });

        // ===== ADD PANELS =====
        add(leftPanel);
        add(rightPanel);
    }

    // ===== LOGIQUE LOGIN =====
    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez remplir tous les champs",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Récupérer l'utilisateur
        UserData user = UserData.getUserByEmail(email);
        if (user == null) {
            JOptionPane.showMessageDialog(this,
                    "Compte inexistant",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Vérifier le mot de passe
        if (user.checkPassword(password)) {
            JOptionPane.showMessageDialog(this,
                    "Connexion réussie",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);

            // Ouvrir la fenêtre Home avec l'utilisateur connecté
            new Home(user, null).setVisible(true); // null pour le store si à choisir plus tard
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Mot de passe incorrect",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
