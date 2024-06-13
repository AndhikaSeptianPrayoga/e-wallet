package db_objs;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

/*
    JDBC class digunakan untuk menghubungkan aplikasi ke database
 */
public class MyJDBC {
    // konfigurasi koneksi

    private static final String DB_URL = "jdbc:mysql://localhost:3306/wallet_app";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";


    // Operasi Validasi Login
    public static User validateLogin(String username, String password) {
        try {
            // membuat koneksi ke database
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            // create sql query
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ? AND password = ?"
            );

            // set parameter
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            // execute query
            ResultSet resultSet = preparedStatement.executeQuery();

            // next() returns true or false
            // true - query kemungkinan mengembalikan data dan result set tidak null
            // false -query tidak mengembalikan data dan result set null
            if (resultSet.next()) {
                // success
                // get id
                int userId = resultSet.getInt("id");

                // get nominal balance
                BigDecimal currentBalance = resultSet.getBigDecimal("current_balance");

                // return user object
                return new User(userId, username, password, currentBalance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // not valid user
        return null;
    }

    // registers new user to the database
    // true - register success
    // false - register fails
    public static boolean register(String username, String password) {
        try {
            // pengecekan apakah user sudah terdaftar
            if (!checkUser(username)) {
                Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO users(username, password, current_balance) " +
                                "VALUES(?, ?, ?)"
                );

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setBigDecimal(3, new BigDecimal(0));

                preparedStatement.executeUpdate();
                return true;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // cek apakah user sudah terdaftar
    // true - user exists
    // false - user doesn't exist
    private static boolean checkUser(String username) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?"
            );

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            // ini akan mengembalikan false jika tidak ada data yang ditemukan
            if (!resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    // true - update to db was a success
    // false - update to the db was a fail
    public static boolean addTransactionToDatabase(Transaction transaction) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
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
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback on error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Reset auto-commit
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    // true - update balance successful
    // false - update balance fail
    public static boolean updateCurrentBalance(User user) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement updateBalance = connection.prepareStatement(
                    "UPDATE users SET current_balance = ? WHERE id = ?"
            );

            updateBalance.setBigDecimal(1, user.getCurrentBalance());
            updateBalance.setInt(2, user.getId());

            updateBalance.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // true - transfer was a success
    // false - transfer was a fail
    public static boolean transfer(User user, String transferredUsername, float transferAmount) {
        if (user.getUsername().equals(transferredUsername)) {
            // Transaksi ke diri sendiri tidak diizinkan
            return false;
        }

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement queryUser = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?"
            );

            queryUser.setString(1, transferredUsername);
            ResultSet resultSet = queryUser.executeQuery();

            if (resultSet.next()) {
                // perform transfer
                User transferredUser = new User(
                        resultSet.getInt("id"),
                        transferredUsername,
                        resultSet.getString("password"),
                        resultSet.getBigDecimal("current_balance")
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // get all transactions (used for past transaction)
    public static ArrayList<Transaction> getPastTransaction(User user) {
        ArrayList<Transaction> pastTransactions = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pastTransactions;
    }


    public static boolean createUser(String username, String password, BigDecimal currentBalance) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO users(username, password, current_balance) VALUES(?, ?, ?)"
            );
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setBigDecimal(3, currentBalance);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Mengambil data user berdasarkan username
    public static User getUser(String username) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
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
                        resultSet.getBigDecimal("current_balance")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Memperbarui data user
    public static boolean updateUser(int userId, String username, BigDecimal currentBalance, String password) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE users SET username = ?, password = ?, current_balance = ? WHERE id = ?"
            );
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setBigDecimal(3, currentBalance);
            preparedStatement.setInt(4, userId);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Menghapus user dari database
    public static boolean deleteUser(int userId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM users WHERE id = ?"
            );
            preparedStatement.setInt(1, userId);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Mendapatkan semua user dari database
    public static ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(MyJDBC.DB_URL, MyJDBC.DB_USERNAME, MyJDBC.DB_PASSWORD)) {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getBigDecimal("current_balance")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static ArrayList<Transaction> getPastTransaction(int userId) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public User getUserById(int userId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
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
                        resultSet.getBigDecimal("current_balance")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createUser(String username, BigDecimal balance, String password) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO users(username, current_balance, password) VALUES(?, ?, ?)"
            );
            preparedStatement.setString(1, username);
            preparedStatement.setBigDecimal(2, balance);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Transaction> getTransactionsByUserId(int userId) {
ArrayList<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
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
        } catch (SQLException e) {
            e.printStackTrace();
    }
        return transactions;
    }
}














