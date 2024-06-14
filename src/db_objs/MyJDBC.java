package db_objs;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.math.BigInteger;

/*
    JDBC class digunakan untuk menghubungkan aplikasi ke database
 */
public class MyJDBC { // 1. Class

    // konfigurasi koneksi
    private static final String DB_URL = "jdbc:mysql://localhost:3306/wallet_app"; // 15. MySQL DB
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    // Operasi Validasi Login
    public static User validateLogin(String username, String password) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ? AND password = ?"
            );
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                BigDecimal currentBalance = resultSet.getBigDecimal("current_balance");
                int role = resultSet.getInt("role"); // Get role
                String phone = resultSet.getString("phone");
                return new User(userId, username, password, currentBalance, role, phone);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // registers new user to the database
    // true - register success
    // false - register fails
<<<<<<< HEAD
    public static boolean register(String username, String password, String phone) { // 1. Method
=======
    public static boolean register(String username, String password, BigInteger phoneNumber) { // 1. Method
>>>>>>> features/update
        try {
            // pengecekan apakah user sudah terdaftar
            if (!checkUser(username) && !checkPhoneNumber(phoneNumber)) {
                Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD); // 14. JDBC

                PreparedStatement preparedStatement = connection.prepareStatement(
<<<<<<< HEAD
                        "INSERT INTO users(username, password, phone, current_balance) " +
=======
                        "INSERT INTO users(username, password, current_balance, phone_number) " +
>>>>>>> features/update
                                "VALUES(?, ?, ?, ?)"
                );

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
<<<<<<< HEAD
                preparedStatement.setString(3, phone);
                preparedStatement.setBigDecimal(4, new BigDecimal(0));
=======
                preparedStatement.setBigDecimal(3, new BigDecimal(0));
                preparedStatement.setString(4, phoneNumber.toString());
>>>>>>> features/update

                preparedStatement.executeUpdate();
                return true;

            }
        } catch (SQLException e) { // 8. Exception handling
            e.printStackTrace();
        }

        return false;
    }

    private static boolean checkPhoneNumber(BigInteger phoneNumber) { // 1. Method
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD); // 14. JDBC

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE phone_number = ?"
            );

            preparedStatement.setString(1, phoneNumber.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            // ini akan mengembalikan false jika tidak ada data yang ditemukan
            if (!resultSet.next()) {
                return false;
            }
        } catch (SQLException e) { // 8. Exception handling
            e.printStackTrace();
        }

        return true;
    }

    // cek apakah user sudah terdaftar
    // true - user exists
    // false - user doesn't exist
    private static boolean checkUser(String username) { // 1. Method
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD); // 14. JDBC

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?"
            );

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            // ini akan mengembalikan false jika tidak ada data yang ditemukan
            if (!resultSet.next()) {
                return false;
            }
        } catch (SQLException e) { // 8. Exception handling
            e.printStackTrace();
        }

        return true;
    }

    // true - update to db was a success
    // false - update to the db was a fail
    public static boolean addTransactionToDatabase(Transaction transaction) { // 1. Method
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD); // 14. JDBC
            connection.setAutoCommit(false); // Start transaction

            // Check if user exists
            PreparedStatement checkUser = connection.prepareStatement("SELECT id FROM users WHERE id = ?");
            checkUser.setInt(1, transaction.getUserId());
            ResultSet userResult = checkUser.executeQuery();
            if (!userResult.next()) {
                return false; // User does not exist, rollback and exit
            }

            // Insert transaction
            PreparedStatement insertTransaction = connection.prepareStatement(
                    "INSERT INTO transactions(user_id, transaction_type, transaction_amount, transaction_date) " +
                            "VALUES(?, ?, ?, NOW())"
            );
            insertTransaction.setInt(1, transaction.getUserId());
            insertTransaction.setString(2, transaction.getTransactionType());
            insertTransaction.setBigDecimal(3, transaction.getTransactionAmount());
            insertTransaction.executeUpdate();
            connection.commit(); // Commit transaction
            return true;
        } catch (SQLException e) { // 8. Exception handling
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback on error
                } catch (SQLException ex) { // 8. Exception handling
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Reset auto-commit
                    connection.close();
                } catch (SQLException e) { // 8. Exception handling
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    // true - update balance successful
    // false - update balance fail
    public static boolean updateCurrentBalance(User user) { // 1. Method
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD); // 14. JDBC

            PreparedStatement updateBalance = connection.prepareStatement(
                    "UPDATE users SET current_balance = ? WHERE id = ?"
            );

            updateBalance.setBigDecimal(1, user.getCurrentBalance());
            updateBalance.setInt(2, user.getId());

            updateBalance.executeUpdate();
            return true;

        } catch (SQLException e) { // 8. Exception handling
            e.printStackTrace();
        }

        return false;
    }

    // true - transfer was a success
    // false - transfer was a fail
    public static boolean transfer(User user, String transferredPhone, float transferAmount) { // 1. Method
        if (user.getPhone().equals(transferredPhone)) {
            // Transaksi ke diri sendiri tidak diizinkan
            return false;
        }
    
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD); // 14. JDBC
    
            PreparedStatement queryUser = connection.prepareStatement(
                    "SELECT * FROM users WHERE phone = ?"
            );
    
            queryUser.setString(1, transferredPhone);
            ResultSet resultSet = queryUser.executeQuery();
    
            if (resultSet.next()) {
                // perform transfer
                User transferredUser = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getBigDecimal("current_balance"),
                        resultSet.getInt("role"),
                        resultSet.getString("phone") // Add phone field
                    );
    
                    // create transaction
                    Transaction transferTransaction = new Transaction(
                            user.getId(),
                            "Transfer",
                            new BigDecimal(-transferAmount),
                            null
                    );
    
                    // this transaction will belong to the transferred user
                    Transaction receivedTransaction = new Transaction(
                            transferredUser.getId(),
                            "Transfer",
                            new BigDecimal(transferAmount),
                            null
                    );
    
                    // update transfer user
                    transferredUser.setCurrentBalance(transferredUser.getCurrentBalance().add(BigDecimal.valueOf(transferAmount)));
                    updateCurrentBalance(transferredUser);
    
                    // update user current balance
                    user.setCurrentBalance(user.getCurrentBalance().subtract(BigDecimal.valueOf(transferAmount)));
                    updateCurrentBalance(user);
    
                    // add these transactions to the database
                    addTransactionToDatabase(transferTransaction);
                    addTransactionToDatabase(receivedTransaction);
    
                    return true;
                }
            } catch (SQLException e) { // 8. Exception handling
                e.printStackTrace();
            }
    
            return false;
        }
    
        // get all transactions (used for past transaction)
        public static ArrayList<Transaction> getPastTransaction(User user) { // 1. Method
            ArrayList<Transaction> pastTransactions = new ArrayList<>();
            try {
                Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD); // 14. JDBC
    
                PreparedStatement selectAllTransaction = connection.prepareStatement(
                        "SELECT * FROM transactions WHERE user_id = ?"
                );
                selectAllTransaction.setInt(1, user.getId());
    
                ResultSet resultSet = selectAllTransaction.executeQuery();
    
                // iterate throught the results (if any)
                while (resultSet.next()) {
                    // create transaction obj
                    Transaction transaction = new Transaction(
                            user.getId(),
                            resultSet.getString("transaction_type"),
                            resultSet.getBigDecimal("transaction_amount"),
                            resultSet.getDate("transaction_date")
                    );
    
                    // store into array list
                    pastTransactions.add(transaction);
                }
            } catch (SQLException e) { // 8. Exception handling
                e.printStackTrace();
            }
    
            return pastTransactions;
        }
    
        public static boolean createUser(String username, String password, BigDecimal currentBalance) { // 1. Method
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) { // 14. JDBC
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO users(username, password, current_balance) VALUES(?, ?, ?)"
                );
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setBigDecimal(3, currentBalance);
                int result = preparedStatement.executeUpdate();
                return result > 0;
            } catch (SQLException e) { // 8. Exception handling
                e.printStackTrace();
                return false;
            }
        }
    
        // Mengambil data user berdasarkan username
        public static User getUser(String username) { // 1. Method
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) { // 14. JDBC
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM users WHERE username = ?"
                );
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getBigDecimal("current_balance"),
                        resultSet.getInt("role"),
                        resultSet.getString("phone") // Add phone field
                    );
                }
            } catch (SQLException e) { // 8. Exception handling
                e.printStackTrace();
            }
            return null;
        }
    
        // Memperbarui data user
        public static boolean updateUser(int userId, String username, BigDecimal currentBalance, String password) { // 1. Method
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) { // 14. JDBC
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE users SET username = ?, password = ?, current_balance = ? WHERE id = ?"
                );
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setBigDecimal(3, currentBalance);
                preparedStatement.setInt(4, userId);
                int result = preparedStatement.executeUpdate();
                return result > 0;
            } catch (SQLException e) { // 8. Exception handling
                e.printStackTrace();
                return false;
            }
        }
    
        // Menghapus user dari database
        public static boolean deleteUser(int userId) { // 1. Method
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) { // 14. JDBC
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM users WHERE id = ?"
                );
                preparedStatement.setInt(1, userId);
                int result = preparedStatement.executeUpdate();
                return result > 0;
            } catch (SQLException e) { // 8. Exception handling
                e.printStackTrace();
                return false;
            }
        }
    
        // Mendapatkan semua user dari database
        public static ArrayList<User> getAllUsers() { // 1. Method
            ArrayList<User> users = new ArrayList<>();
            try (Connection connection = DriverManager.getConnection(MyJDBC.DB_URL, MyJDBC.DB_USERNAME, MyJDBC.DB_PASSWORD)) { // 14. JDBC
    
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getBigDecimal("current_balance"),
                        resultSet.getInt("role"),
                        resultSet.getString("phone") // Add phone field
                    );
                    users.add(user);
                }
            } catch (SQLException e) { // 8. Exception handling
                e.printStackTrace();
            }
            return users;
        }
    
        public static ArrayList<Transaction> getPastTransaction(int userId) { // 1. Method
            ArrayList<Transaction> transactions = new ArrayList<>();
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) { // 14. JDBC
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM transactions WHERE user_id = ?"
                );
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Transaction transaction = new Transaction(
                            resultSet.getInt("user_id"),
                            resultSet.getString("transaction_type"),
                            resultSet.getBigDecimal("transaction_amount"),
                            resultSet.getDate("transaction_date")
                    );
                    transactions.add(transaction);
                }
            } catch (SQLException e) { // 8. Exception handling
                e.printStackTrace();
            }
            return         transactions;
        }
    
        public User getUserById(int userId) { // 1. Method
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) { // 14. JDBC
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM users WHERE id = ?"
                );
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getBigDecimal("current_balance"),
                        resultSet.getInt("role"),
                        resultSet.getString("phone") // Add phone field
                    );
                }
            } catch (SQLException e) { // 8. Exception handling
                e.printStackTrace();
            }
            return null;
        }
    
        public void createUser(String username, BigDecimal balance, String password) { // 1. Method
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) { // 14. JDBC
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO users(username, current_balance, password) VALUES(?, ?, ?)"
                );
                preparedStatement.setString(1, username);
                preparedStatement.setBigDecimal(2, balance);
                preparedStatement.setString(3, password);
                preparedStatement.executeUpdate();
            } catch (SQLException e) { // 8. Exception handling
                e.printStackTrace();
            }
        }
    
        public ArrayList<Transaction> getTransactionsByUserId(int userId) { // 1. Method
            ArrayList<Transaction> transactions = new ArrayList<>();
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) { // 14. JDBC
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM transactions WHERE user_id = ?"
                );
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Transaction transaction = new Transaction(
                            resultSet.getInt("user_id"),
                            resultSet.getString("transaction_type"),
                            resultSet.getBigDecimal("transaction_amount"),
                            resultSet.getDate("transaction_date")
                    );
                    transactions.add(transaction);
                }
            } catch (SQLException e) { // 8. Exception handling
                e.printStackTrace();
            }
            return transactions;
        }
    
        public int getTotalTransactions() { // 1. Method
            int totalTransactions = 0;
            String query = "SELECT COUNT(*) FROM transactions";
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD); // 14. JDBC
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    totalTransactions = rs.getInt(1);
                }
            } catch (SQLException e) { // 8. Exception handling
                e.printStackTrace();
            }
            return totalTransactions;
        }
    
        public int getTotalUsers() { // 1. Method
            int totalUsers = 0;
            String query = "SELECT COUNT(*) FROM users";
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD); // 14. JDBC
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    totalUsers = rs.getInt(1);
                }
            } catch (SQLException e) { // 8. Exception handling
                e.printStackTrace();
            }
            return totalUsers;
        }
    
        public BigDecimal getTotalNominal() { // 1. Method
            BigDecimal totalNominal = BigDecimal.ZERO;
            String query = "SELECT SUM(current_balance) FROM users";
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD); // 14. JDBC
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    totalNominal = rs.getBigDecimal(1);
                }
            } catch (SQLException e) { // 8. Exception handling
                e.printStackTrace();
            }
            return totalNominal;
        }
    }