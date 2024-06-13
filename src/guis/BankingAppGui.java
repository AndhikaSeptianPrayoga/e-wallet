package guis;

import constants.CommonConstants;
import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
    Performs banking functions such as depositing, Tarik Danaing, seeing past transaction, and Transfer Danaring
    This extends from the BaseFrame which means we will need to define our own addGuiComponent
 */
public class BankingAppGui extends BaseFrame implements ActionListener {
    private JTextField currentBalanceField;
    public JTextField getCurrentBalanceField(){return currentBalanceField;}

    public BankingAppGui(User user){
        super("EduWallet", user);
    }
    @Override
    protected void addGuiComponents() {
        // create welcome message
        JButton logoutButton = new JButton("Logout");
        ImageIcon icon = new ImageIcon(new ImageIcon("src/assets/icon/logout.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        logoutButton.setIcon(icon);
        logoutButton.setHorizontalTextPosition(SwingConstants.CENTER);
        logoutButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        logoutButton.setFont(new Font("Dialog", Font.BOLD, 12));
        logoutButton.setBackground(null);
        logoutButton.setBorder(null);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setOpaque(false);
        logoutButton.setBounds(getWidth() - 110, 15, 80, 50); // Adjust the position and size as needed
        logoutButton.addActionListener(this);
        add(logoutButton);
        String namaUser = user.getUsername();
        String welcomeMessage = "<html>" +
                "<body style='text-align:center'>" +
                "<b>Anda Login Sebagai " + "<b style='color:#FF5733;'>" + namaUser + "</b>" + "</b><br>" +
                "Aplikasi EduWallet: Tabungan Siswa</body></html>";
        JLabel welcomeMessageLabel = new JLabel(welcomeMessage);
        welcomeMessageLabel.setBounds(10, 20, getWidth() - 150, 40); // Adjust width to avoid collision with logout button
        welcomeMessageLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        welcomeMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeMessageLabel.setForeground(CommonConstants.TEXT_COLOR);
        add(welcomeMessageLabel);

        // create a custom panel for balance and buttons with gradient background and rounded corners
        JPanel balancePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                Color color1 = new Color(0x00BCD4);
                Color color2 = new Color(0x673AB7);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, width, height, 10, 10); // Rounded corners
            }
        };
        balancePanel.setBounds(6, 100, getWidth() - 30, 200);
        balancePanel.setLayout(null);
        balancePanel.setOpaque(false); // Make the panel transparent to show the gradient
        // create logo label
        ImageIcon logoIcon = new ImageIcon(new ImageIcon("src/assets/logo/logo2.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(balancePanel.getWidth() - 110, 10, 80, 80); // Adjust the position and size as needed
        balancePanel.add(logoLabel);
        // create current balance label
        JLabel currentBalanceLabel = new JLabel("Saldo Tabungan");
        currentBalanceLabel.setBounds(20, 20, 200, 30);
        currentBalanceLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
        currentBalanceLabel.setForeground(Color.WHITE);
        balancePanel.add(currentBalanceLabel);

        // create current balance field
        currentBalanceField = new JTextField("Rp. " + String.format("%,.2f", user.getCurrentBalance()));
        currentBalanceField.setBounds(20, 40, 200, 40);
        currentBalanceField.setFont(new Font("Dialog", Font.BOLD, 28));
        currentBalanceField.setHorizontalAlignment(SwingConstants.LEFT);
        currentBalanceField.setEditable(false);
        currentBalanceField.setBorder(null);
        currentBalanceField.setBackground(new Color(0, 0, 0, 0)); // Make the background transparent
        currentBalanceField.setForeground(Color.WHITE);
        balancePanel.add(currentBalanceField);

        // create buttons with icons
        String[] buttonLabels = {"Deposit Dana", "Transfer Dana", "Tarik Dana"};
        String[] iconPaths = {"src/assets/icon/top-up.png", "src/assets/icon/transfer.png", "src/assets/icon/withdraw.png"};
        int buttonWidth = 100;
        int buttonHeight = 40;
        int buttonY = 120;
        int buttonSpacing = 20;
        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.setBounds(25 + (i * (buttonWidth + buttonSpacing)), buttonY, buttonWidth, buttonHeight);
            button.setFont(new Font("Dialog", Font.BOLD, 14));
            button.setForeground(new Color(0x00BCD4));
            button.setIcon(new ImageIcon(new ImageIcon(iconPaths[i]).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.setBorder(null);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.setOpaque(false);
            button.setMargin(new Insets(0, 0, 0, 0));
            button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
                @Override
                public void paint(Graphics g, JComponent c) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int width = c.getWidth();
                    g2d.setColor(CommonConstants.PRIMARY_COLOR);
                    g2d.fillOval((width - 30) / 2, 5, 30, 30); // Draw circular container for icon
                    g2d.drawImage(((ImageIcon) button.getIcon()).getImage(), (width - 20) / 2, 10, 20, 20, null); // Center icon in the circle
                    g2d.dispose();
                }
            });
            button.addActionListener(this);
            balancePanel.add(button);

            // Add label below the button
            JLabel buttonLabel = new JLabel(buttonLabels[i]);
            buttonLabel.setBounds(25 + (i * (buttonWidth + buttonSpacing)), buttonY + buttonHeight + 5, buttonWidth, 20);
            buttonLabel.setFont(new Font("Dialog", Font.BOLD, 14));
            buttonLabel.setForeground(Color.WHITE);
            buttonLabel.setHorizontalAlignment(SwingConstants.CENTER);
            balancePanel.add(buttonLabel);
        }

        add(balancePanel);
       
        JButton pastTransactionButton = new JButton("History Transaksi");
        pastTransactionButton.setBounds(15, 320, getWidth() - 50, 50);
        pastTransactionButton.setFont(new Font("Dialog", Font.BOLD, 22));
        pastTransactionButton.addActionListener(this);
        pastTransactionButton.setBackground(CommonConstants.BUTTON_COLOR);
        add(pastTransactionButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed = e.getActionCommand();

        // user pressed logout
        if(buttonPressed.equalsIgnoreCase("Logout")){
            // return user to the login gui
            new LoginGui().setVisible(true);

            // dispose of this gui
            this.dispose();

            // don't bother running the rest of the code
            return;
        }

        // other functions
        BankingAppDialog bankingAppDialog = new BankingAppDialog(this, user);

        // set the title of the dialog header to the action
        bankingAppDialog.setTitle(buttonPressed);

        // if the button pressed is deposit, Tarik Dana, or Transfer Dana
        if(buttonPressed.equalsIgnoreCase("Deposit Dana") || buttonPressed.equalsIgnoreCase("Tarik Dana")
                    || buttonPressed.equalsIgnoreCase("Transfer Dana")){
            // add in the current balance and amount gui components to the dialog
            bankingAppDialog.addCurrentBalanceAndAmount();

            // add action button
            bankingAppDialog.addActionButton(buttonPressed);

            // for the Transfer Dana action it will require more components
            if(buttonPressed.equalsIgnoreCase("Transfer Dana")){
                bankingAppDialog.addUserField();
            }

        }else if(buttonPressed.equalsIgnoreCase("History Transaksi")){
            bankingAppDialog.addPastTransactionComponents();
        }

        // make the app dialog visible
        bankingAppDialog.setVisible(true);
    }
}