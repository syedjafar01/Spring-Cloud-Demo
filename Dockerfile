FROM maven:3.9.11-eclipse-temurin-17 AS build

ARG MODULE
WORKDIR /workspace

COPY pom.xml .
COPY discovery-service/pom.xml discovery-service/pom.xml
COPY application-server/pom.xml application-server/pom.xml
COPY application-client/pom.xml application-client/pom.xml
COPY discovery-service/src discovery-service/src
COPY application-server/src application-server/src
COPY application-client/src application-client/src

RUN mvn -pl ${MODULE} -am clean package -DskipTests

FROM eclipse-temurin:17-jre

ARG MODULE
ENV MODULE=${MODULE}
WORKDIR /app

COPY --from=build /workspace/${MODULE}/target/${MODULE}-2.0.0.jar /app/app.jar

EXPOSE 8080 8761

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
