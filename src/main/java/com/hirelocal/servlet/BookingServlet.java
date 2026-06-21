package com.hirelocal.servlet;

import com.hirelocal.dao.BookingDAO;
import com.hirelocal.dao.JobDAO;
import com.hirelocal.db.GigNestDB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/BookingServlet")
public class BookingServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String action = request.getParameter("action");

        try {
            BookingDAO bookingDAO = new BookingDAO();
            JobDAO jobDAO = new JobDAO();

            if ("assign".equals(action)) {
                int jobId = Integer.parseInt(request.getParameter("jobId"));
                int workerId = Integer.parseInt(request.getParameter("workerId"));

                if (hasCommissionDue(workerId)) {
                    response.getWriter().write("{\"success\": false, \"message\": \"Worker has pending commission. Cannot assign new job.\"}");
                    return;
                }

                bookingDAO.assignWorker(jobId, workerId);
                jobDAO.updateJobStatus(jobId, "ASSIGNED");

            } else if ("updateStatus".equals(action)) {
                int jobId = Integer.parseInt(request.getParameter("jobId"));
                String status = request.getParameter("status");
                bookingDAO.updateAssignmentStatus(jobId, status);
                jobDAO.updateJobStatus(jobId, mapToJobStatus(status));
            }

            response.getWriter().write("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    private boolean hasCommissionDue(int workerId) throws SQLException {
        String sql = "SELECT commission_due FROM worker_profiles WHERE user_id = ?";
        try (Connection con = GigNestDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, workerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("commission_due") > 0;
            }
        }
        return false;
    }

    private String mapToJobStatus(String assignmentStatus) {
        switch (assignmentStatus) {
            case "ACCEPTED": return "ASSIGNED";
            case "STARTED": return "ONGOING";
            case "COMPLETED": return "COMPLETED";
            default: return "PENDING";
        }
    }
}