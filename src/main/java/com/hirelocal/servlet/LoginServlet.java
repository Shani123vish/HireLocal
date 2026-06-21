package com.hirelocal.servlet;

import com.hirelocal.dao.UserDAO;
import com.hirelocal.model.User;
import com.hirelocal.util.PasswordUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String email = request.getParameter("email");
        String password = PasswordUtil.encode(request.getParameter("password"));

        try {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserByEmailAndPassword(email, password);

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("userId", user.getId());
                session.setAttribute("userName", user.getName());
                session.setAttribute("userRole", user.getRole());

                String redirect = getRedirectByRole(user.getRole(), request);
                response.getWriter().write("{\"success\": true, \"redirect\": \"" + redirect + "\"}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Invalid email or password\"}");
            }
        } catch (Exception e) {
            response.getWriter().write("{\"success\": false, \"message\": \"Server error\"}");
        }
    }

    private String getRedirectByRole(String role, HttpServletRequest req) {
        String base = req.getContextPath();
        switch (role) {
            case "CUSTOMER": return base + "/customer/dashboard.html";
            case "WORKER": return base + "/worker/dashboard.html";
            case "ADMIN":
            case "SUPER_ADMIN": return base + "/admin/dashboard.html";
            default: return base + "/index.html";
        }
    }
}