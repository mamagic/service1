FROM openjdk:17-jdk

#RUN apt-get update && apt-get -y install sudo

ARG JAR_FILE="build/libs/oauth2server-0.0.1-SNAPSHOT.jar"

COPY ${JAR_FILE} app.jar

ENV    PROFILE dev

ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-jar","/app.jar"]