
package portscan_linux;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PasswordWindow extends JFrame {

    private JPasswordField passwordField;

    public PasswordWindow() {
        super("Password Entry");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter Password:");
        passwordField = new JPasswordField(20);
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredPassword = new String(passwordField.getPassword());
                if (verifyPassword(enteredPassword)) {
                    JOptionPane.showMessageDialog(PasswordWindow.this, "Password is correct. Proceed to the application.");
                    dispose();
                    new NmapGUI().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(PasswordWindow.this, "Incorrect password. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(label);
        panel.add(passwordField);
        panel.add(submitButton);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        add(panel);
        setSize(300, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private boolean verifyPassword(String password) {
        String url = "jdbc:mysql://mysql_db:3306/mydatabase";
        String dbUsername = "user";
        String dbPassword = "password";

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String query = "SELECT * FROM passwords WHERE password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, password);
                ResultSet rs = stmt.executeQuery();
                return rs.next();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
