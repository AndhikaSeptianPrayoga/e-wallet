package guis;
import constants.CommonConstants;

import db_objs.MyJDBC;
import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*
   GUI ini digunakan untuk login ke aplikasi wallet mahasiswa
   dan dapat mengaharah ke GUI Register jika belum memiliki akun.

 */
public class LoginGui extends BaseFrame{
    public LoginGui(){
        super("EduWallet Siswa Login");
    }

    @Override
    protected void addGuiComponents() {
        // create banking app label
        JLabel bankingAppLabel = new JLabel("EduWallet: Tabungan Siswa");

        // set the location and the size of the gui component
        bankingAppLabel.setBounds(-10, 20, super.getWidth(), 40);

        // change the font style
        bankingAppLabel.setFont(new Font("Dialog", Font.BOLD, 28));

        // center text in Jlabel
        bankingAppLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // set the color of the text
        bankingAppLabel.setForeground(CommonConstants.TEXT_COLOR);

        // add to gui
        add(bankingAppLabel);

        // create logo label
        JLabel logoLabel = new JLabel(new ImageIcon("src/assets/logo/logo1.png"));

        logoLabel.setBounds(0, 50, 400, 200);

        // center the logo in the label
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // add the logo to the gui
        add(logoLabel);

        // username label
        JLabel usernameLabel = new JLabel("Username:");

        // getWidth() returns us the width of our frame which is about 420
        usernameLabel.setBounds(20, 250, getWidth() - 30, 24);

        usernameLabel.setFont(new Font("Dialog", Font.PLAIN, 20));

        // change the color of the text
        usernameLabel.setForeground(CommonConstants.TEXT_COLOR);

        add(usernameLabel);

        // create username field
        JTextField usernameField = new JTextField();
        usernameField.setBounds(20, 280, getWidth() - 60, 40);
        usernameField.setFont(new Font("Dialog", Font.PLAIN, 28));
        usernameField.setForeground(CommonConstants.SECONDARY_COLOR);
        add(usernameField);

        // create password label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 330, getWidth() - 60, 24);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        passwordLabel.setForeground(CommonConstants.TEXT_COLOR);
        add(passwordLabel);

        // create password field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(20, 360, getWidth() - 60, 40);
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 28));
        passwordField.setForeground(CommonConstants.SECONDARY_COLOR);
        add(passwordField);

        // create login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(20, 460, getWidth() - 60, 40);
        loginButton.setFont(new Font("Dialog", Font.BOLD, 20));
        loginButton.setForeground(CommonConstants.SECONDARY_COLOR);
        loginButton.setBackground(CommonConstants.BUTTON_COLOR);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get username
                String username = usernameField.getText();

                // get password
                String password = String.valueOf(passwordField.getPassword());

                // validate login
                User user = MyJDBC.validateLogin(username, password);

                // if user is null it means invalid otherwise it is a valid account
                if(user != null){
                    // means valid login

                    // dispose this gui
                    LoginGui.this.dispose();

                    // launch bank app gui
                    BankingAppGui bankingAppGui = new BankingAppGui(user);
                    bankingAppGui.setVisible(true);

                    // show success dialog
                    JOptionPane.showMessageDialog(bankingAppGui, "Login Successfully!");
                }else{
                    // invalid login
                    JOptionPane.showMessageDialog(LoginGui.this, "Login failed...");
                }
            }
        });
        add(loginButton);

        // create register label
        JLabel registerLabel = new JLabel("<html><a href=\"#\">Don't have an account? Register Here</a></html>");
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.setForeground(Color.BLUE);
        registerLabel.setBounds(0, 510, getWidth() - 20, 30);
        registerLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);


        // adds an event listener so when the mouse is clicked it will launch the register gui
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // dispose of this gui
                LoginGui.this.dispose();

                // launch the register gui
                new RegisterGui().setVisible(true);
            }
        });

        add(registerLabel);
    }
}















