package db_objs;

import java.math.BigDecimal;
import java.sql.Date;

/*
    Transaction object digunakan untuk menyimpan data transaksi yang dilakukan oleh user
 */
public class Transaction {
    // 1. Kelas dan Objek: Ini adalah definisi dari kelas Transaction.
    // 2. Enkapsulasi: Field-field bersifat private dan diakses melalui metode-metode publik.
    private final int userId;
    private final String transactionType;
    private final BigDecimal transactionAmount;
    private final Date transactionDate;

    // 1. Konstruktor: Menginisialisasi objek Transaction dengan nilai-nilai yang diberikan.
    public Transaction(int userId, String transactionType, BigDecimal transactionAmount, Date transactionDate){
        this.userId = userId;
        this.transactionType = transactionType;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
    }

    // 3. Getters: Metode untuk mengakses field-field private.
    public int getUserId() {
        return userId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }
}