package com.hirelocal.dao;

import com.hirelocal.db.GigNestDB;
import com.hirelocal.model.User;
import java.sql.*;

public class UserDAO {

    public User getUserByEmailAndPassword(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ? AND is_active = true";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        }
        return null;
    }

    public boolean emailOrPhoneExists(String email, String phone) throws SQLException {
        String sql = "SELECT id FROM users WHERE email = ? OR phone = ?";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, phone);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public int saveUser(User user) throws SQLException {
        String sql = "INSERT INTO users (name, email, phone, password, role, city_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getRole());
            ps.setInt(6, user.getCityId());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            return keys.next() ? keys.getInt(1) : -1;
        }
    }

    public int getTotalCount(String role) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE role = ?";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, role);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPhone(rs.getString("phone"));
        u.setRole(rs.getString("role"));
        u.setCityId(rs.getInt("city_id"));
        u.setActive(rs.getBoolean("is_active"));
        return u;
    }
}