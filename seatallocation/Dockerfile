FROM maven:3.9.0-eclipse-temurin-17 AS builder
WORKDIR /app
COPY seatallocation/pom.xml ./pom.xml
COPY seatallocation/src ./src
COPY seatallocation/.mvn ./.mvn
COPY seatallocation/mvnw ./mvnw
COPY seatallocation/mvnw.cmd ./mvnw.cmd
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/seatallocation-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
