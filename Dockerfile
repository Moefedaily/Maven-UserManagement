FROM tomcat:10.1-jdk21

# Remove default webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy your WAR file
COPY target/UserManagement.war /usr/local/tomcat/webapps/UserManagement.war

# Debug: List what we copied
RUN ls -la /usr/local/tomcat/webapps/

# Expose port
EXPOSE 8080

# Add some debugging output
RUN echo "Starting Tomcat..."

# Start Tomcat
CMD ["catalina.sh", "run"]