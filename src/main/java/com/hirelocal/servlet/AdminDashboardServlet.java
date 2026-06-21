package com.hirelocal.servlet;

import com.google.gson.Gson;
import com.hirelocal.dao.JobDAO;
import com.hirelocal.dao.TransactionDAO;
import com.hirelocal.dao.UserDAO;
import com.hirelocal.dao.WorkerDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/AdminDashboardServlet")
public class AdminDashboardServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        try {
            UserDAO userDAO = new UserDAO();
            WorkerDAO workerDAO = new WorkerDAO();
            JobDAO jobDAO = new JobDAO();
            TransactionDAO txnDAO = new TransactionDAO();

            Map<String, Object> result = new HashMap<>();
            result.put("totalUsers", userDAO.getTotalCount("CUSTOMER"));
            result.put("totalWorkers", userDAO.getTotalCount("WORKER"));
            result.put("pendingVerify", workerDAO.getPendingVerifyCount());
            result.put("totalCommission", txnDAO.getTotalCommission());
            result.put("recentJobs", jobDAO.getRecentJobsForAdmin());
            result.put("pendingWithdrawals", new ArrayList<>());

            response.getWriter().write(new Gson().toJson(result));
        } catch (Exception e) {
            response.getWriter().write("{\"error\": \"Dashboard load failed\"}");
        }
    }
}