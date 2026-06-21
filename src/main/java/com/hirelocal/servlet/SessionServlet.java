package com.hirelocal.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/SessionServlet")
public class SessionServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            int userId = (int) session.getAttribute("userId");
            String userName = (String) session.getAttribute("userName");
            String userRole = (String) session.getAttribute("userRole");
            response.getWriter().write("{\"userId\": " + userId + ", \"userName\": \"" + userName + "\", \"userRole\": \"" + userRole + "\"}");
        } else {
            response.getWriter().write("{\"userId\": null}");
        }
    }
}