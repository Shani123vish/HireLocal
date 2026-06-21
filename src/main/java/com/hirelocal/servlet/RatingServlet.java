package com.hirelocal.servlet;

import com.hirelocal.dao.BookingDAO;
import com.hirelocal.dao.RatingDAO;
import com.hirelocal.dao.WorkerDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/RatingServlet")
public class RatingServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        HttpSession session = request.getSession(false);
        int customerId = (int) session.getAttribute("userId");
        int jobId = Integer.parseInt(request.getParameter("jobId"));
        int stars = Integer.parseInt(request.getParameter("stars"));
        String feedback = request.getParameter("feedback");

        try {
            BookingDAO bookingDAO = new BookingDAO();
            int workerId = bookingDAO.getWorkerIdByJob(jobId);

            if (workerId == -1) {
                response.getWriter().write("{\"success\": false, \"message\": \"Job not found\"}");
                return;
            }

            RatingDAO ratingDAO = new RatingDAO();
            ratingDAO.saveRating(jobId, customerId, workerId, stars, feedback);

            WorkerDAO workerDAO = new WorkerDAO();
            workerDAO.updateRatingAndJobCount(workerId);

            response.getWriter().write("{\"success\": true}");
        } catch (Exception e) {
            response.getWriter().write("{\"success\": false, \"message\": \"Rating failed\"}");
        }
    }
}