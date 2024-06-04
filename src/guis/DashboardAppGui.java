package guis;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import db_objs.MyJDBC;
import db_objs.User;
import db_objs.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;

public class DashboardAppGui extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private MyJDBC db;

    public DashboardAppGui() {
        setTitle("Dashboard Admin");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        db = new MyJDBC();

        // Panel Navigasi
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(33, 33, 33));
        navPanel.setPreferredSize(new Dimension(250, getHeight()));

        JLabel navTitle = new JLabel("Admin Panel");
        navTitle.setFont(new Font("Arial", Font.BOLD, 24));
        navTitle.setForeground(Color.WHITE);
        navTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        navTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        navPanel.add(navTitle);

        // Tombol Operasi CRUD
        JButton createButton = new JButton("Menambah Data");
        JButton readButton = new JButton("Melihat Transaksi");
        JButton updateButton = new JButton("Mengubah Data");
        JButton deleteButton = new JButton("Menghapus Data");
        JButton refreshButton = new JButton("Refresh");

        Dimension buttonSize = new Dimension(200, 40);
        createButton.setMaximumSize(buttonSize);
        readButton.setMaximumSize(buttonSize);
        updateButton.setMaximumSize(buttonSize);
        deleteButton.setMaximumSize(buttonSize);
        refreshButton.setMaximumSize(buttonSize);

        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        readButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        refreshButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        navPanel.add(createButton);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(readButton);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(updateButton);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(deleteButton);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(refreshButton);

        add(navPanel, BorderLayout.WEST);

        // Panel Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(33, 150, 243));
        JLabel headerLabel = new JLabel(" Dashboard Admin", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Model Tabel
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("ID PENGGUNA");
        model.addColumn("NAMA PENGGUNA");
        model.addColumn("SALDO TERKINI");

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(33, 150, 243));
        table.getTableHeader().setForeground(Color.WHITE);

        // Alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row % 2 == 0) {
                    c.setBackground(new Color(240, 240, 240));
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Action Listeners untuk Tombol CRUD
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CreateUserDialog createUserDialog = new CreateUserDialog(null, "Create User", true);
                createUserDialog.setLocationRelativeTo(null);
                createUserDialog.setVisible(true);
            }
        });

        readButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int userId = (int) model.getValueAt(selectedRow, 0);
                    TransactionDialog transactionDialog = new TransactionDialog(null, "View Transactions", true, userId);
                    transactionDialog.setLocationRelativeTo(null);
                    transactionDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Pilih baris untuk melihat transaksi");
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int userId = (int) model.getValueAt(selectedRow, 0);
                    UpdateDialog updateDialog = new UpdateDialog(null, "Update User", true, userId);
                    updateDialog.setLocationRelativeTo(null);
                    updateDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Pilih baris untuk diupdate");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int userId = (int) model.getValueAt(selectedRow, 0);
                    int response = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        db.deleteUser(userId);
                        loadData(); // Muat ulang data
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Pilih baris untuk dihapus");
                }
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadData(); // Memuat ulang data dari database
            }
        });

        loadData();

        setVisible(true);
    }

    private void loadData() {
        ArrayList<User> users = db.getAllUsers();
        model.setRowCount(0); // Menghapus data yang ada
        for (User user : users) {
            model.addRow(new Object[]{user.getId(), user.getUsername(), user.getCurrentBalance()});
        }
    }

    class UpdateDialog extends JDialog {
        private JTextField usernameField;
        private JTextField balanceField;
        private JTextField passwordField;
        private JButton saveButton;

        public UpdateDialog(Frame owner, String title, boolean modal, int userId) {
            super(owner, title, modal);
            setSize(300, 200);
            setLayout(new GridLayout(4, 2));

            add(new JLabel("Username:"));
            usernameField = new JTextField();
            add(usernameField);

            add(new JLabel("Balance:"));
            balanceField = new JTextField();
            add(balanceField);

            add(new JLabel("Password:"));
            passwordField = new JTextField();
            add(passwordField);

            saveButton = new JButton("Save");
            saveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String username = usernameField.getText();
                    BigDecimal balance = new BigDecimal(balanceField.getText());
                    String password = passwordField.getText();
                    db.updateUser(userId, username, balance, password);
                    dispose();
                    loadData();
                }
            });
            add(saveButton);

            User user = db.getUserById(userId);
            if (user != null) {
                usernameField.setText(user.getUsername());
                balanceField.setText(user.getCurrentBalance().toString());
                passwordField.setText("");
            }
        }
    }

    class CreateUserDialog extends JDialog {
        private JTextField usernameField;
        private JTextField balanceField;
        private JTextField passwordField;
        private JButton saveButton;

        public CreateUserDialog(Frame owner, String title, boolean modal) {
            super(owner, title, modal);
            setSize(300, 200);
            setLayout(new GridLayout(4, 2));

            add(new JLabel("Username:"));
            usernameField = new JTextField();
            add(usernameField);

            add(new JLabel("Balance:"));
            balanceField = new JTextField();
            add(balanceField);

            add(new JLabel("Password:"));
            passwordField = new JTextField();
            add(passwordField);

            saveButton = new JButton("Save");
            saveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String username = usernameField.getText();
                    BigDecimal balance = new BigDecimal(balanceField.getText());
                    String password = passwordField.getText();
                    db.createUser(username, balance, password);
                    dispose();
                    loadData();
                }
            });
            add(saveButton);
        }
    }

    class TransactionDialog extends JDialog {
        private JTable transactionTable;
        private DefaultTableModel transactionModel;

        public TransactionDialog(Frame owner, String title, boolean modal, int userId) {
            super(owner, title, modal);
            setSize(400, 300);
            setLayout(new BorderLayout());

            transactionModel = new DefaultTableModel();
            transactionModel.addColumn("Transaction Amount");
            transactionModel.addColumn("Transaction Date");
            transactionModel.addColumn("Transaction Type");
            transactionModel.addColumn("User ID");

            transactionTable = new JTable(transactionModel);
            JScrollPane scrollPane = new JScrollPane(transactionTable);
            add(scrollPane, BorderLayout.CENTER);

            loadTransactionData(userId);
        }

        private void loadTransactionData(int userId) {
            ArrayList<Transaction> transactions = db.getTransactionsByUserId(userId);
            transactionModel.setRowCount(0); // Clear existing data
            for (Transaction transaction : transactions) {
                transactionModel.addRow(new Object[]{
                    transaction.getTransactionAmount(),
                    transaction.getTransactionDate(),
                    transaction.getTransactionType(),
                    transaction.getUserId()
                });
            }
        }
    }

    public static void main(String[] args) {
        new DashboardAppGui();
    }
}
