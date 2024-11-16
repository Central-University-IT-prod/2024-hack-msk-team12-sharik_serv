FROM gradle:jdk21 AS app-builder
WORKDIR /build
COPY . .
RUN gradle build --warning-mode all -x test

FROM eclipse-temurin:21 AS runner
WORKDIR /app
COPY --from=app-builder /build/build/libs/Sharik-*-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]