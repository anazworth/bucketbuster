FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY . .
RUN ./gradlew shadowJar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -jar build/libs/$(ls build/libs | grep jar)"]
