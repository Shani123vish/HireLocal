package com.hirelocal.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter(urlPatterns = {"/customer/*", "/worker/*", "/admin/*"})
public class AuthFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("userId") != null);
        if (!loggedIn) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }
        chain.doFilter(req, res);
    }
}