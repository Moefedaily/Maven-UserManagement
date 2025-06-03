package com.example.usermanagement.dao;

import com.example.usermanagement.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserDAO {
    
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, name, email, phone, date_naissance FROM users ORDER BY name";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone"));
                
                Date dateNaissance = resultSet.getDate("date_naissance");
                if (dateNaissance != null) {
                    user.setDateNaissance(dateNaissance.toLocalDate());
                }
                
                users.add(user);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving users: " + e.getMessage());
            e.printStackTrace();
        }
        
        return users;
    }
    
    
    public User getUserById(int id) {
        String sql = "SELECT id, name, email, phone, date_naissance FROM users WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setName(resultSet.getString("name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPhone(resultSet.getString("phone"));
                    
                    Date dateNaissance = resultSet.getDate("date_naissance");
                    if (dateNaissance != null) {
                        user.setDateNaissance(dateNaissance.toLocalDate());
                    }
                    
                    return user;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    public boolean addUser(User user) {
        String sql = "INSERT INTO users (name, email, phone, date_naissance) VALUES (?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            
            if (user.getDateNaissance() != null) {
                statement.setDate(4, Date.valueOf(user.getDateNaissance()));
            } else {
                statement.setNull(4, Types.DATE);
            }
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                    }
                }
                System.out.println("User added successfully: " + user.getName());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
 
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, phone = ?, date_naissance = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            
            if (user.getDateNaissance() != null) {
                statement.setDate(4, Date.valueOf(user.getDateNaissance()));
            } else {
                statement.setNull(4, Types.DATE);
            }
            
            statement.setInt(5, user.getId());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("User updated successfully: " + user.getName());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("User deleted successfully. ID: " + id);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
  
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, email);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    
    public int getUserCount() {
        String sql = "SELECT COUNT(*) FROM users";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting users: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
}