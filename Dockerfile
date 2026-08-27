FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw

RUN ./mvnw clean package -Dmaven.test.skip=true

EXPOSE 10000

CMD ["java", "-jar", "target/UrlShortner-0.0.1-SNAPSHOT.jar"]