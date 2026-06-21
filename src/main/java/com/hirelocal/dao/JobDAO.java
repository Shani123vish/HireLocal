package com.hirelocal.dao;

import com.hirelocal.db.GigNestDB;
import com.hirelocal.model.Job;
import java.sql.*;
import java.util.*;

public class JobDAO {

    public int saveJob(Job job) throws SQLException {
        String sql = "INSERT INTO jobs (customer_id, category_id, description, address, city_id, scheduled_time) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, job.getCustomerId());
            ps.setInt(2, job.getCategoryId());
            ps.setString(3, job.getDescription());
            ps.setString(4, job.getAddress());
            ps.setInt(5, job.getCityId());
            ps.setString(6, job.getScheduledTime());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            return keys.next() ? keys.getInt(1) : -1;
        }
    }

    public void updateJobStatus(int jobId, String status) throws SQLException {
        String sql = "UPDATE jobs SET status = ? WHERE id = ?";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, jobId);
            ps.executeUpdate();
        }
    }

    public int getActiveJobCount(int customerId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM jobs WHERE customer_id = ? AND status IN ('PENDING','ASSIGNED','ONGOING')";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public int getTotalJobCount(int customerId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM jobs WHERE customer_id = ?";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public List<Map<String, Object>> getRecentJobsForCustomer(int customerId) throws SQLException {
        String sql = "SELECT j.id, sc.name AS categoryName, j.scheduled_time, j.status FROM jobs j JOIN service_categories sc ON j.category_id = sc.id WHERE j.customer_id = ? ORDER BY j.created_at DESC LIMIT 5";
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("categoryName", rs.getString("categoryName"));
                row.put("scheduledTime", rs.getString("scheduled_time"));
                row.put("status", rs.getString("status"));
                list.add(row);
            }
        }
        return list;
    }

    public List<Map<String, Object>> getJobsForCustomerFull(int customerId) throws SQLException {
        String sql = "SELECT j.id, sc.name AS categoryName, j.description, j.address, j.scheduled_time, j.status, u.name AS workerName, (SELECT COUNT(*) FROM ratings r WHERE r.job_id = j.id) AS isRated FROM jobs j JOIN service_categories sc ON j.category_id = sc.id LEFT JOIN job_assignments ja ON j.id = ja.job_id LEFT JOIN users u ON ja.worker_id = u.id WHERE j.customer_id = ? ORDER BY j.created_at DESC";
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("categoryName", rs.getString("categoryName"));
                row.put("description", rs.getString("description"));
                row.put("address", rs.getString("address"));
                row.put("scheduledTime", rs.getString("scheduled_time"));
                row.put("status", rs.getString("status"));
                row.put("workerName", rs.getString("workerName"));
                row.put("isRated", rs.getInt("isRated") > 0);
                list.add(row);
            }
        }
        return list;
    }

    public List<Map<String, Object>> getRecentJobsForAdmin() throws SQLException {
        String sql = "SELECT j.id, sc.name AS categoryName, u.name AS customerName, j.status FROM jobs j JOIN service_categories sc ON j.category_id = sc.id JOIN users u ON j.customer_id = u.id ORDER BY j.created_at DESC LIMIT 5";
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection con = GigNestDB.getConnection()) {
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("categoryName", rs.getString("categoryName"));
                row.put("customerName", rs.getString("customerName"));
                row.put("status", rs.getString("status"));
                list.add(row);
            }
        }
        return list;
    }
}