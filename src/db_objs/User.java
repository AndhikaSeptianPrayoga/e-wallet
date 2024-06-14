package db_objs;

import java.math.BigDecimal;
import java.math.RoundingMode;

/*
    Objek User digunakan untuk menyimpan data user yang terdaftar di aplikasi
 */
public class User {
    // Enkapsulasi: field-field private
    private final int id;
    private final String username, password, phone;
    private BigDecimal currentBalance;
    private final int role; // Add role field

    // Konstruktor: menginisialisasi objek User
    public User(int id, String username, String password, BigDecimal currentBalance, int role, String phone){
        this.id = id;
        this.username = username;
        this.password = password;
        this.currentBalance = currentBalance;
        this.role = role; // Initialize role
        this.phone = phone; // Initialize phone
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

    public int getRole() {
        return role; // Getter for role
    }

    public String getPhone() {
        return phone; // Getter for phone
    }

    // Setter: menyediakan akses tulis ke currentBalance dengan pembulatan
    public void setCurrentBalance(BigDecimal newBalance){
        // menyimpan nilai baru hingga 2 tempat desimal
        currentBalance = newBalance.setScale(2, RoundingMode.FLOOR);
    }
}