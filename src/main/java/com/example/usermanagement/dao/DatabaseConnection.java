package com.example.usermanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/usermanagement";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "root"; 
    
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load PostgreSQL driver", e);
        }
    }
    

    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Connected to PostgreSQL database successfully!");
            return connection;
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            throw e;
        }
    }
    
    
    public static void initializeDatabase() {
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                email VARCHAR(150) UNIQUE NOT NULL,
                phone VARCHAR(20),
                date_naissance DATE
            )
        """;
        
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            
            statement.execute(createUsersTable);
            System.out.println("Database initialized successfully!");
            
            insertMockData(connection);
            
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    private static void insertMockData(Connection connection) throws SQLException {
        String checkData = "SELECT COUNT(*) FROM users";
        String insertSample = """
            INSERT INTO users (name, email, phone, date_naissance) VALUES
            ('Doe Desire', 'doe.desire@gmail.com', '+1234567890', '2000-05-15'),
            ('Will Smith', 'will.smith@gmail.com', '+1987654321', '1980-12-10'),
            ('Margot ruby', 'margot.ruby@gmail.com', '+1122334455', '1992-08-20')
            ON CONFLICT (email) DO NOTHING
        """;
        
        try (Statement statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(checkData);
            resultSet.next();
            int count = resultSet.getInt(1);
            
            if (count == 0) {
                statement.execute(insertSample);
                System.out.println("Mock data inserted successfully!");
            } else {
                System.out.println("Database already has data, skipping sample insertion.");
            }
        }
    }
    
   
    public static void testConnection() {
        try (Connection connection = getConnection()) {
            System.out.println("Database connection test successful!");
            System.out.println("Database: " + connection.getCatalog());
        } catch (SQLException e) {
            System.err.println("Database connection test failed!");
            System.err.println("Error: " + e.getMessage());
            System.err.println("\nPlease check:");
            System.err.println("1. PostgreSQL is running");
            System.err.println("2. Database 'usermanagement' exists");
            System.err.println("3. Username/password are correct");
        }
    }
}