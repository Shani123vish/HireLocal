package com.hirelocal.dao;

import com.hirelocal.db.GigNestDB;
import java.sql.*;
import java.util.*;

public class BookingDAO {

    public void assignWorker(int jobId, int workerId) throws SQLException {
        String sql = "INSERT INTO job_assignments (job_id, worker_id, status) VALUES (?, ?, 'REQUESTED')";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, jobId);
            ps.setInt(2, workerId);
            ps.executeUpdate();
        }
    }

    public void updateAssignmentStatus(int jobId, String status) throws SQLException {
        String sql = "UPDATE job_assignments SET status = ? WHERE job_id = ?";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, jobId);
            ps.executeUpdate();
        }
    }

    public int getWorkerIdByJob(int jobId) throws SQLException {
        String sql = "SELECT worker_id FROM job_assignments WHERE job_id = ? AND status = 'COMPLETED'";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, jobId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("worker_id") : -1;
        }
    }

    public List<Map<String, Object>> getNewJobRequestsForWorker(int workerId) throws SQLException {
        String sql = "SELECT ja.id AS assignmentId, j.id AS jobId, sc.name AS categoryName, j.description, j.address, j.scheduled_time FROM job_assignments ja JOIN jobs j ON ja.job_id = j.id JOIN service_categories sc ON j.category_id = sc.id WHERE ja.worker_id = ? AND ja.status = 'REQUESTED'";
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, workerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("assignmentId", rs.getInt("assignmentId"));
                row.put("jobId", rs.getInt("jobId"));
                row.put("categoryName", rs.getString("categoryName"));
                row.put("description", rs.getString("description"));
                row.put("address", rs.getString("address"));
                row.put("scheduledTime", rs.getString("scheduled_time"));
                list.add(row);
            }
        }
        return list;
    }

    public List<Map<String, Object>> getAllJobsForWorker(int workerId) throws SQLException {
        String sql = "SELECT j.id AS jobId, sc.name AS categoryName, j.description, j.address, j.scheduled_time, ja.status FROM job_assignments ja JOIN jobs j ON ja.job_id = j.id JOIN service_categories sc ON j.category_id = sc.id WHERE ja.worker_id = ? ORDER BY ja.assigned_at DESC";
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, workerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("jobId", rs.getInt("jobId"));
                row.put("categoryName", rs.getString("categoryName"));
                row.put("description", rs.getString("description"));
                row.put("address", rs.getString("address"));
                row.put("scheduledTime", rs.getString("scheduled_time"));
                row.put("status", rs.getString("status"));
                list.add(row);
            }
        }
        return list;
    }
}