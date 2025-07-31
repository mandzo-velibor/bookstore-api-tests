# Use OpenJDK 21.0.7 (Corretto) as base image
FROM amazoncorretto:21.0.7-alpine

# Set working directory
WORKDIR /app

# Install Maven 3.9.11
RUN apk add --no-cache curl tar bash && \
    curl -fsSL https://downloads.apache.org/maven/maven-3/3.9.11/binaries/apache-maven-3.9.11-bin.tar.gz | tar xz -C /opt/ && \
    ln -s /opt/apache-maven-3.9.11/bin/mvn /usr/bin/mvn && \
    rm -rf /var/cache/apk/*

# Copy project files
COPY . /app

# Install dependencies and build
RUN mvn clean install -DskipTests --batch-mode