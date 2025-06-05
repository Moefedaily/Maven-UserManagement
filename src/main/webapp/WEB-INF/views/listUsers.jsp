<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Management - Dashboard</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <h1><i class="fas fa-users-cog"></i> User Management System</h1>
        </header>

        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i>
                ${successMessage}
            </div>
        </c:if>
        
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-triangle"></i>
                ${errorMessage}
            </div>
        </c:if>

        <div class="stats-section">
            <div class="stats-card">
                <i class="fas fa-users"></i>
                <div>
                    <h3>${userCount}</h3>
                    <p>Total Users</p>
                </div>
            </div>
            
            <div class="actions">
                <a href="${pageContext.request.contextPath}/users?action=add" class="btn btn-primary">
                    <i class="fas fa-user-plus"></i> Add New User
                </a>
                <button onclick="location.reload()" class="btn btn-secondary">
                    <i class="fas fa-sync-alt"></i> Refresh Data
                </button>
            </div>
        </div>

        <div class="table-container">
            <c:choose>
                <c:when test="${empty users}">
                    <div class="empty-state">
                        <i class="fas fa-user-slash"></i>
                        <h3>No Users Found</h3>
                        <p>Start building your user base by adding the first user to your system!</p>
                        <a href="${pageContext.request.contextPath}/users?action=add" class="btn btn-primary">
                            <i class="fas fa-user-plus"></i> Create First User
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="users-table">
                        <thead>
                            <tr>
                                <th><i class="fas fa-hashtag"></i> ID</th>
                                <th><i class="fas fa-user"></i> User</th>
                                <th><i class="fas fa-envelope"></i> Email Address</th>
                                <th><i class="fas fa-phone"></i> Phone</th>
                                <th><i class="fas fa-calendar-alt"></i> Birth Date</th>
                                <th><i class="fas fa-cogs"></i> Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="user" items="${users}" varStatus="status">
                                <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                                    <td class="id-cell">#${user.id}</td>
                                    <td class="name-cell">
                                        <div class="user-avatar">
                                            ${fn:substring(user.name, 0, 1)}
                                        </div>
                                        <div>
                                            <strong>${user.name}</strong>
                                        </div>
                                    </td>
                                    <td class="email-cell">
                                        <a href="mailto:${user.email}" title="Send email to ${user.name}">
                                            <i class="fas fa-envelope"></i> ${user.email}
                                        </a>
                                    </td>
                                    <td class="phone-cell">
                                        <c:choose>
                                            <c:when test="${not empty user.phone}">
                                                <a href="tel:${user.phone}" title="Call ${user.name}">
                                                    <i class="fas fa-phone"></i> ${user.phone}
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="no-data">
                                                    <i class="fas fa-minus"></i> Not provided
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="date-cell">
                                        <c:choose>
                                            <c:when test="${not empty user.dateNaissance}">
                                                <i class="fas fa-birthday-cake"></i> 
                                                ${formattedDates[user.id]}
                                            </c:when>
                                            <c:otherwise>
                                                <span class="no-data">
                                                    <i class="fas fa-minus"></i> Not set
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="actions-cell">
                                        <div class="action-buttons">
                                            <a href="${pageContext.request.contextPath}/users?action=edit&id=${user.id}" 
                                               class="btn btn-edit" title="Edit ${user.name}">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <button onclick="confirmDelete(${user.id}, '${fn:escapeXml(user.name)}')" 
                                                    class="btn btn-delete" title="Delete ${user.name}">
                                                <i class="fas fa-trash-alt"></i>
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
                    <i class="fas fa-heart" style="color:rgb(92, 8, 8);"></i>
                    Built with Java, JSP & PostgreSQL &copy; 2025
                </p>
        </footer>
    </div>

    <script>
        function confirmDelete(userId, userName) {
           console.log("Confirm delete for user ID: " + userId + ", Name: " + userName);
            const result = confirm(
                `⚠️ DELETE USER CONFIRMATION\n\n` +
                `User:` + userName +
                `\n` +
                `ID:` + userId +
                `\n\n` +
                `This action cannot be undone!\n` +
                `Are you absolutely sure?`
            );
            
            if (result) {
                const deleteBtn = event.target.closest('.btn-delete');
                deleteBtn.classList.add('loading');
                deleteBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i>';   
                window.location.href = 'users?action=delete&id=' + userId;
            }
        }

        document.addEventListener('DOMContentLoaded', function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(alert => {
                setTimeout(() => {
                    alert.style.transform = 'translateX(100%)';
                    alert.style.opacity = '0';
                    setTimeout(() => alert.remove(), 300);
                }, 5000);
            });

            const tableRows = document.querySelectorAll('.users-table tbody tr');
            tableRows.forEach(row => {
                row.addEventListener('mouseenter', function() {
                    this.style.transform = 'scale(1.02)';
                });
                
                row.addEventListener('mouseleave', function() {
                    this.style.transform = 'scale(1)';
                });
            });
        });

        document.querySelectorAll('.btn:not(.btn-delete)').forEach(btn => {
            btn.addEventListener('click', function() {
                this.classList.add('loading');
                setTimeout(() => this.classList.remove('loading'), 2000);
            });
        });

        document.addEventListener('keydown', function(e) {
            if (e.altKey && e.key === 'n') {
                e.preventDefault();
                window.location.href = '${pageContext.request.contextPath}/users?action=add';
            }
            
            if (e.altKey && e.key === 'r') {
                e.preventDefault();
                location.reload();
            }
        });
    </script>
</body>
</html>