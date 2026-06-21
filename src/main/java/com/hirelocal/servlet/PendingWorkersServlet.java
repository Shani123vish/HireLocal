package com.hirelocal.servlet;

import com.google.gson.Gson;
import com.hirelocal.dao.WorkerDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/PendingWorkersServlet")
public class PendingWorkersServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        try {
            WorkerDAO workerDAO = new WorkerDAO();
            Map<String, Object> result = new HashMap<>();
            result.put("workers", workerDAO.getPendingWorkers());
            response.getWriter().write(new Gson().toJson(result));
        } catch (Exception e) {
            response.getWriter().write("{\"error\": \"Failed to load workers\"}");
        }
    }
}