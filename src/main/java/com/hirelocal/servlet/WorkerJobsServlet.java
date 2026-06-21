package com.hirelocal.servlet;

import com.google.gson.Gson;
import com.hirelocal.dao.BookingDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/WorkerJobsServlet")
public class WorkerJobsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        HttpSession session = request.getSession(false);
        int workerId = (int) session.getAttribute("userId");

        try {
            BookingDAO bookingDAO = new BookingDAO();
            Map<String, Object> result = new HashMap<>();
            result.put("jobs", bookingDAO.getAllJobsForWorker(workerId));
            response.getWriter().write(new Gson().toJson(result));
        } catch (Exception e) {
            response.getWriter().write("{\"error\": \"Failed to load jobs\"}");
        }
    }
}