FROM openjdk:19-jdk-slim
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

WORKDIR /app

COPY . /app

COPY mysql-connector-java-8.0.26.jar /app/

RUN javac -cp .:mysql-connector-java-8.0.26.jar src/portscan_linux/*.java

CMD ["java", "-cp", "src:mysql-connector-java-8.0.26.jar", "portscan_linux.PortScan_Linux"]
