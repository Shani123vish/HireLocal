package com.hirelocal.dao;

import com.hirelocal.db.GigNestDB;
import java.sql.*;

public class NotificationDAO {

    public void sendNotification(int userId, String message) throws SQLException {
        String sql = "INSERT INTO notifications (user_id, message) VALUES (?, ?)";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, message);
            ps.executeUpdate();
        }
    }
}