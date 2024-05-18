# Use an OpenJDK runtime as the base image
FROM openjdk:21-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY target/blockchain-viewer-0.0.1-SNAPSHOT.jar /app/

# Copy the wait-for-it.sh script into the container at /app
COPY wait-for-it.sh /app/

# Set execution permissions for the script
RUN chmod +x wait-for-it.sh

# Expose port 8333 for Bitcoin network communication
EXPOSE 8333
EXPOSE 8080

# Specify the command to run your application
CMD ["./wait-for-it.sh", "postgres:5432", "--", "java", "-jar", "blockchain-viewer-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]
