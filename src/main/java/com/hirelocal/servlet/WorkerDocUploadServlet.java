package com.hirelocal.servlet;

import com.hirelocal.db.GigNestDB;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/WorkerDocUploadServlet")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class WorkerDocUploadServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        HttpSession session = request.getSession(false);
        int workerId = (int) session.getAttribute("userId");
        String docType = request.getParameter("docType");

        try {
            Part filePart = request.getPart("file");
            String originalName = filePart.getSubmittedFileName();
            String ext = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();

            if (!ext.equals(".pdf") && !ext.equals(".jpg") && !ext.equals(".png")) {
                response.getWriter().write("{\"success\": false, \"message\": \"Only PDF, JPG, PNG allowed\"}");
                return;
            }

            String fileName = workerId + "_" + System.currentTimeMillis() + ext;
            String uploadDir = getServletContext().getRealPath("/uploads/documents/");
            new File(uploadDir).mkdirs();
            filePart.write(uploadDir + File.separator + fileName);

            String sql = "INSERT INTO worker_documents (worker_id, doc_type, file_path) VALUES (?, ?, ?)";
            try (Connection con = GigNestDB.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, workerId);
                ps.setString(2, docType);
                ps.setString(3, "/uploads/documents/" + fileName);
                ps.executeUpdate();
            }

            response.getWriter().write("{\"success\": true, \"fileName\": \"" + fileName + "\"}");
        } catch (Exception e) {
            response.getWriter().write("{\"success\": false, \"message\": \"Upload failed\"}");
        }
    }
}