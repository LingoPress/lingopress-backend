FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "-Dspring.profiles.active=prod", "/app.jar", "-Dspring-boot.run.arguments=--datasource-username=${DATASOURCE_USERNAME}, --datasource-password=${DATASOURCE_PASSWORD}, --chatgpt-key=${CHATGPT_KEY}, --jwt-secret=${JWT_SECRET}, --sentry-dsn=${SENTRY_DSN}, --google-secret=${GOOGLE_SECRET}, --google-redirect-uri=${GOOGLE_REDIRECT_URI}, --google-client-id=${GOOGLE_CLIENT_ID}, --google-client-secret=${GOOGLE_CLIENT_SECRET}, --google-refresh-token=${GOOGLE_REFRESH_TOKEN}, --google-access-token=${GOOGLE_ACCESS_TOKEN}, --google-expire-time=${GOOGLE_EXPIRE_TIME}, --google-token-type=${GOOGLE_TOKEN_TYPE}, --google-scope=${GOOGLE_SCOPE}, --google-token-uri=${GOOGLE_TOKEN_URI}, --google-auth-uri=${GOOGLE_AUTH_URI}, --google-user-info-uri=${GOOGLE_USER_INFO_URI}, --google-user-info-name=${GOOGLE_USER_INFO_NAME}, --google-user-info-email=${GOOGLE_USER_INFO_EMAIL}, --google-user-info-picture=${GOOGLE_USER_INFO_PICTURE}, --ai-url=${LAMBDA_AI_URL}"]

