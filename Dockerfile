FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY guitar.jar /app/guitar.jar

CMD ["java", "-jar", "/app/guitar.jar"]