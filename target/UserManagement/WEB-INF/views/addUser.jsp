<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New User - User Management</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/forms.css">
    
</head>
<body>
    <div class="container">
        <header class="header">
            <h1><i class="fas fa-user-plus"></i> Add New User</h1>
        </header>

        <a href="${pageContext.request.contextPath}/users" class="back-link">
            <i class="fas fa-arrow-left"></i>
            Back to User List
        </a>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-triangle"></i>
                ${errorMessage}
            </div>
        </c:if>

        <div class="form-container">
            <div class="form-info">
                <p><i class="fas fa-info-circle"></i> Fill in the details below to create a new user account. Fields marked with * are required.</p>
            </div>

            <form action="${pageContext.request.contextPath}/users?action=add" method="post" id="addUserForm">
                <div class="form-group">
                    <label for="name" class="required-field">
                        <i class="fas fa-user"></i>Full Name
                    </label>
                    <input type="text" 
                           id="name" 
                           name="name" 
                           placeholder="Enter user's full name"
                           required 
                           maxlength="100"
                           value="${param.name}">
                </div>

                <div class="form-group">
                    <label for="email" class="required-field">
                        <i class="fas fa-envelope"></i>Email Address
                    </label>
                    <input type="email" 
                           id="email" 
                           name="email" 
                           placeholder="user@example.com"
                           required 
                           maxlength="150"
                           value="${param.email}">
                </div>

                <div class="form-group">
                    <label for="phone">
                        <i class="fas fa-phone"></i>Phone Number
                    </label>
                    <input type="tel" 
                           id="phone" 
                           name="phone" 
                           placeholder="+1234567890 (optional)"
                           maxlength="20"
                           value="${param.phone}">
                </div>

                <div class="form-group">
                    <label for="dateNaissance">
                        <i class="fas fa-calendar-alt"></i>Date of Birth
                    </label>
                    <input type="date" 
                           id="dateNaissance" 
                           name="dateNaissance"
                           max="<%= java.time.LocalDate.now() %>"
                           value="${param.dateNaissance}">
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary btn-large">
                        <i class="fas fa-user-plus"></i>
                        Create User
                    </button>
                    <a href="${pageContext.request.contextPath}/users" class="btn btn-secondary btn-large">
                        <i class="fas fa-times"></i>
                        Cancel
                    </a>
                </div>
            </form>
        </div>

        <footer class="footer">
            <p>
                <i class="fas fa-shield-alt"></i>
                All user data is securely stored and protected
            </p>
        </footer>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('addUserForm');
            const submitBtn = form.querySelector('button[type="submit"]');
            
            form.addEventListener('submit', function(e) {
                submitBtn.classList.add('loading');
                submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Creating...';
                submitBtn.disabled = true;
            });

            // Real-time validation feedback
            const inputs = form.querySelectorAll('input[required]');
            inputs.forEach(input => {
                input.addEventListener('blur', function() {
                    if (this.value.trim() === '') {
                        this.style.borderColor = 'var(--error-red)';
                    } else {
                        this.style.borderColor = 'var(--success-green)';
                    }
                });

                input.addEventListener('input', function() {
                    if (this.value.trim() !== '') {
                        this.style.borderColor = 'var(--success-green)';
                    }
                });
            });

            const emailInput = document.getElementById('email');
            emailInput.addEventListener('blur', function() {
                const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (this.value && !emailPattern.test(this.value)) {
                    this.style.borderColor = 'var(--error-red)';
                } else if (this.value) {
                    this.style.borderColor = 'var(--success-green)';
                }
            });

            // Phone number formatting (optional)
            const phoneInput = document.getElementById('phone');
            phoneInput.addEventListener('input', function() {
                // Remove non-numeric characters except + and -
                this.value = this.value.replace(/[^\d+\-\s()]/g, '');
            });

            // Auto-focus on first input
            document.getElementById('name').focus();
        });
    </script>
</body>
</html>