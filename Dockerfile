FROM maven:3.9.11-eclipse-temurin-17 AS build

ARG MODULE
WORKDIR /workspace

COPY pom.xml .
COPY discovery-service/pom.xml discovery-service/pom.xml
COPY greeting-service/pom.xml greeting-service/pom.xml
COPY consumer-service/pom.xml consumer-service/pom.xml
COPY gateway-service/pom.xml gateway-service/pom.xml
COPY discovery-service/src discovery-service/src
COPY greeting-service/src greeting-service/src
COPY consumer-service/src consumer-service/src
COPY gateway-service/src gateway-service/src

RUN mvn -pl ${MODULE} -am clean package -DskipTests

FROM eclipse-temurin:17-jre

ARG MODULE
ENV MODULE=${MODULE}
WORKDIR /app

COPY --from=build /workspace/${MODULE}/target/${MODULE}-2.0.0.jar /app/app.jar

EXPOSE 8080 8761

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
