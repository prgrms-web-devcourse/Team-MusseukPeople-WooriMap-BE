FROM openjdk:11-jdk
EXPOSE 8082
ARG JAR_FILE=/build/libs/woorimap-notification-0.0.1-SNAPSHOT.jar
VOLUME ["/logs"]
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar", "-Duser.timezone=Asia/Seoul","/app.jar"]
