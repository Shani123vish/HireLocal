package com.hirelocal.servlet;

import com.google.gson.Gson;
import com.hirelocal.db.GigNestDB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/AIRecommendServlet")
public class AIRecommendServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String categoryId = request.getParameter("categoryId");
        String cityId = request.getParameter("cityId");

        try (Connection con = GigNestDB.getConnection()) {
            String sql = "SELECT u.id, u.name, wp.rating, wp.base_price, wp.total_jobs " +
                         "FROM users u JOIN worker_profiles wp ON u.id = wp.user_id " +
                         "WHERE wp.skill_category_id = ? AND u.city_id = ? " +
                         "AND wp.is_verified = 1 AND wp.is_available = 1 " +
                         "ORDER BY wp.rating DESC, wp.total_jobs DESC LIMIT 3";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(categoryId));
            ps.setInt(2, Integer.parseInt(cityId));
            ResultSet rs = ps.executeQuery();

            List<Map<String, Object>> workers = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> w = new HashMap<>();
                w.put("id", rs.getInt("id"));
                w.put("name", rs.getString("name"));
                w.put("rating", rs.getDouble("rating"));
                w.put("base_price", rs.getDouble("base_price"));
                w.put("total_jobs", rs.getInt("total_jobs"));
                workers.add(w);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("workers", workers);
            response.getWriter().write(new Gson().toJson(result));

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}