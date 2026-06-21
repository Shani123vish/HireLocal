package com.hirelocal.dao;

import com.hirelocal.db.GigNestDB;
import com.hirelocal.model.Cancellation;
import java.sql.*;

public class CancellationDAO {

    public int getCancelCount(int customerId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM cancellations WHERE customer_id = ?";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public void saveCancellation(Cancellation c) throws SQLException {
        String sql = "INSERT INTO cancellations (customer_id, job_id, cancel_count, penalty_amount, is_blocked) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getCustomerId());
            ps.setInt(2, c.getJobId());
            ps.setInt(3, c.getCancelCount());
            ps.setDouble(4, c.getPenaltyAmount());
            ps.setBoolean(5, c.isBlocked());
            ps.executeUpdate();
        }
    }
}