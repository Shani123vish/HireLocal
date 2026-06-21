package com.hirelocal.servlet;

import com.google.gson.Gson;
import com.hirelocal.dao.JobDAO;
import com.hirelocal.dao.WalletDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/CustomerDashboardServlet")
public class CustomerDashboardServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        HttpSession session = request.getSession(false);
        int customerId = (int) session.getAttribute("userId");

        try {
            JobDAO jobDAO = new JobDAO();
            WalletDAO walletDAO = new WalletDAO();

            Map<String, Object> result = new HashMap<>();
            result.put("activeJobs", jobDAO.getActiveJobCount(customerId));
            result.put("totalJobs", jobDAO.getTotalJobCount(customerId));
            result.put("walletBalance", walletDAO.getBalance(customerId));
            result.put("recentJobs", jobDAO.getRecentJobsForCustomer(customerId));

            response.getWriter().write(new Gson().toJson(result));
        } catch (Exception e) {
            response.getWriter().write("{\"error\": \"Dashboard load failed\"}");
        }
    }
}