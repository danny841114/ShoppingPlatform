FROM openjdk:21-jdk-slim

WORKDIR /root

COPY target/ShoppingPlatform-0.0.1-SNAPSHOT.jar root.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "root.jar"]