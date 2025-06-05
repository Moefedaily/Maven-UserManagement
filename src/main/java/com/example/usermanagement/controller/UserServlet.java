package com.example.usermanagement.controller;

import com.example.usermanagement.dao.DatabaseConnection;
import com.example.usermanagement.dao.UserDAO;
import com.example.usermanagement.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet(name = "UserServlet", urlPatterns = { "/users", "/user" })
public class UserServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
        DatabaseConnection.initializeDatabase();
        System.out.println("UserServlet initialized successfully!");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            switch (action != null ? action : "list") {
                case "list":
                    listUsers(request, response);
                    break;
                case "add":
                    showAddForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteUser(request, response);
                    break;
                default:
                    listUsers(request, response);
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error in doGet: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An error occurred while processing your request.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            switch (action != null ? action : "") {
                case "add":
                    addUser(request, response);
                    break;
                case "update":
                    updateUser(request, response);
                    break;
                default:
                    listUsers(request, response);
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error in doPost: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An error occurred while processing your request.");
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<User> users = userDAO.getAllUsers();
        int userCount = userDAO.getUserCount();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        java.util.Map<Integer, String> formattedDates = new java.util.HashMap<>();

        for (User user : users) {
            if (user.getDateNaissance() != null) {
                String formattedDate = user.getDateNaissance().format(formatter);
                formattedDates.put(user.getId(), formattedDate);
            }
        }

        request.setAttribute("users", users);
        request.setAttribute("userCount", userCount);
        request.setAttribute("formattedDates", formattedDates);

        request.getRequestDispatcher("/WEB-INF/views/listUsers.jsp")
                .forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/addUser.jsp")
                .forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userId = Integer.parseInt(request.getParameter("id"));
        User user = userDAO.getUserById(userId);

        if (user != null) {
            request.setAttribute("user", user);
            request.getRequestDispatcher("/WEB-INF/views/editUser.jsp")
                    .forward(request, response);
        } else {
            request.setAttribute("errorMessage", "User not found with ID: " + userId);
            listUsers(request, response);
        }
    }

    private void addUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String dateString = request.getParameter("dateNaissance");

        if (name == null || name.trim().isEmpty() ||
                email == null || email.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Name and email are required fields.");
            showAddForm(request, response);
            return;
        }

        if (userDAO.emailExists(email)) {
            request.setAttribute("errorMessage", "Email already exists: " + email);
            showAddForm(request, response);
            return;
        }

        try {
            User user = new User();
            user.setName(name.trim());
            user.setEmail(email.trim());
            user.setPhone(phone != null ? phone.trim() : null);

            if (dateString != null && !dateString.trim().isEmpty()) {
                user.setDateNaissance(LocalDate.parse(dateString));
            }

            if (userDAO.addUser(user)) {
                request.setAttribute("successMessage", "User added successfully: " + name);
            } else {
                request.setAttribute("errorMessage", "Failed to add user. Please try again.");
            }

        } catch (DateTimeParseException e) {
            request.setAttribute("errorMessage", "Invalid date format. Please use YYYY-MM-DD.");
            showAddForm(request, response);
            return;
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error adding user: " + e.getMessage());
            showAddForm(request, response);
            return;
        }

        listUsers(request, response);
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String dateString = request.getParameter("dateNaissance");

            if (name == null || name.trim().isEmpty() ||
                    email == null || email.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Name and email are required fields.");
                showEditForm(request, response);
                return;
            }

            User user = userDAO.getUserById(userId);
            if (user == null) {
                request.setAttribute("errorMessage", "User not found.");
                listUsers(request, response);
                return;
            }

            user.setName(name.trim());
            user.setEmail(email.trim());
            user.setPhone(phone != null ? phone.trim() : null);

            if (dateString != null && !dateString.trim().isEmpty()) {
                user.setDateNaissance(LocalDate.parse(dateString));
            } else {
                user.setDateNaissance(null);
            }

            if (userDAO.updateUser(user)) {
                request.setAttribute("successMessage", "User updated successfully: " + name);
            } else {
                request.setAttribute("errorMessage", "Failed to update user. Please try again.");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid user ID.");
        } catch (DateTimeParseException e) {
            request.setAttribute("errorMessage", "Invalid date format. Please use YYYY-MM-DD.");
            showEditForm(request, response);
            return;
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error updating user: " + e.getMessage());
        }

        listUsers(request, response);
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        System.out.println("DEBUG: Delete request - ID parameter: '" + idParam + "'");

        if (idParam == null || idParam.trim().isEmpty()) {
            System.out.println("ERROR: Empty or null ID parameter");
            request.setAttribute("errorMessage", "Invalid user ID - cannot delete user.");
            listUsers(request, response);
            return;
        }

        try {
            int userId = Integer.parseInt(idParam.trim());
            System.out.println("DEBUG: Attempting to delete user ID: " + userId);

            if (userDAO.deleteUser(userId)) {
                request.setAttribute("successMessage", "User deleted successfully.");
            } else {
                request.setAttribute("errorMessage", "Failed to delete user.");
            }

        } catch (NumberFormatException e) {
            System.out.println("ERROR: Invalid ID format: " + idParam);
            request.setAttribute("errorMessage", "Invalid user ID format.");
        }

        listUsers(request, response);
    }
}