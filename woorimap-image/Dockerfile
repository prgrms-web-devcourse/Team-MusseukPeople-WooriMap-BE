## Dockerfile-dev
FROM openjdk:11-jdk
EXPOSE 8081
ARG JAR_FILE=/build/libs/woorimap-image-0.0.1-SNAPSHOT.jar
VOLUME ["/logs/image"]
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar", "-Duser.timezone=Asia/Seoul","/app.jar"]
