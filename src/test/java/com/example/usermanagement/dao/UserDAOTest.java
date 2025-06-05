package com.example.usermanagement.dao;

import com.example.usermanagement.model.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("UserDAO Tests")
class UserDAOTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private Statement mockStatement;

    private UserDAO userDAO;
    private User testUser;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();

        testUser = new User();
        testUser.setId(1);
        testUser.setName("John Snow");
        testUser.setEmail("Snow.king@winter.com");
        testUser.setPhone("+175567890");
        testUser.setDateNaissance(LocalDate.of(1990, 5, 15));

        System.out.println("Test setup completed for: " + testUser.getName());
    }

    @Test
    @Order(1)
    @DisplayName("Test Add User - Success")
    void testAddUser_Success() throws SQLException {
        System.out.println("\n Testing addUser()");

        try (MockedStatic<DatabaseConnection> mockedStatic = mockStatic(DatabaseConnection.class)) {
            mockedStatic.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                    .thenReturn(mockPreparedStatement);

            when(mockPreparedStatement.executeUpdate()).thenReturn(1);
            when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt(1)).thenReturn(1);

            boolean result = userDAO.addUser(testUser);

            assertTrue(result, "AddUser should return true for successful insertion");
            assertEquals(1, testUser.getId(), "User ID should be set to generated key");

            verify(mockPreparedStatement).setString(1, testUser.getName());
            verify(mockPreparedStatement).setString(2, testUser.getEmail());
            verify(mockPreparedStatement).setString(3, testUser.getPhone());
            verify(mockPreparedStatement).setDate(4, Date.valueOf(testUser.getDateNaissance()));
            verify(mockPreparedStatement).executeUpdate();

            System.out.println("AddUser test passed!");
        }
    }

    @Test
    @Order(2)
    @DisplayName("Test Get User By ID - Found")
    void testGetUserById_Found() throws SQLException {
        System.out.println("\n Testing getUserById()");

        try (MockedStatic<DatabaseConnection> mockedStatic = mockStatic(DatabaseConnection.class)) {
            mockedStatic.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("id")).thenReturn(testUser.getId());
            when(mockResultSet.getString("name")).thenReturn(testUser.getName());
            when(mockResultSet.getString("email")).thenReturn(testUser.getEmail());
            when(mockResultSet.getString("phone")).thenReturn(testUser.getPhone());
            when(mockResultSet.getDate("date_naissance")).thenReturn(Date.valueOf(testUser.getDateNaissance()));

            User result = userDAO.getUserById(1);

            assertNotNull(result, "User should be found");
            assertEquals(testUser.getId(), result.getId());
            assertEquals(testUser.getName(), result.getName());
            assertEquals(testUser.getEmail(), result.getEmail());
            assertEquals(testUser.getPhone(), result.getPhone());
            assertEquals(testUser.getDateNaissance(), result.getDateNaissance());

            verify(mockPreparedStatement).setInt(1, 1);

            System.out.println("GetUserById test passed successfully!");
        }
    }

    @Test
    @Order(3)
    @DisplayName("Test Update User - Success")
    void testUpdateUser_Success() throws SQLException {
        System.out.println("\n Testing updateUser()");

        try (MockedStatic<DatabaseConnection> mockedStatic = mockStatic(DatabaseConnection.class)) {
            mockedStatic.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            boolean result = userDAO.updateUser(testUser);
            assertTrue(result, "UpdateUser should return true for successful update");

            verify(mockPreparedStatement).setString(1, testUser.getName());
            verify(mockPreparedStatement).setString(2, testUser.getEmail());
            verify(mockPreparedStatement).setString(3, testUser.getPhone());
            verify(mockPreparedStatement).setDate(4, Date.valueOf(testUser.getDateNaissance()));
            verify(mockPreparedStatement).setInt(5, testUser.getId());

            System.out.println("UpdateUser test passed successfully!");
        }
    }

    @Test
    @Order(4)
    @DisplayName("Test Delete User - Success")
    void testDeleteUser_Success() throws SQLException {
        System.out.println("\nTesting deleteUser()");

        try (MockedStatic<DatabaseConnection> mockedStatic = mockStatic(DatabaseConnection.class)) {
            mockedStatic.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            boolean result = userDAO.deleteUser(1);
            assertTrue(result, "DeleteUser should return true for successful deletion");
            verify(mockPreparedStatement).setInt(1, 1);
            System.out.println("DeleteUser test passed successfully!");
        }
    }

    @Test
    @Order(5)
    @DisplayName("Test Email Exists - True")
    void testEmailExists_True() throws SQLException {
        System.out.println("\nTesting emailExists()");

        try (MockedStatic<DatabaseConnection> mockedStatic = mockStatic(DatabaseConnection.class)) {
            mockedStatic.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt(1)).thenReturn(1);

            boolean result = userDAO.emailExists("Snow.king@winter.com");

            assertTrue(result, "EmailExists should return true when email exists");
            verify(mockPreparedStatement).setString(1, "Snow.king@winter.com");

            System.out.println("EmailExists test passed successfully!");
        }
    }

    @Test
    @Order(6)
    @DisplayName("Test Get All Users - Success")
    void testGetAllUsers_Success() throws SQLException {
        System.out.println("\nTesting getAllUsers()");

        try (MockedStatic<DatabaseConnection> mockedStatic = mockStatic(DatabaseConnection.class)) {
            mockedStatic.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, true, false);
            when(mockResultSet.getInt("id")).thenReturn(1, 2);
            when(mockResultSet.getString("name")).thenReturn("John Snow", "Jane Smith");
            when(mockResultSet.getString("email")).thenReturn("Snow.king@winter.com", "jane@Smith.com");
            when(mockResultSet.getString("phone")).thenReturn("+1234567890", "+0987654321");
            when(mockResultSet.getDate("date_naissance"))
                    .thenReturn(Date.valueOf(LocalDate.of(1990, 5, 15)),
                            Date.valueOf(LocalDate.of(1985, 10, 20)));

            List<User> result = userDAO.getAllUsers();

            assertNotNull(result, "Result should not be null");
            assertEquals(2, result.size(), "Should return 2 users");

            assertEquals("John Snow", result.get(0).getName());
            assertEquals("Jane Smith", result.get(1).getName());

            System.out.println("GetAllUsers test passed successfully!");
        }
    }

    @Test
    @Order(9)
    @DisplayName("Test Get User Count - Success")
    void testGetUserCount_Success() throws SQLException {
        System.out.println("\nTesting getUserCount()");

        try (MockedStatic<DatabaseConnection> mockedStatic = mockStatic(DatabaseConnection.class)) {
            mockedStatic.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt(1)).thenReturn(5);

            int result = userDAO.getUserCount();
            assertEquals(5, result, "Should return correct user count");

            System.out.println("GetUserCount test passed successfully!");
        }
    }

    @AfterEach
    void tearDown() {
        System.out.println("Test cleanup completed\n");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("All UserDAO tests completed successfully!");
        System.out.println("Test Summary:");
        System.out.println("Add User (Success & Error)");
        System.out.println(" Get User By ID (Found & Not Found)");
        System.out.println("Update User");
        System.out.println("Delete User");
        System.out.println("Email Exists");
        System.out.println("Get All Users");
        System.out.println("Get User Count");
    }
}