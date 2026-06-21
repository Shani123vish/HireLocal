package com.hirelocal.servlet;

import com.google.gson.Gson;
import com.hirelocal.dao.BookingDAO;
import com.hirelocal.dao.WalletDAO;
import com.hirelocal.dao.WorkerDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/WorkerDashboardServlet")
public class WorkerDashboardServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        HttpSession session = request.getSession(false);
        int workerId = (int) session.getAttribute("userId");

        try {
            WorkerDAO workerDAO = new WorkerDAO();
            WalletDAO walletDAO = new WalletDAO();
            BookingDAO bookingDAO = new BookingDAO();

            Map<String, Object> result = new HashMap<>();
            result.put("completedJobs", workerDAO.getCompletedJobCount(workerId));
            result.put("rating", workerDAO.getWorkerRating(workerId));
            result.put("totalEarnings", walletDAO.getBalance(workerId));
            result.put("newJobs", bookingDAO.getNewJobRequestsForWorker(workerId));

            response.getWriter().write(new Gson().toJson(result));
        } catch (Exception e) {
            response.getWriter().write("{\"error\": \"Dashboard load failed\"}");
        }
    }
}