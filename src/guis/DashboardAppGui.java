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

// Kelas utama GUI untuk aplikasi Dashboard (Class dan Object)
public class DashboardAppGui extends JFrame {
    // Komponen Swing dan objek database (Swing component, JDBC, MySQL DB)
    private JTable table;
    private DefaultTableModel model;
    private MyJDBC db;
    private JLabel totalTransactionsLabel;
    private JLabel totalUsersLabel;
    private JLabel totalNominalLabel;

    // Konstruktor untuk menginisialisasi GUI (Constructor)
    public DashboardAppGui() {
        setTitle("Dashboard Admin");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        db = new MyJDBC(); // Koneksi database (JDBC, MySQL DB)

        // Panel Navigasi (Swing component)
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(33, 33, 33));
        navPanel.setPreferredSize(new Dimension(250, getHeight()));

        // Logo (Swing component)
        ImageIcon logoIcon = new ImageIcon("lib/logo1.png");
        Image logoImage = logoIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        navPanel.add(logoLabel);

        // Tombol CRUD (Swing component, CRUD)
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

        // Set border radius dan warna latar belakang (Swing component)
        createButton.setBackground(new Color(70, 130, 180));
        readButton.setBackground(new Color(70, 130, 180));
        updateButton.setBackground(new Color(70, 130, 180));
        deleteButton.setBackground(new Color(70, 130, 180));
        refreshButton.setBackground(new Color(70, 130, 180));

        createButton.setForeground(Color.WHITE);
        readButton.setForeground(Color.WHITE);
        updateButton.setForeground(Color.WHITE);
        deleteButton.setForeground(Color.WHITE);
        refreshButton.setForeground(Color.WHITE);

        createButton.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1, true));
        readButton.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1, true));
        updateButton.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1, true));
        deleteButton.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1, true));
        refreshButton.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1, true));

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

        // Panel Header (Swing component)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(33, 150, 243));
        JLabel headerLabel = new JLabel(" Dashboard Admin", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Panel Konten (Swing component)
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Model Tabel (Swing component)
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Hanya kolom checkbox yang dapat diedit
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class; // Kolom checkbox
                }
                return super.getColumnClass(columnIndex);
            }
        };
        model.addColumn("Select");
        model.addColumn("ID PENGGUNA");
        model.addColumn("NAMA PENGGUNA");
        model.addColumn("SALDO TERKINI");

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(33, 150, 243));
        table.getTableHeader().setForeground(Color.WHITE);

        // Warna baris bergantian (Swing component)
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row % 2 == 0) {
                    c.setBackground(new Color(240, 240, 240));
                } else {
                    c.setBackground(Color.WHITE);
                }
                if (isSelected) {
                    c.setBackground(new Color(184, 207, 229));
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(33, 150, 243), 2));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel Statistik (Swing component)
        JPanel statsPanel = new JPanel(new GridLayout(1, 3));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        totalTransactionsLabel = new JLabel("Total Transaksi: 0");
        totalUsersLabel = new JLabel("Jumlah Users: 0");
        totalNominalLabel = new JLabel("Total Nominal: 0");

        statsPanel.add(totalTransactionsLabel);
        statsPanel.add(totalUsersLabel);
        statsPanel.add(totalNominalLabel);

        contentPanel.add(statsPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);

        // Action Listeners untuk Tombol CRUD (Listener interface, Action event)
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CreateUserDialog createUserDialog = new CreateUserDialog(null, "Create User", true);
                createUserDialog.setLocationRelativeTo(null);
                createUserDialog.setVisible(true);
            }
        });

        readButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = getSelectedRow();
                if (selectedRow >= 0) {
                    int userId = (int) model.getValueAt(selectedRow, 1);
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
                int selectedRow = getSelectedRow();
                if (selectedRow >= 0) {
                    int userId = (int) model.getValueAt(selectedRow, 1);
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
                int selectedRow = getSelectedRow();
                if (selectedRow >= 0) {
                    int userId = (int) model.getValueAt(selectedRow, 1);
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
        loadStats();

        setVisible(true);
    }

    // Mendapatkan baris yang dipilih (Method)
    private int getSelectedRow() {
        for (int i = 0; i < model.getRowCount(); i++) {
            if ((Boolean) model.getValueAt(i, 0)) {
                return i;
            }
        }
        return -1;
    }

    // Memuat data dari database (Method, CRUD)
    private void loadData() {
        ArrayList<User> users = db.getAllUsers();
        model.setRowCount(0); // Menghapus data yang ada
        for (User user : users) {
            model.addRow(new Object[]{false, user.getId(), user.getUsername(), user.getCurrentBalance()});
        }
    }

    // Memuat statistik dari database (Method, CRUD)
    private void loadStats() {
        int totalTransactions = db.getTotalTransactions();
        int totalUsers = db.getTotalUsers();
        BigDecimal totalNominal = db.getTotalNominal();

        totalTransactionsLabel.setText("Total Transaksi: " + totalTransactions);
        totalUsersLabel.setText("Jumlah Users: " + totalUsers);
        totalNominalLabel.setText("Total Nominal: " + totalNominal);
    }

    // Kelas untuk dialog update user (Inner class, Swing component)
    class UpdateDialog extends JDialog {
        private JTextField usernameField;
        private JTextField balanceField;
        private JTextField passwordField;
        private JButton saveButton;

        // Konstruktor untuk dialog update user (Constructor)
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

    // Kelas untuk dialog create user (Inner class, Swing component)
    class CreateUserDialog extends JDialog {
        private JTextField usernameField;
        private JTextField balanceField;
        private JTextField passwordField;
        private JButton saveButton;

        // Konstruktor untuk dialog create user (Constructor)
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

    // Kelas untuk dialog transaksi (Inner class, Swing component)
    class TransactionDialog extends JDialog {
        private JTable transactionTable;
        private DefaultTableModel transactionModel;

        // Konstruktor untuk dialog transaksi (Constructor)
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

        // Memuat data transaksi (Method, CRUD)
        private void loadTransactionData(int userId) {
            ArrayList<Transaction> transactions = db.getTransactionsByUserId(userId);
            transactionModel.setRowCount(0); // Menghapus data yang ada
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

    // Metode utama untuk menjalankan aplikasi (Method)
    public static void main(String[] args) {
        new DashboardAppGui();
    }
}