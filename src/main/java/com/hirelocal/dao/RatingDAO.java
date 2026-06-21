package com.hirelocal.dao;

import com.hirelocal.db.GigNestDB;
import java.sql.*;

public class RatingDAO {

    public void saveRating(int jobId, int customerId, int workerId, int stars, String feedback) throws SQLException {
        String sql = "INSERT INTO ratings (job_id, customer_id, worker_id, stars, feedback) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, jobId);
            ps.setInt(2, customerId);
            ps.setInt(3, workerId);
            ps.setInt(4, stars);
            ps.setString(5, feedback);
            ps.executeUpdate();
        }
    }
}