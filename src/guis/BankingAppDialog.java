package guis;
import constants.CommonConstants;
import db_objs.MyJDBC;
import db_objs.Transaction;
import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;

/*
    Display untuk menampilkan dialog ketika user melakukan deposit, withdraw, atau transfer
 */
public class BankingAppDialog extends JDialog implements ActionListener {
    // 1. Class and Object: This is a class definition for BankingAppDialog which extends JDialog and implements ActionListener.
    private User user;
    private BankingAppGui bankingAppGui;
    private JLabel balanceLabel, enterAmountLabel, enterUserLabel;
    private JTextField enterAmountField, enterUserField;
    private JButton actionButton;
    private JPanel pastTransactionPanel;
    private ArrayList<Transaction> pastTransactions;

    // 1. Constructor: Initializes the dialog with references to the main GUI and user.
    public BankingAppDialog(BankingAppGui bankingAppGui, User user){
        // set the size
        setSize(400, 400);

        // add focus to the dialog (can't interact with anything else until dialog is closed)
        setModal(true);

        // loads in the center of our banking gui
        setLocationRelativeTo(bankingAppGui);

        // when user closes dialog, it releases its resources that are being used
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // prevents dialog from being resized
        setResizable(false);

        // allows us to manually specify the size and position of each component
        setLayout(null);

        // we will need reference to our gui so that we can update the current balance
        this.bankingAppGui = bankingAppGui;

        // we will need access to the user info to make updates to our db or retrieve data about the user
        this.user = user;
    }

    // 13. Swing component: Adds labels and text fields to the dialog.
    public void addCurrentBalanceAndAmount(){
        // balance label
        balanceLabel = new JLabel("Saldo: RP." + user.getCurrentBalance());
        balanceLabel.setBounds(0, 10, getWidth() - 20, 20);
        balanceLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(balanceLabel);

        // enter amount label
        enterAmountLabel = new JLabel("Masukan Nominal:");
        enterAmountLabel.setBounds(0, 50, getWidth() - 20, 20);
        enterAmountLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        enterAmountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterAmountLabel);

