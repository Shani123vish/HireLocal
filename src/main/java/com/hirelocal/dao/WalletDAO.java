package com.hirelocal.dao;

import com.hirelocal.db.GigNestDB;
import java.sql.*;

public class WalletDAO {

    public void createWallet(int userId) throws SQLException {
        String sql = "INSERT INTO wallets (user_id, balance) VALUES (?, 0.00)";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    public double getBalance(int userId) throws SQLException {
        String sql = "SELECT balance FROM wallets WHERE user_id = ?";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getDouble("balance") : 0;
        }
    }

    public void deductBalance(int userId, double amount) throws SQLException {
        String sql = "UPDATE wallets SET balance = balance - ? WHERE user_id = ?";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    public void addBalance(int userId, double amount) throws SQLException {
        String sql = "UPDATE wallets SET balance = balance + ? WHERE user_id = ?";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }
}