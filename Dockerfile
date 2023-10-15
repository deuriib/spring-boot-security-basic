FROM eclipse-temurin:17-jammy

ADD target/security-0.0.1-SNAPSHOT.jar /security.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/security.jar"]