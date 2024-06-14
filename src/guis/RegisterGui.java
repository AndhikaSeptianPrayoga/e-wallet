package guis;

import db_objs.MyJDBC;
import java.math.BigInteger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterGui extends BaseFrame {
    public RegisterGui() {
        super("Wallet Mahasiswa Register");
        setSize(420, 600);
    }

    @Override
    protected void addGuiComponents() {
        // Container Panel
        JPanel container = new JPanel();
        container.setLayout(null);
        container.setBackground(Color.WHITE);
        container.setBounds(0, 0, 420, 600);
        add(container);

        // Title label
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(85, 20, 250, 40);
        titleLabel.setForeground(Color.BLACK);
        container.add(titleLabel);

        // Subtext label
        JLabel subtextLabel = new JLabel("I Am A New User");
        subtextLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtextLabel.setBounds(85, 60, 250, 20);
        subtextLabel.setForeground(Color.BLACK);
        container.add(subtextLabel);

        // Username label
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        usernameLabel.setBounds(50, 100, 250, 20);
        usernameLabel.setForeground(Color.BLACK);
        container.add(usernameLabel);

        // Username field
        JTextField usernameField = new JTextField();
        usernameField.setBounds(50, 130, 320, 40);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 18));
        usernameField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        container.add(usernameField);

        // Password label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordLabel.setBounds(50, 180, 250, 20);
        passwordLabel.setForeground(Color.BLACK);
        container.add(passwordLabel);

        // Password field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(50, 210, 320, 40);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        container.add(passwordField);

        // Re-type Password label
        JLabel rePasswordLabel = new JLabel("Re-type Password");
        rePasswordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        rePasswordLabel.setBounds(50, 260, 250, 20);
        rePasswordLabel.setForeground(Color.BLACK);
        container.add(rePasswordLabel);

        // Re-type Password field
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
                // get username
                String username = usernameField.getText();
                // get password
                String password = String.valueOf(passwordField.getPassword());
                // get re-password
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
                        JOptionPane.showMessageDialog(RegisterGui.this, "Error: Username atau nomor telepon telah digunakan!");
                    }
                } else {
                    JOptionPane.showMessageDialog(RegisterGui.this,
                            "Error: username minimal memiliki 6 karakter\n" +
                                    "and/or Password tidak sama!");
                }
            }
        });
        container.add(registerButton);

        // 13. Swing component: JLabel with HTML link
        JLabel loginLabel = new JLabel("<html><a href=\"#\">Have an account? Sign-in here</a></html>");
        loginLabel.setBounds(0, 550, getWidth() - 10, 30);
        loginLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // 10. Listener interface: MouseAdapter
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                RegisterGui.this.dispose();
                new LoginGui().setVisible(true);
            }
        });
        container.add(loginLabel);
    }

    // 2. Encapsulation: Private method to validate user input
    private boolean validateUserInput(String username, String password, String rePassword, String phone) {
        if (username.length() == 0 || password.length() == 0 || rePassword.length() == 0 || phone.length() == 0) return false;
        if (username.length() < 6) return false;
        // password and re-password must be the same
        if (!password.equals(rePassword)) return false;
        // passes validation
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RegisterGui().setVisible(true);
            }
        });
    }
}