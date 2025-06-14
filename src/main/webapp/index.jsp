<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Management System - Welcome</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
</head>
<body>
    <div class="container">
        <div class="hero-section">
            <div class="hero-content">
                <div class="hero-icon">
                    <i class="fas fa-users-cog"></i>
                </div>
                <h1 class="hero-title">User Management System</h1>
                <p class="hero-subtitle">
                    User administration platform built with Java web technologies
                </p>
                <div class="hero-features">
                    <div class="feature-item">
                        <i class="fas fa-shield-alt"></i>
                        <span>Secure & Reliable</span>
                    </div>
                    <div class="feature-item">
                        <i class="fas fa-bolt"></i>
                        <span>Fast Performance</span>
                    </div>
                    <div class="feature-item">
                        <i class="fas fa-mobile-alt"></i>
                        <span>Responsive Design</span>
                    </div>
                </div>
                <div class="hero-actions">
                    <a href="${pageContext.request.contextPath}/users" class="btn btn-primary btn-large">
                        <i class="fas fa-tachometer-alt"></i>
                        Go to Dashboard
                    </a>
                    <a href="${pageContext.request.contextPath}/users?action=add" class="btn btn-secondary btn-large">
                        <i class="fas fa-user-plus"></i>
                        Add User
                    </a>
                </div>
            </div>
        </div>

        <footer class="footer">
            <div class="footer-content">
                <div class="footer-section">
                    <h4><i class="fas fa-code"></i> Technologies</h4>
                    <ul>
                        <li>Java 21 + Jakarta EE</li>
                        <li>PostgreSQL Database</li>
                        <li>Apache Tomcat Server</li>
                        <li>Maven Build System</li>
                    </ul>
                </div>
                <div class="footer-section">
                    <h4><i class="fas fa-cogs"></i> Features</h4>
                    <ul>
                        <li>User CRUD Operations</li>
                        <li>Responsive Design</li>
                    </ul>
                </div>
                <div class="footer-section">
                    <h4><i class="fas fa-info-circle"></i> About</h4>
                    <p>A User management system built with Java technologies for  user administration.</p>
                </div>
            </div>
            <div class="footer-bottom">
                <p>
                    <i class="fas fa-heart" style="color:rgb(92, 8, 8);"></i>
                    Built with Java, JSP & PostgreSQL &copy; 2025
                </p>
            </div>
        </footer>
    </div>

    <script>
      
        document.addEventListener('DOMContentLoaded', function() {
            const buttons = document.querySelectorAll('.btn');
            buttons.forEach(button => {
                button.addEventListener('click', function(e) {
                    const ripple = document.createElement('span');
                    const rect = this.getBoundingClientRect();
                    const size = Math.max(rect.width, rect.height);
                    const x = e.clientX - rect.left - size / 2;
                    const y = e.clientY - rect.top - size / 2;
                    
                    ripple.style.cssText = `
                        position: absolute;
                        border-radius: 50%;
                        background: rgba(255, 255, 255, 0.3);
                        width: ${size}px;
                        height: ${size}px;
                        left: ${x}px;
                        top: ${y}px;
                        transform: scale(0);
                        animation: ripple 0.6s ease-out;
                        pointer-events: none;
                    `;
                    
                    this.style.position = 'relative';
                    this.style.overflow = 'hidden';
                    this.appendChild(ripple);
                    
                    setTimeout(() => ripple.remove(), 600);
                });
            });

            document.addEventListener('keydown', function(e) {
                if (e.altKey && e.key === 'd') {
                    e.preventDefault();
                    window.location.href = '${pageContext.request.contextPath}/users';
                }
            });
        });
    </script>