FROM openjdk:17-jdk-slim
WORKDIR /memitService
COPY . .
RUN ./gradlew build
CMD ["java", "-jar", "build/libs/memitService-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080