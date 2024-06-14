package guis; // Deklarasi paket

import constants.CommonConstants; // Mengimpor konstanta

import db_objs.MyJDBC; // Mengimpor kelas utilitas JDBC
import db_objs.User; // Mengimpor kelas User

import javax.swing.*; // Mengimpor komponen Swing
import java.awt.*; // Mengimpor komponen AWT
import java.awt.event.ActionEvent; // Mengimpor ActionEvent
import java.awt.event.ActionListener; // Mengimpor ActionListener
import java.awt.event.MouseAdapter; // Mengimpor MouseAdapter
import java.awt.event.MouseEvent; // Mengimpor MouseEvent

/*
   GUI ini digunakan untuk login ke aplikasi wallet mahasiswa
   dan dapat mengaharah ke GUI Register jika belum memiliki akun.
 */
public class LoginGui extends BaseFrame { // Deklarasi kelas, LoginGui adalah subclass dari BaseFrame

    // Konstruktor
    public LoginGui() {
        super("EduWallet Siswa Login"); // Memanggil konstruktor superclass
    }

    @Override
    protected void addGuiComponents() { // Metode yang di-override dari superclass
        // membuat label aplikasi perbankan
        JLabel bankingAppLabel = new JLabel("EduWallet: Tabungan Siswa");

        // mengatur lokasi dan ukuran komponen GUI
        bankingAppLabel.setBounds(-10, 20, super.getWidth(), 40);

        // mengubah gaya font
        bankingAppLabel.setFont(new Font("Dialog", Font.BOLD, 28));

        // memusatkan teks di JLabel
        bankingAppLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // mengatur warna teks
        bankingAppLabel.setForeground(CommonConstants.TEXT_COLOR);

        // menambahkan ke GUI
        add(bankingAppLabel);

        // membuat label logo
        JLabel logoLabel = new JLabel(new ImageIcon("src/assets/logo/logo1.png"));

        logoLabel.setBounds(0, 50, 400, 200);

        // memusatkan logo di label
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // menambahkan logo ke GUI
        add(logoLabel);

        // label username
        JLabel usernameLabel = new JLabel("Username:");

        // getWidth() mengembalikan lebar frame kita yang sekitar 420
        usernameLabel.setBounds(20, 250, getWidth() - 30, 24);

        usernameLabel.setFont(new Font("Dialog", Font.PLAIN, 20));

        // mengubah warna teks
        usernameLabel.setForeground(CommonConstants.TEXT_COLOR);

        add(usernameLabel);

        // membuat field username
        JTextField usernameField = new JTextField();
        usernameField.setBounds(20, 280, getWidth() - 60, 40);
        usernameField.setFont(new Font("Dialog", Font.PLAIN, 28));
        usernameField.setForeground(CommonConstants.SECONDARY_COLOR);
        add(usernameField);

        // membuat label password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 330, getWidth() - 60, 24);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        passwordLabel.setForeground(CommonConstants.TEXT_COLOR);
        add(passwordLabel);

        // membuat field password
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(20, 360, getWidth() - 60, 40);
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 28));
        passwordField.setForeground(CommonConstants.SECONDARY_COLOR);
        add(passwordField);

        // membuat tombol login
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(20, 460, getWidth() - 60, 40);
        loginButton.setFont(new Font("Dialog", Font.BOLD, 20));
        loginButton.setForeground(CommonConstants.SECONDARY_COLOR);
        loginButton.setBackground(CommonConstants.BUTTON_COLOR);
        loginButton.addActionListener(new ActionListener() { // Menambahkan ActionListener
            @Override
            public void actionPerformed(ActionEvent e) { // Metode yang dipanggil saat tombol diklik
                // mendapatkan username
                String username = usernameField.getText();

                // mendapatkan password
                String password = String.valueOf(passwordField.getPassword());

                // memvalidasi login
                User user = MyJDBC.validateLogin(username, password);

                // jika user null berarti tidak valid, jika tidak berarti akun valid
                if(user != null){
                    // berarti login valid

                    // menutup GUI ini
                    LoginGui.this.dispose();

                    // meluncurkan GUI aplikasi perbankan
                    if (user.getRole() == 1) {
                        DashboardAppGui dashboardAppGui = new DashboardAppGui();
                        dashboardAppGui.setVisible(true);
                        JOptionPane.showMessageDialog(dashboardAppGui, "Login as Admin Successfully!");
                    } else {
                        BankingAppGui bankingAppGui = new BankingAppGui(user);
                        bankingAppGui.setVisible(true);
                        JOptionPane.showMessageDialog(bankingAppGui, "Login Successfully!");
                    }
                }else{
                    // login tidak valid
                    JOptionPane.showMessageDialog(LoginGui.this, "Login failed...");
                }
            }
        });
        add(loginButton);

        // membuat label register
        JLabel registerLabel = new JLabel("<html><a href=\"#\">Don't have an account? Register Here</a></html>");
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.setForeground(Color.BLUE);
        registerLabel.setBounds(0, 510, getWidth() - 20, 30);
        registerLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // menambahkan event listener sehingga saat mouse diklik akan meluncurkan GUI register
        registerLabel.addMouseListener(new MouseAdapter() { // Menambahkan MouseAdapter
            @Override
            public void mouseClicked(MouseEvent e) { // Metode yang dipanggil saat mouse diklik
                // menutup GUI ini
                LoginGui.this.dispose();

                // meluncurkan GUI register
                new RegisterGui().setVisible(true);
            }
        });

        add(registerLabel);
    }
}