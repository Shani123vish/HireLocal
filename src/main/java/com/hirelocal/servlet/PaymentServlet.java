package com.hirelocal.servlet;

import com.google.gson.Gson;
import com.hirelocal.db.GigNestDB;
import com.hirelocal.util.CommissionCalc;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/PaymentServlet")
public class PaymentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String action = request.getParameter("action");

        try {
            if ("pay".equals(action)) {
                customerPay(request, response);
            } else if ("payCommission".equals(action)) {
                payCommission(request, response);
            } else if ("getWorkerBalance".equals(action)) {
                getWorkerBalance(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    private void customerPay(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int jobId = Integer.parseInt(request.getParameter("jobId"));
        int workerId = Integer.parseInt(request.getParameter("workerId"));
        double amount = Double.parseDouble(request.getParameter("amount"));
        double commission = CommissionCalc.calculate(amount);
        double workerEarning = amount - commission;

        try (Connection con = GigNestDB.getConnection()) {
            String checkSql = "SELECT id FROM escrow WHERE job_id = ?";
            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setInt(1, jobId);
            ResultSet checkRs = checkPs.executeQuery();
            if (checkRs.next()) {
                response.getWriter().write("{\"success\": false, \"message\": \"Payment already done\"}");
                return;
            }

            HttpSession session = request.getSession(false);
            int customerId = (int) session.getAttribute("userId");

            String escrowSql = "INSERT INTO escrow (job_id, customer_id, worker_id, amount, commission, status) VALUES (?, ?, ?, ?, ?, 'RELEASED')";
            PreparedStatement escrowPs = con.prepareStatement(escrowSql);
            escrowPs.setInt(1, jobId);
            escrowPs.setInt(2, customerId);
            escrowPs.setInt(3, workerId);
            escrowPs.setDouble(4, amount);
            escrowPs.setDouble(5, commission);
            escrowPs.executeUpdate();

            String workerWalletSql = "UPDATE wallets SET balance = balance + ? WHERE user_id = ?";
            PreparedStatement workerPs = con.prepareStatement(workerWalletSql);
            workerPs.setDouble(1, workerEarning);
            workerPs.setInt(2, workerId);
            workerPs.executeUpdate();

            String updateProfileSql = "UPDATE worker_profiles SET total_earned = total_earned + ?, commission_due = commission_due + ? WHERE user_id = ?";
            PreparedStatement profilePs = con.prepareStatement(updateProfileSql);
            profilePs.setDouble(1, workerEarning);
            profilePs.setDouble(2, commission);
            profilePs.setInt(3, workerId);
            profilePs.executeUpdate();

            String txnSql = "INSERT INTO transactions (user_id, job_id, type, amount, description) VALUES (?, ?, 'CREDIT', ?, 'Payment received for job')";
            PreparedStatement txnPs = con.prepareStatement(txnSql);
            txnPs.setInt(1, workerId);
            txnPs.setInt(2, jobId);
            txnPs.setDouble(3, workerEarning);
            txnPs.executeUpdate();

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("workerEarning", workerEarning);
            result.put("commission", commission);
            response.getWriter().write(new Gson().toJson(result));
        }
    }

    private void payCommission(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        int workerId = (int) session.getAttribute("userId");

        try (Connection con = GigNestDB.getConnection()) {
            String checkSql = "SELECT commission_due FROM worker_profiles WHERE user_id = ?";
            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setInt(1, workerId);
            ResultSet rs = checkPs.executeQuery();
            rs.next();
            double commissionDue = rs.getDouble("commission_due");

            if (commissionDue <= 0) {
                response.getWriter().write("{\"success\": false, \"message\": \"No commission due\"}");
                return;
            }

            String deductSql = "UPDATE wallets SET balance = balance - ? WHERE user_id = ?";
            PreparedStatement deductPs = con.prepareStatement(deductSql);
            deductPs.setDouble(1, commissionDue);
            deductPs.setInt(2, workerId);
            deductPs.executeUpdate();

            String clearSql = "UPDATE worker_profiles SET commission_due = 0 WHERE user_id = ?";
            PreparedStatement clearPs = con.prepareStatement(clearSql);
            clearPs.setInt(1, workerId);
            clearPs.executeUpdate();

            response.getWriter().write("{\"success\": true, \"message\": \"Commission paid successfully\"}");
        }
    }

    private void getWorkerBalance(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        int workerId = (int) session.getAttribute("userId");

        try (Connection con = GigNestDB.getConnection()) {
            String sql = "SELECT w.balance, wp.total_earned, wp.commission_due FROM wallets w JOIN worker_profiles wp ON w.user_id = wp.user_id WHERE w.user_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, workerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("balance", rs.getDouble("balance"));
                result.put("totalEarned", rs.getDouble("total_earned"));
                result.put("commissionDue", rs.getDouble("commission_due"));
                response.getWriter().write(new Gson().toJson(result));
            }
        }
    }
}