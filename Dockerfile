# Use a Debian-based image with Java 19
FROM openjdk:19-jdk-slim

# Install necessary libraries for GUI applications
RUN apt-get update && apt-get install -y \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libfreetype6 \
    libfontconfig1 \
    nmap \
    net-tools \
    && rm -rf /var/lib/apt/lists/*

# Set the working directory
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Copy the MySQL Connector/J jar into the container
COPY mysql-connector-java-8.0.26.jar /app/

# Compile the Java application
RUN javac -cp .:mysql-connector-java-8.0.26.jar src/portscan_linux/*.java

# Set the entry point to run your Java application
CMD ["java", "-cp", "src:mysql-connector-java-8.0.26.jar", "portscan_linux.PortScan_Linux"]
