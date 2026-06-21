package com.hirelocal.servlet;

import com.hirelocal.dao.CancellationDAO;
import com.hirelocal.dao.JobDAO;
import com.hirelocal.dao.WalletDAO;
import com.hirelocal.model.Cancellation;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/CancelServlet")
public class CancelServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        HttpSession session = request.getSession(false);
        int customerId = (int) session.getAttribute("userId");
        int jobId = Integer.parseInt(request.getParameter("jobId"));

        try {
            CancellationDAO cancelDAO = new CancellationDAO();
            WalletDAO walletDAO = new WalletDAO();
            JobDAO jobDAO = new JobDAO();

            int prevCount = cancelDAO.getCancelCount(customerId);
            int cancelCount = prevCount + 1;

            double penalty = 0;
            boolean blocked = false;

            if (cancelCount == 2) penalty = 10;
            else if (cancelCount == 3) { penalty = 25; blocked = true; }
            else if (cancelCount > 3) { penalty = 50; blocked = true; }

            if (penalty > 0) walletDAO.deductBalance(customerId, penalty);

            Cancellation c = new Cancellation();
            c.setCustomerId(customerId);
            c.setJobId(jobId);
            c.setCancelCount(cancelCount);
            c.setPenaltyAmount(penalty);
            c.setBlocked(blocked);
            cancelDAO.saveCancellation(c);

            jobDAO.updateJobStatus(jobId, "CANCELLED");

            response.getWriter().write("{\"success\": true, \"penalty\": " + penalty + ", \"cancelCount\": " + cancelCount + "}");
        } catch (Exception e) {
            response.getWriter().write("{\"success\": false, \"message\": \"Cancellation failed\"}");
        }
    }
}