        // enter amount field
        enterAmountField = new JTextField();
        enterAmountField.setBounds(15, 80, getWidth() - 50, 40);
        enterAmountField.setFont(new Font("Dialog", Font.BOLD, 20));
        enterAmountField.setHorizontalAlignment(SwingConstants.RIGHT);
        add(enterAmountField);
    }

    // 13. Swing component: Adds an action button to the dialog.
    public void addActionButton(String actionButtonType){
        actionButton = new JButton(actionButtonType);
        actionButton.setBounds(15, 300, getWidth() - 50, 40);
        actionButton.setFont(new Font("Dialog", Font.BOLD, 20));
        actionButton.addActionListener(this); // 10. Listener interface: Registers the dialog as an action listener.
        add(actionButton);
    }

    // 13. Swing component: Adds a user input field to the dialog.
    public void addUserField(){
        // enter user label
        enterUserLabel = new JLabel("Masukan Nomor Telepon:");
        enterUserLabel.setBounds(0, 160, getWidth() - 20, 20);
        enterUserLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        enterUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterUserLabel);

        // enter user field
        enterUserField = new JTextField();
        enterUserField.setBounds(15, 190, getWidth() - 50, 40);
        enterUserField.setFont(new Font("Dialog", Font.BOLD, 20));
        add(enterUserField);
    }

    // 13. Swing component: Adds past transaction components to the dialog.
    public void addPastTransactionComponents(){
        // container where we will store each transaction
        pastTransactionPanel = new JPanel();

        // make layout 1x1
        pastTransactionPanel.setLayout(new BoxLayout(pastTransactionPanel, BoxLayout.Y_AXIS));

        // add scrollability to the container
        JScrollPane scrollPane = new JScrollPane(pastTransactionPanel);

        // displays the vertical scroll only when it is required
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(0, 20, getWidth() - 15, getHeight() - 80);

        // 14. JDBC: Perform db call to retrieve all of the past transactions and store into array list
        pastTransactions = MyJDBC.getPastTransaction(user);

        // iterate through the list and add to the gui
        for(int i = 0; i < pastTransactions.size(); i++){
            // store current transaction
            Transaction pastTransaction = pastTransactions.get(i);

            // create a container to store an individual transaction
            JPanel pastTransactionContainer = new JPanel();
            pastTransactionContainer.setLayout(new BorderLayout());

            // create a sub-container to hold transaction type, date, and amount labels
            JPanel typeDateAndAmountContainer = new JPanel();
            typeDateAndAmountContainer.setLayout(new BorderLayout());

            // create transaction type label
            JLabel transactionTypeLabel = new JLabel(pastTransaction.getTransactionType());
            transactionTypeLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            // create transaction date label
            JLabel transactionDateLabel = new JLabel(String.valueOf(pastTransaction.getTransactionDate()));
            transactionDateLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            // create a panel to hold type and date labels vertically
            JPanel typeAndDatePanel = new JPanel();
            typeAndDatePanel.setLayout(new BoxLayout(typeAndDatePanel, BoxLayout.Y_AXIS));
            typeAndDatePanel.add(transactionTypeLabel);
            typeAndDatePanel.add(transactionDateLabel);

            // create transaction amount label
            JLabel transactionAmountLabel = new JLabel(String.valueOf(pastTransaction.getTransactionAmount()));
            transactionAmountLabel.setFont(new Font("Dialog", Font.BOLD, 20));
            transactionAmountLabel.setText("Rp." + String.format("%,.2f", pastTransaction.getTransactionAmount()).replace(',', '.'));

            // create a panel to hold all components horizontally
            JPanel fullWidthPanel = new JPanel();
            fullWidthPanel.setLayout(new BoxLayout(fullWidthPanel, BoxLayout.X_AXIS));
            fullWidthPanel.add(typeAndDatePanel);
            fullWidthPanel.add(Box.createHorizontalGlue()); // to push the amount label to the right
            fullWidthPanel.add(transactionAmountLabel);

            // add the full width panel to the past transaction container
            pastTransactionContainer.setLayout(new BorderLayout());
            pastTransactionContainer.add(fullWidthPanel, BorderLayout.CENTER);
            // give a white background to each container
            pastTransactionContainer.setBackground(Color.WHITE);

            // give a black border to each transaction container
            pastTransactionContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            // add transaction component to the transaction panel
            pastTransactionPanel.add(pastTransactionContainer);
        }

        // add to the dialog
        add(scrollPane);
    }

    // 16. CRUD: Handles deposit and withdraw transactions.
    private void handleTransaction(String transactionType, float amountVal){
        Transaction transaction;

        if(transactionType.equalsIgnoreCase("Deposit Dana")){
            // deposit transaction type
            // add to current balance
            user.setCurrentBalance(user.getCurrentBalance().add(new BigDecimal(amountVal)));

            // create transaction
            // we leave date null because we are going to be using the NOW() in sql which will get the current date
            transaction = new Transaction(user.getId(), transactionType, new BigDecimal(amountVal), null);
        }else{
            // withdraw transaction type
            // subtract from current balance
            user.setCurrentBalance(user.getCurrentBalance().subtract(new BigDecimal(amountVal)));

            // we want to show a negative sign for the amount val when withdrawing
            transaction = new Transaction(user.getId(), transactionType, new BigDecimal(-amountVal), null);
        }

        // update database
        if(MyJDBC.addTransactionToDatabase(transaction) && MyJDBC.updateCurrentBalance(user)){
            // show success dialog
            JOptionPane.showMessageDialog(this, transactionType + " Berhasil!");

            // reset the fields
            resetFieldsAndUpdateCurrentBalance();
        }else{
            // show failure dialog
            JOptionPane.showMessageDialog(this, transactionType + " Gagal...");
        }

    }

    // 2. Encapsulation: Method to reset fields and update current balance.
    private void resetFieldsAndUpdateCurrentBalance(){
        // reset fields
        enterAmountField.setText("");

        // only appears when transfer is clicked
        if(enterUserField != null){
            enterUserField.setText("");
        }

        // update current balance on dialog
        balanceLabel.setText("Saldo: Rp." + user.getCurrentBalance());

        // update current balance on main gui
        bankingAppGui.getCurrentBalanceField().setText("Rp." + user.getCurrentBalance());
    }

    // 16. CRUD: Handles transfer transactions.
    private void handleTransfer(User user, String transferredPhone, float amount){
        if (MyJDBC.transfer(user, transferredPhone, amount)) {
            JOptionPane.showMessageDialog(this, "Transfer berhasil!");
            resetFieldsAndUpdateCurrentBalance();
        } else {
            JOptionPane.showMessageDialog(this, "Transfer gagal...");
        }
    }

    // 11. Action event: Handles button press events.
    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed = e.getActionCommand();

        // get amount val
        float amountVal = Float.parseFloat(enterAmountField.getText());

        // pressed deposit
        if(buttonPressed.equalsIgnoreCase("Deposit Dana")){
            // we want to handle the deposit transaction
            handleTransaction(buttonPressed, amountVal);
        }else{
            // pressed withdraw or transfer

            // validate input by making sure that withdraw or transfer amount is less than current balance
            // if result is -1 it means that the entered amount is more, 0 means they are equal, and 1 means that
            // the entered amount is less
            int result = user.getCurrentBalance().compareTo(BigDecimal.valueOf(amountVal));
            if(result < 0){
                // display error dialog
                JOptionPane.showMessageDialog(this, "Error: input nominal lebih besar dari saldo!");
                return;
            }

            // check to see if withdraw or transfer was pressed
            if(buttonPressed.equalsIgnoreCase("Tarik Dana")){
                handleTransaction(buttonPressed, amountVal);
            }else{
                // transfer
                String transferredPhone = enterUserField.getText();

                // handle transfer
                handleTransfer(user, transferredPhone, amountVal);
            }

        }
    }
}