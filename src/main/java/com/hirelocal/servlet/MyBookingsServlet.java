package com.hirelocal.servlet;

import com.google.gson.Gson;
import com.hirelocal.db.GigNestDB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/MyBookingsServlet")
public class MyBookingsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        HttpSession session = request.getSession(false);
        int customerId = (int) session.getAttribute("userId");

        try (Connection con = GigNestDB.getConnection()) {
            String sql = "SELECT j.id, sc.name AS categoryName, j.description, j.address, j.scheduled_time, j.status, " +
                         "u.name AS workerName, ja.worker_id AS workerId, " +
                         "(SELECT COUNT(*) FROM ratings r WHERE r.job_id = j.id) AS isRated, " +
                         "(SELECT COUNT(*) FROM escrow e WHERE e.job_id = j.id) AS isPaid " +
                         "FROM jobs j JOIN service_categories sc ON j.category_id = sc.id " +
                         "LEFT JOIN job_assignments ja ON j.id = ja.job_id " +
                         "LEFT JOIN users u ON ja.worker_id = u.id " +
                         "WHERE j.customer_id = ? ORDER BY j.created_at DESC";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            List<Map<String, Object>> jobs = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> job = new HashMap<>();
                job.put("id", rs.getInt("id"));
                job.put("categoryName", rs.getString("categoryName"));
                job.put("description", rs.getString("description"));
                job.put("address", rs.getString("address"));
                job.put("scheduledTime", rs.getString("scheduled_time"));
                job.put("status", rs.getString("status"));
                job.put("workerName", rs.getString("workerName"));
                job.put("workerId", rs.getInt("workerId"));
                job.put("isRated", rs.getInt("isRated") > 0);
                job.put("isPaid", rs.getInt("isPaid") > 0);
                jobs.add(job);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("jobs", jobs);
            response.getWriter().write(new Gson().toJson(result));

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"error\": \"Failed to load bookings\"}");
        }
    }
}