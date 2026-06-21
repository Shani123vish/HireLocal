package com.hirelocal.servlet;

import com.google.gson.Gson;
import com.hirelocal.db.GigNestDB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/WorkerEarningsServlet")
public class WorkerEarningsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        HttpSession session = request.getSession(false);
        int workerId = (int) session.getAttribute("userId");

        try (Connection con = GigNestDB.getConnection()) {
            String sql = "SELECT w.balance, wp.total_earned, wp.commission_due FROM wallets w JOIN worker_profiles wp ON w.user_id = wp.user_id WHERE w.user_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, workerId);
            ResultSet rs = ps.executeQuery();

            Map<String, Object> result = new HashMap<>();

            if (rs.next()) {
                result.put("walletBalance", rs.getDouble("balance"));
                result.put("totalEarned", rs.getDouble("total_earned"));
                result.put("commissionDue", rs.getDouble("commission_due"));
            }

            String txnSql = "SELECT type, amount, description, created_at FROM transactions WHERE user_id = ? ORDER BY created_at DESC";
            PreparedStatement txnPs = con.prepareStatement(txnSql);
            txnPs.setInt(1, workerId);
            ResultSet txnRs = txnPs.executeQuery();

            List<Map<String, Object>> transactions = new ArrayList<>();
            while (txnRs.next()) {
                Map<String, Object> txn = new HashMap<>();
                txn.put("type", txnRs.getString("type"));
                txn.put("amount", txnRs.getDouble("amount"));
                txn.put("description", txnRs.getString("description"));
                txn.put("createdAt", txnRs.getString("created_at"));
                transactions.add(txn);
            }
            result.put("transactions", transactions);

            response.getWriter().write(new Gson().toJson(result));

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"error\": \"Failed to load earnings\"}");
        }
    }
}