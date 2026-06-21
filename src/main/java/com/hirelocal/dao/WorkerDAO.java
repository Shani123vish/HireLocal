package com.hirelocal.dao;

import com.hirelocal.db.GigNestDB;
import java.sql.*;
import java.util.*;

public class WorkerDAO {

    public int getCompletedJobCount(int workerId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM job_assignments WHERE worker_id = ? AND status = 'COMPLETED'";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, workerId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public double getWorkerRating(int workerId) throws SQLException {
        String sql = "SELECT rating FROM worker_profiles WHERE user_id = ?";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, workerId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getDouble("rating") : 0;
        }
    }

    public void updateRatingAndJobCount(int workerId) throws SQLException {
        String sql = "UPDATE worker_profiles SET rating = (SELECT AVG(stars) FROM ratings WHERE worker_id = ?), total_jobs = total_jobs + 1 WHERE user_id = ?";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, workerId);
            ps.setInt(2, workerId);
            ps.executeUpdate();
        }
    }

    public void verifyWorker(int workerId, boolean approve) throws SQLException {
        String sql = approve
                ? "UPDATE worker_profiles SET is_verified = true WHERE user_id = ?"
                : "UPDATE users SET is_active = false WHERE id = ?";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, workerId);
            ps.executeUpdate();
        }
    }

    public int getPendingVerifyCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM worker_profiles WHERE is_verified = false";
        try (Connection con = GigNestDB.getConnection()) {
            ResultSet rs = con.createStatement().executeQuery(sql);
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public List<Map<String, Object>> getPendingWorkers() throws SQLException {
        String sql = "SELECT u.id, u.name, u.phone, u.email, sc.name AS skillCategory, wp.base_price, wp.service_area FROM users u JOIN worker_profiles wp ON u.id = wp.user_id JOIN service_categories sc ON wp.skill_category_id = sc.id WHERE wp.is_verified = false AND u.is_active = true";
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection con = GigNestDB.getConnection()) {
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("name", rs.getString("name"));
                row.put("phone", rs.getString("phone"));
                row.put("email", rs.getString("email"));
                row.put("skillCategory", rs.getString("skillCategory"));
                row.put("basePrice", rs.getDouble("base_price"));
                row.put("serviceArea", rs.getString("service_area"));
                row.put("documents", getDocuments(rs.getInt("id")));
                list.add(row);
            }
        }
        return list;
    }

    private List<Map<String, Object>> getDocuments(int workerId) throws SQLException {
        String sql = "SELECT doc_type, file_path FROM worker_documents WHERE worker_id = ?";
        List<Map<String, Object>> docs = new ArrayList<>();
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, workerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("docType", rs.getString("doc_type"));
                doc.put("filePath", rs.getString("file_path"));
                docs.add(doc);
            }
        }
        return docs;
    }
}