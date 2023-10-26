FROM openjdk:17-jdk-alpine
COPY target/dbecommercev1-0.0.1-SNAPSHOT.jar dbecommercev1.jar
ENTRYPOINT ["java", "-jar", "dbecommercev1.jar"]