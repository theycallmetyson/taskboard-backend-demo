# Use an official JDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the host into the container
COPY target/taskboard-0.0.1-SNAPSHOT.jar /app/taskboard-0.0.1-SNAPSHOT.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "taskboard-0.0.1-SNAPSHOT.jar"]
