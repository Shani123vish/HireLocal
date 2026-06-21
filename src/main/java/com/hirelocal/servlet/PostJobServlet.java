package com.hirelocal.servlet;

import com.hirelocal.dao.JobDAO;
import com.hirelocal.model.Job;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/PostJobServlet")
public class PostJobServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        HttpSession session = request.getSession(false);
        int customerId = (int) session.getAttribute("userId");

        try {
            Job job = new Job();
            job.setCustomerId(customerId);
            job.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
            job.setDescription(request.getParameter("description"));
            job.setAddress(request.getParameter("address"));
            job.setCityId(Integer.parseInt(request.getParameter("cityId")));
            job.setScheduledTime(request.getParameter("scheduledTime"));

            JobDAO jobDAO = new JobDAO();
            int jobId = jobDAO.saveJob(job);

            response.getWriter().write("{\"success\": true, \"jobId\": " + jobId + "}");
        } catch (Exception e) {
            response.getWriter().write("{\"success\": false, \"message\": \"Job posting failed\"}");
        }
    }
}