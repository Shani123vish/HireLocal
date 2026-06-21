package com.hirelocal.dao;

import com.hirelocal.db.GigNestDB;
import java.sql.*;
import java.util.*;

public class TransactionDAO {

    public void saveTransaction(int userId, int jobId, String type, double amount, String description) throws SQLException {
        String sql = "INSERT INTO transactions (user_id, job_id, type, amount, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, jobId);
            ps.setString(3, type);
            ps.setDouble(4, amount);
            ps.setString(5, description);
            ps.executeUpdate();
        }
    }

    public List<Map<String, Object>> getTransactions(int userId) throws SQLException {
        String sql = "SELECT type, amount, description, created_at FROM transactions WHERE user_id = ? ORDER BY created_at DESC";
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("type", rs.getString("type"));
                row.put("amount", rs.getDouble("amount"));
                row.put("description", rs.getString("description"));
                row.put("createdAt", rs.getString("created_at"));
                list.add(row);
            }
        }
        return list;
    }

    public double getTotalCommission() throws SQLException {
        String sql = "SELECT IFNULL(SUM(commission), 0) FROM escrow WHERE status = 'RELEASED'";
        try (Connection con = GigNestDB.getConnection()) {
            ResultSet rs = con.createStatement().executeQuery(sql);
            return rs.next() ? rs.getDouble(1) : 0;
        }
    }
}