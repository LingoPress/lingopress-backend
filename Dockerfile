FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java", "-jar",  "/app.jar"]
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "-Dspring.profiles.active=prod", "/app.jar", "-Dspring-boot.run.arguments=--datasource-username=${DATASOURCE_USERNAME}, --datasource-password=${DATASOURCE_PASSWORD}, --deepl-key=${DEEPL_API_KEY}, --jwt-secret=${JWT_SECRET}"]

