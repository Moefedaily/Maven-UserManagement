<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="jakarta.tags.core" %> <%@ taglib prefix="fmt"
uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>User Management - User List</title>
        <link
            rel="stylesheet"
            href="${pageContext.request.contextPath}/css/style.css"
        />
        <link
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
            rel="stylesheet"
        />
    </head>
    <body>
        <div class="container">
            <!-- Header -->
            <header class="header">
                <h1><i class="fas fa-users"></i> User Management System</h1>
                <p>Manage your users efficiently</p>
            </header>

            <!-- Success/Error Messages -->
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i>
                    ${successMessage}
                </div>
            </c:if>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-circle"></i>
                    ${errorMessage}
                </div>
            </c:if>

            <!-- Stats and Actions -->
            <div class="stats-section">
                <div class="stats-card">
                    <i class="fas fa-user-friends"></i>
                    <div>
                        <h3>${userCount}</h3>
                        <p>Total Users</p>
                    </div>
                </div>

                <div class="actions">
                    <a
                        href="${pageContext.request.contextPath}/users?action=add"
                        class="btn btn-primary"
                    >
                        <i class="fas fa-plus"></i> Add New User
                    </a>
                    <button
                        onclick="location.reload()"
                        class="btn btn-secondary"
                    >
                        <i class="fas fa-refresh"></i> Refresh
                    </button>
                </div>
            </div>

            <!-- Users Table -->
            <div class="table-container">
                <c:choose>
                    <c:when test="${empty users}">
                        <div class="empty-state">
                            <i class="fas fa-user-slash"></i>
                            <h3>No Users Found</h3>
                            <p>Get started by adding your first user!</p>
                            <a
                                href="${pageContext.request.contextPath}/users?action=add"
                                class="btn btn-primary"
                            >
                                <i class="fas fa-plus"></i> Add First User
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <table class="users-table">
                            <thead>
                                <tr>
                                    <th><i class="fas fa-hashtag"></i> ID</th>
                                    <th><i class="fas fa-user"></i> Name</th>
                                    <th>
                                        <i class="fas fa-envelope"></i> Email
                                    </th>
                                    <th><i class="fas fa-phone"></i> Phone</th>
                                    <th>
                                        <i class="fas fa-birthday-cake"></i>
                                        Birth Date
                                    </th>
                                    <th><i class="fas fa-cogs"></i> Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach
                                    var="user"
                                    items="${users}"
                                    varStatus="status"
                                >
                                    <tr
                                        class="${status.index % 2 == 0 ? 'even' : 'odd'}"
                                    >
                                        <td class="id-cell">${user.id}</td>
                                        <td class="name-cell">
                                            <div class="user-avatar">
                                                ${user.name.substring(0,
                                                1).toUpperCase()}
                                            </div>
                                            <span>${user.name}</span>
                                        </td>
                                        <td class="email-cell">
                                            <a href="mailto:${user.email}"
                                                >${user.email}</a
                                            >
                                        </td>
                                        <td class="phone-cell">
                                            <c:choose>
                                                <c:when
                                                    test="${not empty user.phone}"
                                                >
                                                    <a href="tel:${user.phone}"
                                                        >${user.phone}</a
                                                    >
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="no-data"
                                                        >N/A</span
                                                    >
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="date-cell">
                                            <c:choose>
                                                <c:when
                                                    test="${not empty user.dateNaissance}"
                                                >
                                                    ${formattedDates[user.id]}
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="no-data"
                                                        >N/A</span
                                                    >
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="actions-cell">
                                            <div class="action-buttons">
                                                <a
                                                    href="${pageContext.request.contextPath}/users?action=edit&id=${user.id}"
                                                    class="btn btn-edit"
                                                    title="Edit User"
                                                >
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <button
                                                    onclick="confirmDelete(${user.id}, '${user.name}')"
                                                    class="btn btn-delete"
                                                    title="Delete User"
                                                >
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>

            <footer class="footer">
                <p>
                    &copy; 2025 User Management System. Built with Java, JSP &
                    PostgreSQL.
                </p>
            </footer>
        </div>

        <script>
            function confirmDelete(userId, userName) {
                // Store values immediately to prevent corruption
                const id = parseInt(userId)
                const name = String(userName || 'Unknown User')

                console.log('Stored ID:', id)
                console.log('Stored Name:', name)

                if (
                    confirm(
                        'Are you sure you want to delete user "' +
                            name +
                            '"?\n\nThis action cannot be undone.'
                    )
                ) {
                    // Use simple string concatenation
                    window.location.href = 'users?action=delete&id=' + id
                }
            }
            document.addEventListener('DOMContentLoaded', function () {
                const alerts = document.querySelectorAll('.alert')
                alerts.forEach((alert) => {
                    setTimeout(() => {
                        alert.style.opacity = '0'
                        setTimeout(() => alert.remove(), 300)
                    }, 5000)
                })
            })

            // Add loading animation for buttons
            document.querySelectorAll('.btn').forEach((btn) => {
                btn.addEventListener('click', function () {
                    if (!this.classList.contains('btn-delete')) {
                        this.classList.add('loading')
                    }
                })
            })
        </script>
    </body>
</html>
