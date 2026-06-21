package com.hirelocal.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.hirelocal.dao.UserDAO;
import com.hirelocal.dao.WalletDAO;
import com.hirelocal.db.GigNestDB;
import com.hirelocal.model.User;
import com.hirelocal.util.PasswordUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");

		try {
			// ── Common Fields ──
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			String password = request.getParameter("password");
			String role = request.getParameter("role");
			String cityIdParam = request.getParameter("cityId");

			// Basic null check
			if (name == null || name.trim().isEmpty() || email == null || email.trim().isEmpty() || phone == null
					|| phone.trim().isEmpty() || password == null || password.trim().isEmpty() || role == null
					|| role.trim().isEmpty() || cityIdParam == null || cityIdParam.trim().isEmpty()) {

				response.getWriter().write("{\"success\": false, \"message\": \"All fields are required\"}");
				return;
			}

			int cityId = Integer.parseInt(cityIdParam);

			UserDAO userDAO = new UserDAO();
			WalletDAO walletDAO = new WalletDAO();

			// Duplicate email/phone check
			if (userDAO.emailOrPhoneExists(email, phone)) {
				response.getWriter().write("{\"success\": false, \"message\": \"Email or phone already registered\"}");
				return;
			}

			// ── Save User ──
			User user = new User();
			user.setName(name);
			user.setEmail(email);
			user.setPhone(phone);
			user.setPassword(PasswordUtil.encode(password));
			user.setRole(role);
			user.setCityId(cityId);

			int newUserId = userDAO.saveUser(user);

			// ── Create Wallet ──
			walletDAO.createWallet(newUserId);

			// ── Worker Fields (agar role WORKER hai) ──
			if ("WORKER".equals(role)) {

				String skillCategoryIdParam = request.getParameter("skillCategoryId");
				String basePriceParam = request.getParameter("basePrice");
				String serviceArea = request.getParameter("serviceArea");
				String experienceYearsParam = request.getParameter("experienceYears");

				// Worker fields null/empty check
				if (skillCategoryIdParam == null || skillCategoryIdParam.trim().isEmpty() || basePriceParam == null
						|| basePriceParam.trim().isEmpty() || serviceArea == null || serviceArea.trim().isEmpty()
						|| experienceYearsParam == null || experienceYearsParam.trim().isEmpty()) {

					response.getWriter().write("{\"success\": false, \"message\": \"Please fill all worker details\"}");
					return;
				}

				int skillCategoryId = Integer.parseInt(skillCategoryIdParam);
				double basePrice = Double.parseDouble(basePriceParam);
				int experienceYears = Integer.parseInt(experienceYearsParam);

				// Worker ko directly yahan insert karo — koi alag class nahi chahiye
				String sql = "INSERT INTO worker_profiles (user_id, skill_category_id, base_price, service_area, experience_years) "
						+ "VALUES (?, ?, ?, ?, ?)";

				Connection con = GigNestDB.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setInt(1, newUserId);
				ps.setInt(2, skillCategoryId);
				ps.setDouble(3, basePrice);
				ps.setString(4, serviceArea);
				ps.setInt(5, experienceYears);
				ps.executeUpdate();
				ps.close();
				con.close();
			}

			response.getWriter().write("{\"success\": true, \"message\": \"Registration successful\"}");

		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
		}
	}
}