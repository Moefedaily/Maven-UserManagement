# User Management System

This is my Java web development assignment - a user management application built to demonstrate understanding of Java and web development.

## Live Demo

**Application URL:** https://maven-usermanagement-production.up.railway.app/UserManagement/

## Assignment overview

This project was created as part of my Java web development coursework. The assignment required building a full-stack web application that manages user data with complete CRUD operations, database integration, and a web interface.

## What this assignment covers

The application manages users with their basic information (names, emails, phone numbers, and birth dates). It includes functionality to add new users, display them in a table format, edit existing records, and delete users from the system.

## Technologies demonstrated

**Backend:**

-   Java 21 with Jakarta EE
-   Maven project management
-   PostgreSQL database integration
-   Apache Tomcat deployment
-   Servlet-based request handling

**Frontend:**

-   JSP templating
-   Custom CSS styling
-   Responsive web design
-   Form validation

**Architecture:**

-   MVC design pattern
-   DAO pattern for data access
-   RESTful URL structure

Through this assignment, I gained experience with:

-   **JSP templating** - This was completely new to me, learning how to mix Java and HTML
-   **Tomcat deployment** - Understanding how web applications actually get deployed and run
-   **Servlet lifecycle** - How requests flow through the application
-   **Automated deployment** - Created a `.bat` script to streamline the build-deploy process
-   **CSS animations and modern styling**
-   **Railway platform** - Figured out how to get this thing running in the cloud

## My approach to the design

Since design isn't my strongest area, I took this as an opportunity to learn some modern CSS techniques. I researched and implemented:

-   CSS animations and keyframes for better user experience
-   Gradient backgrounds
-   Smooth transitions between interface states
-   Dark theme styling

The visual design might be simple, but it demonstrates my willingness to go beyond just backend functionality and create a complete user experience.

## Some challenges I faced

Getting this to work on Railway was... interesting. The main issue was that Railway gives you database URLs in one format (`postgresql://user:pass@host:port/db`) but the PostgreSQL driver expects a different format (`jdbc:postgresql://host:port/db`).

So I had to write a simple URL parser to fix this. Also had some fun with classloader issues in the container environment. Nothing too crazy, just had to figure out how to make the PostgreSQL driver load properly.

In the end, it was a good learning experience about how things work differently between local development and cloud environments.

## What's under the hood

**Backend Technologies:**

-   Java 21 with Jakarta EE
-   Maven for project management
-   PostgreSQL database
-   Apache Tomcat server

**Frontend Experience:**

-   JSP pages with modern HTML5
-   Custom CSS with dark theme
-   Responsive design
-   Smooth animations and interactions

**Architecture:**
-   MVC pattern (Model-View-Controller)
-   Data Access Object (DAO) pattern
-   RESTful URL structure


## Assignment requirements

### What you'll need to run this

-   Java 21+
-   Apache Tomcat 10.1
-   PostgreSQL database
-   Maven 3.6+

### Setup instructions

1. **Database setup** - Create a PostgreSQL database named `usermanagement`
2. **Configure connection** - Update database credentials in `DatabaseConnection.java` if needed
3. **Build the project**:
    ```bash
    mvn clean package
    ```
4. **Deploy** - Copy the generated `.war` file to Tomcat's webapps folder
5. **Access** - Visit `http://localhost:8080/UserManagement`

Alternatively, use the included `deploy.bat` script for automated deployment.

## Application features

-   **User listing** - View all users in a responsive table
-   **Add users** - Create new user records with validation
-   **Edit users** - Update existing user information
-   **Delete users** - Remove users with confirmation
-   **Data persistence** - All data stored in PostgreSQL database
-   **Error handling** - Proper validation and error messages

## Project structure

```
src/main/java/
├── controller/     # Servlet controllers for web requests
├── dao/           # Database access layer
├── model/         # User entity and data models

src/main/webapp/
├── css/           # Custom stylesheets
├── WEB-INF/views/ # JSP view templates
└── index.jsp      # Application entry point

src/test/java/     # JUnit tests with Mockito

Dockerfile         # Container configuration
railway.toml       # Railway platform settings
deploy.bat         # Local deployment script
```

## Testing approach

The assignment includes comprehensive testing:

-   **Unit tests** for DAO methods using Mockito mocks

Run tests with: `mvn test`

_Built with curiosity and lots of coffee_ ☕☕☕☕☕☕☕☕☕☕☕☕
