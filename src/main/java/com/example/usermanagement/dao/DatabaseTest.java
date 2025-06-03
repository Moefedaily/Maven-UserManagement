package com.example.usermanagement.dao;

public class DatabaseTest {
    
    public static void main(String[] args) {
        System.out.println("=== PostgreSQL Database Connection Test ===");
        
        System.out.println("\n1. Testing database connection...");
        DatabaseConnection.testConnection();
        
        System.out.println("\n2. Initializing database...");
        DatabaseConnection.initializeDatabase();
        
        System.out.println("\n=== Test Complete ===");
    }
}