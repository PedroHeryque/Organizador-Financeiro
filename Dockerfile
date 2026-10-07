# Estágio de Build usando JDK 26
FROM eclipse-temurin:26-jdk-alpine AS build
WORKDIR /app

# Copia as configurações do Gradle e o wrapper
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Baixa as dependências do projeto
RUN ./gradlew dependencies --no-daemon || true

# Copia o código-fonte e gera o JAR
COPY src ./src
RUN ./gradlew bootJar -x test --no-daemon

# Estágio de Execução usando JRE 26
FROM eclipse-temurin:26-jre-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]