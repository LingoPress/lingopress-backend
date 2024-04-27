FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "-Dspring.profiles.active=prod", "/app.jar", "-Dspring-boot.run.arguments=--datasource-username=${DATASOURCE_USERNAME}, --datasource-password=${DATASOURCE_PASSWORD}, --chatgpt-key=${CHATGPT_KEY}, --jwt-secret=${JWT_SECRET}, --sentry-dsn=${SENTRY_DSN}, --google-secret=${GOOGLE_SECRET}"]

