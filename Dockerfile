# Build
FROM gradle:jdk17-corretto-al2023 AS builder
WORKDIR /usr/src/subscription
COPY . .
RUN chown -R gradle:gradle /usr/src/subscription
RUN gradle clean build -x test

# Package
FROM openjdk:17-slim-buster
WORKDIR /app
COPY --from=builder /usr/src/subscription/build/libs/Subscription*.jar /app/Subscription.jar
EXPOSE 9009
ENTRYPOINT ["java", "-jar", "/app/Subscription.jar"]