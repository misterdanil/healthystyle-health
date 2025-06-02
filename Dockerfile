FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /health

COPY ../util/pom.xml /util/pom.xml
WORKDIR /util
RUN mvn dependency:go-offline -B

COPY ../util .
RUN mvn -f /util/pom.xml clean install -DskipTests

WORKDIR /health

COPY ./health/pom.xml .
COPY ./health/health-model/pom.xml ./health-model/pom.xml
COPY ./health/health-repository/pom.xml ./health-repository/pom.xml
COPY ./health/health-service/pom.xml ./health-service/pom.xml
COPY ./health/health-web/pom.xml ./health-web/pom.xml
COPY ./health/health-app/pom.xml ./health-app/pom.xml

RUN mvn dependency:go-offline -B

COPY ./health .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /health
COPY --from=build /health/health-app/target/*.jar health.jar
EXPOSE 3006
ENTRYPOINT ["java", "-jar", "health.jar"]
