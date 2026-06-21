package com.hirelocal.dao;

import com.hirelocal.db.GigNestDB;
import com.hirelocal.model.Escrow;
import java.sql.*;

public class EscrowDAO {

    public void holdPayment(Escrow escrow) throws SQLException {
        String sql = "INSERT INTO escrow (job_id, customer_id, worker_id, amount, commission) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, escrow.getJobId());
            ps.setInt(2, escrow.getCustomerId());
            ps.setInt(3, escrow.getWorkerId());
            ps.setDouble(4, escrow.getAmount());
            ps.setDouble(5, escrow.getCommission());
            ps.executeUpdate();
        }
    }

    public Escrow getHeldEscrow(int jobId) throws SQLException {
        String sql = "SELECT * FROM escrow WHERE job_id = ? AND status = 'HELD'";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, jobId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Escrow e = new Escrow();
                e.setId(rs.getInt("id"));
                e.setJobId(rs.getInt("job_id"));
                e.setCustomerId(rs.getInt("customer_id"));
                e.setWorkerId(rs.getInt("worker_id"));
                e.setAmount(rs.getDouble("amount"));
                e.setCommission(rs.getDouble("commission"));
                e.setStatus(rs.getString("status"));
                return e;
            }
        }
        return null;
    }

    public void updateStatus(int jobId, String status) throws SQLException {
        String sql = "UPDATE escrow SET status = ? WHERE job_id = ?";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, jobId);
            ps.executeUpdate();
        }
    }
}