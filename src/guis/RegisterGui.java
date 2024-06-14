package guis;

import constants.CommonConstants;
import db_objs.MyJDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// 1. Class and Object: This is a class definition for RegisterGui which extends BaseFrame
public class RegisterGui extends BaseFrame {

    // 1. Constructor: Initializes the RegisterGui object
    public RegisterGui() {
        super("Wallet Mahasiswa Register");
    }

    // 4. Superclass and Subclass: This method overrides a method from the superclass BaseFrame
    @Override
    protected void addGuiComponents() {
        // 13. Swing component: JLabel
        JLabel bankingAppLabel = new JLabel("Register Wallet Mahasiswa");
        bankingAppLabel.setBounds(0, 20, super.getWidth(), 40);
        bankingAppLabel.setFont(new Font("Dialog", Font.BOLD, 32));
        bankingAppLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bankingAppLabel.setForeground(CommonConstants.TEXT_COLOR);
        add(bankingAppLabel);

        // 13. Swing component: JLabel
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(20, 120, getWidth() - 30, 24);
        usernameLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        usernameLabel.setForeground(CommonConstants.TEXT_COLOR);
        add(usernameLabel);

        // 13. Swing component: JTextField
        JTextField usernameField = new JTextField();
        usernameField.setBounds(20, 160, getWidth() - 50, 40);
        usernameField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(usernameField);

        // 13. Swing component: JLabel
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 220, getWidth() - 50, 24);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        passwordLabel.setForeground(CommonConstants.TEXT_COLOR);
        add(passwordLabel);

        // 13. Swing component: JPasswordField
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(20, 260, getWidth() - 50, 40);
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(passwordField);

        // 13. Swing component: JLabel
        JLabel rePasswordLabel = new JLabel("Re-type Password:");
        rePasswordLabel.setBounds(20, 320, getWidth() - 50, 40);
        rePasswordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        rePasswordLabel.setForeground(CommonConstants.TEXT_COLOR);
        add(rePasswordLabel);

        // 13. Swing component: JPasswordField
        JPasswordField rePasswordField = new JPasswordField();
        rePasswordField.setBounds(20, 360, getWidth() - 50, 40);
        rePasswordField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(rePasswordField);

        // 13. Swing component: JLabel
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(20, 400, getWidth() - 50, 24);
        phoneLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        phoneLabel.setForeground(CommonConstants.TEXT_COLOR);
        add(phoneLabel);

        // 13. Swing component: JTextField
        JTextField phoneField = new JTextField();
        phoneField.setBounds(20, 440, getWidth() - 50, 40);
        phoneField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(phoneField);

        // 13. Swing component: JButton
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(20, 500, getWidth() - 50, 40);
        registerButton.setFont(new Font("Dialog", Font.BOLD, 20));
        
        // 10. Listener interface: ActionListener
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 11. Action event: Handling button click
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());
                String rePassword = String.valueOf(rePasswordField.getPassword());
                String phone = phoneField.getText();

                // Validate user input
                if (validateUserInput(username, password, rePassword, phone)) {
                    // 14. JDBC: Register user in the database
                    if (MyJDBC.register(username, password, phone)) {
                        // Registration success
                        RegisterGui.this.dispose();
                        LoginGui loginGui = new LoginGui();
                        loginGui.setVisible(true);
                        JOptionPane.showMessageDialog(loginGui, "Registrasi Berhasil! Silahkan Login");
                    } else {
                        // Registration failed
                        JOptionPane.showMessageDialog(RegisterGui.this, "Error: Username telah digunakan!");
                    }
                } else {
                    // Invalid user input
                    JOptionPane.showMessageDialog(RegisterGui.this,
                            "Error: username minimal memiliki 6 karakter\n" +
                            "and/or Password tidak sama!");
                }
            }
        });
        add(registerButton);

        // 13. Swing component: JLabel with HTML link
        JLabel loginLabel = new JLabel("<html><a href=\"#\">Have an account? Sign-in here</a></html>");
        loginLabel.setBounds(0, 550, getWidth() - 10, 30);
        loginLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // 10. Listener interface: MouseAdapter
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 11. Action event: Handling label click
                RegisterGui.this.dispose();
                new LoginGui().setVisible(true);
            }
        });
        add(loginLabel);
    }

    // 2. Encapsulation: Private method to validate user input
    private boolean validateUserInput(String username, String password, String rePassword, String phone) {
        if (username.length() == 0 || password.length() == 0 || rePassword.length() == 0 || phone.length() == 0) return false;
        if (username.length() < 6) return false;
        if (!password.equals(rePassword)) return false;
        return true;
    }
}