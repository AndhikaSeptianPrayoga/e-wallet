package guis;

import constants.CommonConstants;
import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
    Performs banking functions such as depositing, withdrawing, seeing past transaction, and transferring
    This extends from the BaseFrame which means we will need to define our own addGuiComponent
 */
public class BankingAppGui extends BaseFrame implements ActionListener {
    private JTextField currentBalanceField;
    public JTextField getCurrentBalanceField(){return currentBalanceField;}

    public BankingAppGui(User user){
        super("Wallet Mahasiswa", user);
    }
    @Override
    protected void addGuiComponents() {
        // create welcome message
        String welcomeMessage = "<html>" +
                "<body style='text-align:center'>" +
                "<b>Anda Login Sebagai " + user.getUsername() + "</b><br>" +
                "Aplikasi E-wallet Mahasiswa</body></html>";
        JLabel welcomeMessageLabel = new JLabel(welcomeMessage);
        welcomeMessageLabel.setBounds(0, 20, getWidth() - 10, 40);
        welcomeMessageLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        welcomeMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeMessageLabel.setForeground(CommonConstants.TEXT_COLOR);
        add(welcomeMessageLabel);

        // create current balance label
        JLabel currentBalanceLabel = new JLabel("Saldo Anda Saat Ini");
        currentBalanceLabel.setBounds(0, 80, getWidth() - 10, 30);
        currentBalanceLabel.setFont(new Font("Dialog", Font.BOLD, 22));
        currentBalanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        currentBalanceLabel.setForeground(CommonConstants.TEXT_COLOR);

        add(currentBalanceLabel);
//
        // create current balance field
        currentBalanceField = new JTextField("RP." + user.getCurrentBalance());
        currentBalanceField.setBounds(15, 120, getWidth() - 50, 40);
        currentBalanceField.setFont(new Font("Dialog", Font.BOLD, 28));
        currentBalanceField.setHorizontalAlignment(SwingConstants.RIGHT);
        currentBalanceField.setEditable(false);
        add(currentBalanceField);

        // deposit button
        JButton depositButton = new JButton("Deposit Dana");
        depositButton.setBounds(15, 180, getWidth() - 50, 50);
        depositButton.setFont(new Font("Dialog", Font.BOLD, 22));
        depositButton.addActionListener(this);
        add(depositButton);

        // withdraw button
        JButton withdrawButton = new JButton("Penarikan Dana");
        withdrawButton.setBounds(15, 250, getWidth() - 50, 50);
        withdrawButton.setFont(new Font("Dialog", Font.BOLD, 22));
        withdrawButton.addActionListener(this);
        add(withdrawButton);

        // past transaction button
        JButton pastTransactionButton = new JButton("History Transaksi");
        pastTransactionButton.setBounds(15, 320, getWidth() - 50, 50);
        pastTransactionButton.setFont(new Font("Dialog", Font.BOLD, 22));
        pastTransactionButton.addActionListener(this);
        add(pastTransactionButton);

        // transfer button
        JButton transferButton = new JButton("Transfer Dana");
        transferButton.setBounds(15, 390, getWidth() - 50, 50);
        transferButton.setFont(new Font("Dialog", Font.BOLD, 22));
        transferButton.addActionListener(this);
        add(transferButton);

        // logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(15, 500, getWidth() - 50, 50);
        logoutButton.setFont(new Font("Dialog", Font.BOLD, 22));
        logoutButton.addActionListener(this);
        add(logoutButton);
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

        // if the button pressed is deposit, withdraw, or transfer
        if(buttonPressed.equalsIgnoreCase("Deposit Dana") || buttonPressed.equalsIgnoreCase("Penarikan Dana")
                    || buttonPressed.equalsIgnoreCase("Transfer Dana")){
            // add in the current balance and amount gui components to the dialog
            bankingAppDialog.addCurrentBalanceAndAmount();

            // add action button
            bankingAppDialog.addActionButton(buttonPressed);

            // for the transfer action it will require more components
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












