package com.hirelocal.servlet;

import com.hirelocal.dao.NotificationDAO;
import com.hirelocal.dao.WorkerDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/VerifyWorkerServlet")
public class VerifyWorkerServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        int workerId = Integer.parseInt(request.getParameter("workerId"));
        boolean approve = Boolean.parseBoolean(request.getParameter("approve"));

        try {
            WorkerDAO workerDAO = new WorkerDAO();
            workerDAO.verifyWorker(workerId, approve);

            if (approve) {
                NotificationDAO notificationDAO = new NotificationDAO();
                notificationDAO.sendNotification(workerId, "Congratulations! Your profile is verified. You can now receive job requests.");
            }

            response.getWriter().write("{\"success\": true}");
        } catch (Exception e) {
            response.getWriter().write("{\"success\": false, \"message\": \"Verification failed\"}");
        }
    }
}