package fr.istorejava.ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;
    private JLabel messageLabel;

    public LoginWindow() {
        setTitle("Login - iStore");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1));

        emailField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Se connecter");
        signupButton = new JButton("Créer un compte");
        messageLabel = new JLabel("", SwingConstants.CENTER);

        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Mot de passe:"));
        add(passwordField);
        add(loginButton);
        add(signupButton);
        add(messageLabel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                // TODO: Vérifier email et mot de passe dans MySQL
                boolean success = false; // remplacer par la vérification
                String role = ""; // récupérer le rôle depuis MySQL

                if (success) {
                    dispose(); // fermer la fenêtre de login
                    if (role.equals("admin")) {
                        new AdminDashboard().setVisible(true);
                    } else {
                        new EmployeeDashboard().setVisible(true);
                    }
                } else {
                    messageLabel.setText("Email ou mot de passe incorrect");
                }
            }
        });

        signupButton.addActionListener(e -> {
            dispose();
            new SignupWindow().setVisible(true);
        });
    }

    public static void main(String[] args) {
        new LoginWindow().setVisible(true);
    }
}

