package db_objs;

import java.math.BigDecimal;
import java.math.RoundingMode;

/*
    Objek User digunakan untuk menyimpan data user yang terdaftar di aplikasi
 */
public class User {
    // Enkapsulasi: field-field private
    private final int id;
    private final String username, password;
    private BigDecimal currentBalance;

    // Konstruktor: menginisialisasi objek User
    public User(int id, String username, String password, BigDecimal currentBalance){
        this.id = id;
        this.username = username;
        this.password = password;
        this.currentBalance = currentBalance;
    }

    // Getter: menyediakan akses baca ke field-field private
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    // Setter: menyediakan akses tulis ke currentBalance dengan pembulatan
    public void setCurrentBalance(BigDecimal newBalance){
        // menyimpan nilai baru hingga 2 tempat desimal
        currentBalance = newBalance.setScale(2, RoundingMode.FLOOR);
    }
}